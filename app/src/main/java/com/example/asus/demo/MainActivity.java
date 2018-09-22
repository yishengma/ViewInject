package com.example.asus.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.asus.viewinject.ContentView;
import com.example.asus.viewinject.R;
import com.example.asus.viewinject.ViewInject;
import com.example.asus.viewinject.ViewInjectUtils;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_one)
    Button mButtonOne;
    @ViewInject(R.id.btn_two)
    Button mButtonTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjectUtils.bind(this);

    }


}
