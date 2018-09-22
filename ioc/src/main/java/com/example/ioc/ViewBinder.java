package com.example.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by PirateHat on 18-9-22.
 */

public class ViewBinder {

    private static final  String SUFFIX = "_ViewBinding";

    public static void bind(Activity activity){
        bind(activity,activity);
    }

    public static void bind(final Object host,final  Object root){
        if (host==null||root==null){
            return;
        }
        Class<?> clazz = host.getClass();
        String className = clazz.getName()+ SUFFIX;
        try {
            Class<?> proxyClass = Class.forName(className);
            ViewInject viewInject = (ViewInject) proxyClass.newInstance();
            if (viewInject==null){
                return;
            }
            viewInject.bind(host,root);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }

}
