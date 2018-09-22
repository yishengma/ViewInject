package com.example.asus.viewinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PirateHat on 18-9-22.
 */
@Target(ElementType.TYPE)   //标识注解能修饰的类型
@Retention(RetentionPolicy.RUNTIME)  // 注解保存到运行时
public @interface ContentView {
    int value();
}

