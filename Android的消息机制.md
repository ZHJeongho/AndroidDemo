# Android的消息机制

Android的消息机制主要就是指Handler的运行机制。主要围绕Handler、Looper、MessageQueue三者展开。

介绍下基本概念：

1. Handler:一个线程中可以有多个Handler
2. Looper:一个线程中只能有一个Looper
3. MessageQueue:消息队列，实质并不是队列，是一个链表

一般情况下，我们都是使用Handler来更新UI，开启子线程处理一些耗时的操作，比如说网络请求、IO操作等等，耗时操作结束后，调用Handler的sendMessage方法，向MessageQueue中添加消息，Handler再循环处理MessageQueue中的消息（handleMessage方法），更新相应的UI或其他操作。

``` java
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mButton;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x01){
                mTextView.setText("Game Over");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.tv);
        mButton = (Button) findViewById(R.id.btn);

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
	                    //sleep 3秒，模拟耗时操作
                        Thread.sleep(3000);
                        Message msg = Message.obtain();
                        msg.what = 0x01;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
```

布局代码就不上了，效果就是点击Button后3秒钟，TextView显示文本GameOver。
上面这个例子中，Handler的初始化是在HomeActivity中，也就是UI线程中。**如果Handler的初始化不是在主线程中，也就是UI线程的时候，调用sendMessage的方法的之前，必须先调用Looper.prepare()方法，否则就会引发异常Can't create handler inside thread that has not called Looper.prepare()。**这一点我们可以从Handler类的源码中查看到：

``` java
    public Handler(Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                    klass.getCanonicalName());
            }
        }

        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
```
源码中可以看到，mLooper == null的时候就是抛出这个异常。
- 为什么Handler初始化在主线程中，不调用Looper.prepare()就不会抛出异常呢？
这一点我们从**ActivityThread类**中得知，ActivityThread类的main()是整个app的入口。

``` java
    public static void main(String[] args) {
        Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "ActivityThreadMain");
        SamplingProfilerIntegration.start();

        // CloseGuard defaults to true and can be quite spammy.  We
        // disable it here, but selectively enable it later (via
        // StrictMode) on debug builds, but using DropBox, not logs.
        CloseGuard.setEnabled(false);

        Environment.initForCurrentUser();

        // Set the reporter for event logging in libcore
        EventLogger.setReporter(new EventLoggingReporter());

        // Make sure TrustedCertificateStore looks in the right place for CA certificates
        final File configDir = Environment.getUserConfigDirectory(UserHandle.myUserId());
        TrustedCertificateStore.setDefaultUserDirectory(configDir);

        Process.setArgV0("<pre-initialized>");

        Looper.prepareMainLooper();

        ActivityThread thread = new ActivityThread();
        thread.attach(false);

        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }

        if (false) {
            Looper.myLooper().setMessageLogging(new
                    LogPrinter(Log.DEBUG, "ActivityThread"));
        }

        // End of event ActivityThreadMain.
        Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
        Looper.loop();

        throw new RuntimeException("Main thread loop unexpectedly exited");
    }
```
中间有一行Looper.prepareMainLooper()，准备主线程中的Looper，所以说Handler在主线程中初始化，在其调用sendMessage方法时，并不是不需要Looper.prepare，而是因为主线程中在最开始的初始化的时候就已经为主线程初始化好了Looper，**并且这个Looper是不可以退出的，这个我们后边再进行分析。**

所以Handler初始化在子线程时候，基本格式如下：
``` java
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        Message msg = Message.obtain();
                        ...初始化msg
                        handler.sendMessage(msg);
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
```
## Handler初始化
先从Handler的构造方法入手，其方法主要初始化Looper、MessageQueue以及callback。

``` java
    public Handler() {
        this(null, false);
    }
    
    ....
    //调用该构造方法
    public Handler(Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                    klass.getCanonicalName());
            }
        }
		//初始化looper
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
        //初始化MessageQueue
        mQueue = mLooper.mQueue;
        //初始化callback
        mCallback = callback;
        mAsynchronous = async;
    }
```
### Looper初始化
源码中可以看到如果mLooper 为null的时候，就会抛出异常"Can't create handler inside thread that has not called Looper.prepare()"。Looper.myLooper()是从一个ThreadLocal变量中获取。
``` java
	static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>(); 
	...
	...
    public static @Nullable Looper myLooper() {
        return sThreadLocal.get();
    }
```
ThreadLocal实际是类似一个HashMap的存储方式，根据当前的线程去获取一个ThreadLocalMap，Key值就是ThreadLocal变量本身，Value值就是要存储的值。在Android消息机制中，存储的就是Looper。**各个线程所访问的ThreadLocal对象都是不同的。**
``` java
    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null)
                return (T)e.value;
        }
        return setInitialValue();
    }
```
上面讲到的都是Looper的获取，然而并没有找到Looper的初始化。细心点会发现，当mLooper为空的时候，会抛出没有调用Looper.prepare()方法，所以Looper的初始化的必然是在Looper.prepare()中。
``` java
    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }
```
prepare()方法直接调用下边的，先判断该线程是否有Looper，如果有就抛出异常，这里可以看出一个线程中只能拥有一个Looper。如果没有就new一个Looper。**子线程的Looper是允许quit的，而主线程中的Looper是不允许quit的。**

