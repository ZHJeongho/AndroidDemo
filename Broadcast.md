#Android BroadcastReceiver总结
---
>基于郭神的《第一行代码》，都是一些比较基础的知识，后期再进行补充。

Android四大组件之一，不同与Activity，广播没有可视化界面，没有UI。

##广播类型

###标准广播

标准广播是一种异步执行的广播，广播发出之后，所有的接收器几乎都在同一时刻收到广播，并没有先后顺序。**特点：异步、高效率、无法被拦截。**

###有序广播

有序广播是一种同步执行的广播，广播发出之后，同一时刻只能有一个接收器，接收的顺序按照接收器的优先级来决定，先接收到消息的接收器可以拦截掉广播，使其不再继续传递。**特点：同步、优先级决定接收顺序、可被拦截。**

##注册方式

###静态Manifest文件

Broadcast同Activity一样也可以在Manifest文件中进行静态的注册。

- 首先，新建一个XXXReceiver继承BroadcastReceiver，实现BroadcastReceiver中的OnReceive()方法，方法中是接收到广播时所执行的逻辑。

```
public class BootCompleteReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtil.showLong(context, "Boot Complete");
    }
}
```

```
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".activity.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
        <receiver android:name=".broadcast.NetworkChangeReceiver"/>
        <receiver android:name=".broadcast.BootCompleteReceiver">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
        </receiver>
    </application>
```



###动态registerBroadcast

##自定义广播
##本地广播