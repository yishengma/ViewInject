package com.example.asus.viewinject.reflect;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by PirateHat on 18-9-22.
 */

public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> mHandlerRef; //被代理的对象， 例子这里的就是 Activity
    private final HashMap<String,Method> mMethodMap = new HashMap<>(1); //保存方法
    private static final String TAG = "DynamicHandler";

    public DynamicHandler(Object handler) {
        mHandlerRef = new WeakReference<>(handler);
    }

    public void  addMethod(String name,Method method){
        mMethodMap.put(name,method);//两个参数分别是  OnClick 和 handleOnClick 方法
    }

    public Object getHandler(){
         return  mHandlerRef.get();
    }

    public void setHandler(Object handler){
        mHandlerRef = new WeakReference<>(handler);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object handler = mHandlerRef.get();

        if (handler==null){
            return null;
        }
        String methodName = method.getName(); //正在执行的是 onClick 方法
        method = mMethodMap.get(methodName); //handleOnClick
        if (method==null){
            return null;
        }
        return method.invoke(handler,objects); // 将 onClick 方法转换为 handleOnClick 方法。
         //也就是用 View.OnClickListener.Class 方法的 OnClick 方法代理 handleOnClick 方法


    }
}