**总结下，就是子线程先调用Looper.prepare()初始化一个looper之后，再进行Handler的初始化，否则就会报错。**
### 初始化MessageQueue
``` java
mQueue = mLooper.mQueue;
```
Handler中的mQueue就是mLooper中的mQueue，所以我们继续学习Looper源码。Looper是在构造方法中初始化mQueue
``` java
    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }
```
MessageQueue实际并不是一个队列，而是以链表的形式存在的。
### 初始化callback
callback是个接口，主要是用于handler发送message的后的回调。
``` java
    public interface Callback {
        public boolean handleMessage(Message msg);
    }
```

``` java
    public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }
```

## Handler将Message插入到MessageQueue中
Handler的sendMessage主要是把Message插入到MessageQueue中。
``` java
    public final boolean sendMessage(Message msg)
    {
        return sendMessageDelayed(msg, 0);
    }
```
sendMessage会最终调用的sendMessageAtTime方法，先判断mQueue是否为空，为空就报错；不为空，就调用Handler的enqueueMessage方法。
``` java
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }
```
最终调用queue.enqueueMessage的方法，也就是mQueue的enqueueMessage方法，将msg(Message变量)插入到MessageQueue中。
``` java
    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }
```
## 循环获取Message，进行相应的处理
将Message插入到MessageQueue中，该如何从MessageQueue循环取出Message进行相应的处理呢？这时候Looper.loop()该出场了。
``` java
    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        Binder.clearCallingIdentity();
        final long ident = Binder.clearCallingIdentity();

        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // This must be in a local variable, in case a UI event sets the logger
            final Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }

            final long traceTag = me.mTraceTag;
            if (traceTag != 0) {
                Trace.traceBegin(traceTag, msg.target.getTraceName(msg));
            }
            try {
                msg.target.dispatchMessage(msg);
            } finally {
                if (traceTag != 0) {
                    Trace.traceEnd(traceTag);
                }
            }

            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }

            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf(TAG, "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }

            msg.recycleUnchecked();
        }
    }
```
loop方法中可以看到有个死循环for(;;)，调用queue.next()方法从MessageQueue中取出Message，再进行一系列的判断，最终调用msg.target.dispatchMessage(msg)方法，实质就是调用Handler.dispatchMessage(msg)。
``` java
    public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }
```
判断msg.callback是否为空之前，先看一下callback在哪里赋值。调用obtain方法获取Message对象的时候，进行callback的赋值，实质是Runnable。
``` java
    public static Message obtain(Handler h, Runnable callback) {
        Message m = obtain();
        m.target = h;
        m.callback = callback;

        return m;
    }
```
一般情况下，直接调用Message.obtian()无参方法来获取Message对象，所以msg.callback为空，接下来判断Handler的mCallback 是否为空。Handler.mCallback类型为Handelr.Callback，实际是一个接口。
``` java
    public interface Callback {
        public boolean handleMessage(Message msg);
    }
```
Handler.mCallback赋值都是在Handler的构造方法中，如果在new一个Handler的时候，给mCallback赋值的话，dispatchMessage方法会调用mCallback.handleMessage。
如果没有对mCallback赋值的话，最终会调用Handler的handleMessage方法。
``` java
    public Handler(Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                    klass.getCanonicalName());
            }
        }

        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
```
``` java
    public Handler(Looper looper, Callback callback, boolean async) {
        mLooper = looper;
        mQueue = looper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
```
所以对于我们顶部的写法，最终会回调到Handler的handleMessage方法。

## 注意事项

- Message对象最好使用Message.obtain方法获取，这种方法是从一个全局的消息池中获取到一个Message对象，避免new新的对象分配新的内存。
``` java
	    /**
     * Return a new Message instance from the global pool. Allows us to
     * avoid allocating new objects in many cases.
     */
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }
```
	