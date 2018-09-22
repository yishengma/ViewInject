package com.example.asus.viewinject.compile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.viewinject.R;
import com.example.ioc.ViewBinder;
import com.example.ioc_annotation.BindView;

import java.util.Map;

public class CompileActivity extends AppCompatActivity {
    @BindView(R.id.btn_one)
    Button mButtonOne;
    @BindView(R.id.btn_two)
    Button mButtonTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile);
        ViewBinder.bind(this);
        mButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompileActivity.this,"",Toast.LENGTH_SHORT).show();
            }
        });
        mButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompileActivity.this,"",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
