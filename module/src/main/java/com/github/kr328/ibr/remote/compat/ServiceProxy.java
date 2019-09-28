package com.github.kr328.ibr.remote.compat;

import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.proxy.IBinderProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class ServiceProxy {
    private static Method startActivity;
    private static Method startActivityTask;
    private static Method asBinder;

    public IBinder proxy(String name, IBinder original) {
        switch (name) {
            case "activity":
                return proxyActivityManager(original);
            case "activity_task":
                return proxyActivityTaskManager(original);
        }
        return original;
    }

    private IBinder proxyActivityManager(IBinder original) {
        return new IBinderProxy(original, (descriptor, i) -> {
            if (IActivityManager.class.getName().equals(descriptor)) {
                IActivityManager am = IActivityManager.Stub.asInterface(original);

                return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                        new Class[]{IActivityManager.class},
                        (instance, method, args) -> {
                            if ( method.equals(startActivity) ) {
                                args[2] = handleStartActivity((Intent)args[2], (String)args[1]);
                            }
                            else if ( method.equals(asBinder) ) {
                                return original;
                            }
                            return method.invoke(am, args);
                        });
            }

            return i;
        });
    }

    private IBinder proxyActivityTaskManager(IBinder original) {
        return new IBinderProxy(original, (descriptor, i) -> {
            if (IActivityTaskManager.class.getName().equals(descriptor)) {
                IActivityTaskManager am = IActivityTaskManager.Stub.asInterface(original);

                return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                        new Class[]{IActivityTaskManager.class},
                        (instance, method, args) -> {
                            if ( method.equals(startActivityTask) ){
                                args[2] = handleStartActivity((Intent)args[2], (String)args[1]);
                            }
                            else if ( method.equals(asBinder) ) {
                                return original;
                            }
                            return method.invoke(am, args);
                        });
            }

            return i;
        });
    }

    protected abstract Intent handleStartActivity(Intent intent, String callingPackage);

    static {
        try {
            startActivity = IActivityManager.class.getMethod("startActivity",
                    IApplicationThread.class, String.class, Intent.class,
                    String.class, IBinder.class, String.class, int.class,
                    int.class, ProfilerInfo.class, Bundle.class);
        } catch (NoSuchMethodException e) {
            Log.e(Constants.TAG, "IActivityManager.startActivity not found", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startActivityTask = IActivityTaskManager.class.getMethod("startActivity",
                        IApplicationThread.class, String.class, Intent.class,
                        String.class, IBinder.class, String.class, int.class,
                        int.class, ProfilerInfo.class, Bundle.class);
            } catch (NoSuchMethodException e) {
                Log.e(Constants.TAG, "IActivityTaskManager.startActivity not found", e);
            }
        }

        try {
            asBinder = IInterface.class.getMethod("asBinder");
        } catch (NoSuchMethodException e) {
            Log.e(Constants.TAG, "IInterface.asBinder", e);
        }
    }
}
