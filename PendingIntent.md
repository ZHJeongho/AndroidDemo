# PendingIntent

即将发生的意图
``` java
class PendingIntent implements Parcelable 
```
PendingIntent实现Parcelable 接口，意味着他可以在其他线程中使用。

##静态方法获取PendingIntent实例

-  启动Activity
	>getActivity(Context context, int requestCode, Intent intent, @Flags int flags)
            
	>getActivities(Context context, int requestCode, @NonNull Intent[] intents, @Flags int flags)
		
-  启动广播
	>getBroadcast(Context context, int requestCode, Intent intent, @Flags int flags)
	
-  启动服务
	>getService(Context context, int requestCode, @NonNull Intent intent, @Flags int flags)

## PendingIntent的Flag

上述的静态方法都比较好理解，细说Flag参数。

Flag参数一共有五个值。

1. **FLAG_ONE_SHOT**
	FLAG_ONE_SHOT导致PendingIntent只能使用一次，send方法调用之后，会自动取消，之后关于相同的PendingIntent的尝试都会失败。
2. **FLAG_NO_CREATE**
	所描述的PendingIntent不会主动创建，如果PendingIntent不存在的话，调用getXXX的方法获取其实例，会返回null。
3. **FLAG_CANCEL_CURRENT**
	在新生成的PendingIntent之前，当前的PendingIntent会被取消。对通知栏来说，被取消的Notification无法打开。
4. **FLAG_UPDATE_CURRENT**
	所描述的PendingIntent如果存在的话，更新它们Intent中的Extra。
5. **FLAG_IMMUTABLE**

## PendingIntent中的主要方法

- send: 执行PendingIntent所关联的操作
``` java
    /**
     * Perform the operation associated with this PendingIntent.
     *
     * @see #send(Context, int, Intent, android.app.PendingIntent.OnFinished, Handler)
     *
     * @throws CanceledException Throws CanceledException if the PendingIntent
     * is no longer allowing more intents to be sent through it.
     */
    public void send() throws CanceledException {
        send(null, 0, null, null, null, null, null);
    }
```
- cancel: 取消当前活动的PendingIntent

``` java
    /**
     * Cancel a currently active PendingIntent.  Only the original application
     * owning a PendingIntent can cancel it.
     */
    public void cancel() {
        try {
            ActivityManagerNative.getDefault().cancelIntentSender(mTarget);
        } catch (RemoteException e) {
        }
    }
```