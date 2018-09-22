package com.example.asus.viewinject.reflect;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PirateHat on 18-9-22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ListenerClass(listenerType = View.OnClickListener.class,listenerSetter = "setOnClickListener",methodName = "onClick")
public @interface OnClick {
    int[] value();

}
