package com.example.asus.viewinject.reflect.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.viewinject.reflect.ContentView;
import com.example.asus.viewinject.reflect.OnClick;
import com.example.asus.viewinject.R;
import com.example.asus.viewinject.reflect.ViewInject;
import com.example.asus.viewinject.reflect.ViewInjectUtils;




@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_one)
    Button mButtonOne;
    @ViewInject(R.id.btn_two)
    Button mButtonTwo;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjectUtils.bind(this);


    }

    @OnClick(R.id.btn_one)
    public void handleOnClick(View view) {
        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

    }


}
