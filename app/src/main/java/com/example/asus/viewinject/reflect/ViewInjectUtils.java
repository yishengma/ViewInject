package com.example.asus.viewinject.reflect;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by PirateHat on 18-9-22.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";
    private static final String TAG = "ViewInjectUtils";
    public static void bind(Activity activity) {
        injectContentView(activity);
        injectView(activity);
        injectEvents(activity);
    }

    /**
     * 注入 ContentView
     *
     * @param activity Context
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();  // 得到 Activity 类
        ContentView contentView = clazz.getAnnotation(ContentView.class);  // 获取Activity 类中的 Annotation 也就是 ContentView 注解
        if (contentView != null) {
            int contentViewId = contentView.value();  // ContentView 注解中的值 就是 R.layout.layout_main
            try {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);  // 获取对应的 setContentView 方法
                method.setAccessible(true);    // 设置在运行的时候屏蔽这个方法的权限访问
                method.invoke(activity, contentViewId);  // 调用这个方法， 第一个参数就是 方法调用者，第二个就是方法的参数
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 注入 View
     *
     * @param activity Context
     */
    private static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();   //获取所有的成员变量域
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class); //获取成员变量的注解
            if (viewInject == null) {  //如果没有则返回
                continue;
            }

            int viewId = viewInject.value();  //获取对应的 value 也就是这里的 R.id.btn_one 等
            if (viewId == -1) {   // 如果没有就返回
                continue;
            }
            try {
                Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class); //获取对应的 findViewById 的方法
                method.setAccessible(true);  // 屏蔽对应的 访问权限
                Object resView = method.invoke(activity, viewId);  //调用 findViewById ,第一个参数是调用者，第二个参数就是对应的 id
                field.set(activity, resView); //找到对应的 View 后还有对变量进行赋值。
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }


    private static void injectEvents(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods(); //找到所有的方法
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations(); // 找到方法的注解

            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType(); //找到注解的类型 即 onClick 类
                ListenerClass listenerClass = annotationType.getAnnotation(ListenerClass.class);  // 找到类型中的 ListenerClass 类型

                //没有返回继续找
                if (listenerClass == null) {
                    continue;
                }

                //获取 ListenerClass 的 类型的里面的值
                String listenerSetter = listenerClass.listenerSetter(); //setOnClickListener
                Class<?> listenerType = listenerClass.listenerType();  //View.OnClickListener.Class 这个就是匿名的内部类
                String methodName = listenerClass.methodName();  //  View.OnClickListener.Class 里面的 OnClick 方法

                try {
                    Method handleOnClickMethod = annotationType.getDeclaredMethod("value"); // 获取 OnClick 方法的里面 value 方法
                    int[] viewIds = (int[]) handleOnClickMethod.invoke(annotation, null); //这个方法就是存储所有的 id ,调用这个方法就可以获取所有的id
                    DynamicHandler dynamicHandler = new DynamicHandler(activity); // 动态代理
                    dynamicHandler.addMethod(methodName, method); //两个参数分别是  OnClick 和 handleOnClick 方法
                    Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                            new Class[]{listenerType}, dynamicHandler); //生成代理的对象， 这里就是 View.OnClickListener.Class 对象


                    for (int viewId : viewIds) {
                        View view = activity.findViewById(viewId);
                        Method setListenerMethod = view.getClass().getMethod(listenerSetter, View.OnClickListener.class);
                        setListenerMethod.invoke(view, listener); //这里执行的是 setOnClickListener 方法，参数就是代理对象 Listener
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}
