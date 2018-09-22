package com.example.ioc;

/**
 * Created by PirateHat on 18-9-22.
 */

public interface ViewInject<T> {
    void bind(T t,Object source);
}
