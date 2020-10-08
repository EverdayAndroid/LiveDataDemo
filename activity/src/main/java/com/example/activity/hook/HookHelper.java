package com.example.activity.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.activity.StubActivity;
import com.example.activity.TargetActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookHelper {
    private static String TAG = "HookHelper";
    private static final String EXTRA_TARGET_INTENT = "extra_target_intent";
    public static void hookIActivityManager() {
        try {
            Field IActivityTaskManagerSingletonField = null;
            Class<?> activityTaskManager = Class.forName("android.app.ActivityTaskManager");
            //获取单例对象
            IActivityTaskManagerSingletonField = activityTaskManager.getDeclaredField("IActivityTaskManagerSingleton");
            IActivityTaskManagerSingletonField.setAccessible(true);
            //获取Singleton<IActivityTaskManager> IActivityTaskManagerSingleton
            Object IActivityTaskManagerSingleton = IActivityTaskManagerSingletonField.get(null);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstance = singletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            //获取IActivityTaskManager
            final Object IActivityTaskManager = mInstance.get(IActivityTaskManagerSingleton);

            //进行动态代理
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Class<?> iActivityTaskManagerInterface = Class.forName("android.app.IActivityTaskManager");
            Object proxy = Proxy.newProxyInstance(contextClassLoader, new Class[]{iActivityTaskManagerInterface}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    Intent oldIntent = null;
                    String pkg = "com.example.activity";
                    int index = 0;
                    if ("startActivity".equals(method.getName())) {
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Intent) {
                                Log.e(TAG,"============> startActivity "+method.toGenericString());
                                oldIntent = (Intent) args[i];
//                                pkg = oldIntent.getComponent().getPackageName();
                                index = i;
                                break;
                            }
                        }
                        Intent newIntent = new Intent();
                        newIntent.setComponent(new ComponentName(pkg, StubActivity.class.getName()));
                        Log.e(TAG,"============>"+oldIntent.getComponent().getClassName());
                        newIntent.putExtra(EXTRA_TARGET_INTENT,new Intent().setComponent(new ComponentName(pkg, TargetActivity.class.getName())));
                        args[index] = newIntent;
                    }
//                    Log.e(TAG,"===========> method:"+method);
                    if(args!=null){
//                        Log.e(TAG,"===========> args:"+args.length);
                    }else{
//                        Log.e(TAG,"===========> args: null");
                    }

                    return method.invoke(IActivityTaskManager, args);
                }
            });
            mInstance.set(IActivityTaskManagerSingleton, proxy);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

    }

    public static void hookHandler() {
        try {
            Field sCurrentActivityThreadField;
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object activityThread = sCurrentActivityThreadField.get(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(activityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            mCallbackField.set(mH, new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        case 100:
                            try {
                                Object obj = msg.obj;
                                Field intentField = obj.getClass().getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent mIntent = (Intent) intentField.get(obj);
                                Intent targetIntent = mIntent.getParcelableExtra(EXTRA_TARGET_INTENT);
                                mIntent.setComponent(targetIntent.getComponent());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 159:
                            try {
                                Object obj = msg.obj;
                                Field mActivityCallbacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);
                                List<Object> mActivityCallbacks = (List<Object>) mActivityCallbacksField.get(obj);
                                if (mActivityCallbacks.size() > 0) {
                                    Log.e(TAG,mActivityCallbacks.get(0).getClass().getName());
                                    if (mActivityCallbacks.get(0).getClass().getName().equals("android.app.servertransaction.LaunchActivityItem")) {
                                        Object launchActivityItem = mActivityCallbacks.get(0);
                                        Field intentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                                        intentField.setAccessible(true);

                                        Intent mIntent = (Intent) intentField.get(launchActivityItem);
                                        Intent targetIntent = mIntent.getParcelableExtra(EXTRA_TARGET_INTENT);

                                        Log.e(TAG,"===========>"+targetIntent.getComponent().getClassName());

                                        mIntent.setComponent(targetIntent.getComponent());
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                    mH.handleMessage(msg);
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }


    public static void hookHandler1() {
        try {
            Class<?> atClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = atClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            //ActivityThread 一个app进程 只有一个，获取它的mH
            Field mHField = atClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(sCurrentActivityThread);

            //获取mCallback
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            mCallbackField.set(mH, new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {
                    Log.i(TAG, "handleMessage: " + msg.what);
                    switch (msg.what) {
                        case 100: {

                        }
                        break;
                        case 159: {
                            Object obj = msg.obj;
                            Log.i(TAG, "handleMessage: obj=" + obj);
                            try {
                                Field mActivityCallbacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);
                                List mActivityCallbacks = (List) mActivityCallbacksField.get(obj);
                                Log.i(TAG, "handleMessage: mActivityCallbacks= " + mActivityCallbacks);
                                //注意了 这里如果有同学debug调试会发现第一次size=0 原因如下
                                //在Android O之前
                                //public static final int LAUNCH_ACTIVITY         = 100;
                                //public static final int PAUSE_ACTIVITY          = 101;
                                //public static final int PAUSE_ACTIVITY_FINISHING= 102;
                                //public static final int STOP_ACTIVITY_SHOW      = 103;
                                //public static final int STOP_ACTIVITY_HIDE      = 104;
                                //public static final int SHOW_WINDOW             = 105;
                                //public static final int HIDE_WINDOW             = 106;
                                //public static final int RESUME_ACTIVITY         = 107;
                                //public static final int SEND_RESULT             = 108;
                                //public static final int DESTROY_ACTIVITY        = 109;
                                //end
                                //从AndroidP开始重构了状态模式
                                //public static final int EXECUTE_TRANSACTION = 159;
                                // 首先一个app 只有一个ActivityThread 然后就只有一个mH
                                //我们app所有的activity的生命周期的处理都在mH的handleMessage里面处理
                                //在Android 8.0之前，不同的生命周期对应不同的msg.what处理
                                //在Android 8.0 改成了全部由EXECUTE_TRANSACTION来处理
                                //所以这里第一次mActivityCallbacks是MainActivity的生命周期回调的
//                                handleMessage: 159
//                                handleMessage: obj=android.app.servertransaction.ClientTransaction@efd342
//                                handleMessage: mActivityCallbacks= []
//                                invoke: method activityPaused
//                                handleMessage: 159
//                                handleMessage: obj=android.app.servertransaction.ClientTransaction@4962
//                                handleMessage: mActivityCallbacks= [WindowVisibilityItem{showWindow=true}]
//                                handleMessage: size= 1
//                                handleMessage: 159
//                                handleMessage: obj=android.app.servertransaction.ClientTransaction@9e98c6b
//                                handleMessage: mActivityCallbacks= [LaunchActivityItem{intent=Intent { cmp=com.zero.activityhookdemo/.StubActivity (has extras) },ident=168243404,info=ActivityInfo{5b8d769 com.zero.activityhookdemo.StubActivity},curConfig={1.0 310mcc260mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v -nav/h winConfig={ mBounds=Rect(0, 0 - 0, 0) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=undefined} s.6},overrideConfig={1.0 310mcc260mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v -nav/h winConfig={ mBounds=Rect(0, 0 - 1080, 1794) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=standard} s.6},referrer=com.zero.activityhookdemo,procState=2,state=null,persistentState=null,pendingResults=null,pendingNewIntents=null,profilerInfo=null}]
//                                handleMessage: size= 1
                                if (mActivityCallbacks.size() > 0) {
                                    Log.i(TAG, "handleMessage: size= " + mActivityCallbacks.size());
                                    String className = "android.app.servertransaction.LaunchActivityItem";
                                    if (mActivityCallbacks.get(0).getClass().getCanonicalName().equals(className)) {
                                        Object object = mActivityCallbacks.get(0);
                                        Field intentField = object.getClass().getDeclaredField("mIntent");
                                        intentField.setAccessible(true);
                                        Intent intent = (Intent) intentField.get(object);
                                        Intent targetIntent = intent.getParcelableExtra(EXTRA_TARGET_INTENT);
                                        intent.setComponent(targetIntent.getComponent());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    }
                    mH.handleMessage(msg);
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "hookHandler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hookIActivityTaskManager(){
        try{
            Field singletonField = null;
            Class<?> actvityManager = Class.forName("android.app.ActivityTaskManager");
            singletonField = actvityManager.getDeclaredField("IActivityTaskManagerSingleton");
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);
            //拿IActivityManager对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //原始的IActivityTaskManager
            final Object IActivityTaskManager = mInstanceField.get(singleton);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                    , new Class[]{Class.forName("android.app.IActivityTaskManager")}
                    , new InvocationHandler() {
                        @Override
                        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
//                            Log.i(TAG, "invoke: " + method.getName());

                            //偷梁换柱
                            //真正要启动的activity目标
                            Intent raw = null;
                            int index = -1;
                            if ("startActivity".equals(method.getName())) {
                                Log.i(TAG, "invoke: startActivity 启动准备");
                                for (int i = 0; i < args.length; i++) {
                                    if(args[i] instanceof  Intent){
                                        raw = (Intent)args[i];
                                        index = i;
                                    }
                                }
                                Log.i(TAG, "invoke: raw: " + raw);
                                //代替的Intent
                                Intent newIntent = new Intent();
                                newIntent.setComponent(new ComponentName("com.example.activity", StubActivity.class.getName()));
                                newIntent.putExtra(EXTRA_TARGET_INTENT,raw);

                                args[index] = newIntent;

                            }

                            return method.invoke(IActivityTaskManager, args);
                        }
                    });

            //            7. IActivityManagerProxy 融入到framework
            mInstanceField.set(singleton, proxy);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
