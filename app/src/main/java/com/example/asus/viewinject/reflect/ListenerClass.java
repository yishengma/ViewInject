package com.example.asus.viewinject.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PirateHat on 18-9-22.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerClass {
    Class<?> listenerType();

    String listenerSetter();

    String methodName();


}
