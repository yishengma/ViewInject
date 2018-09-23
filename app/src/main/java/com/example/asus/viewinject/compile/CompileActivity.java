package com.example.asus.viewinject.compile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.viewinject.R;
import com.example.ioc.ViewBinder;
import com.example.ioc_annotation.BindView;

/**
 *  使用的是编译时注解的方式，使用的是  @Retention(RetentionPolicy.CLASS) 策略
 *  依赖的库有 ioc,ioc-annotation ,ioc-compile
 *  对应的 注解处理器就是 AnnotationProcessor ，注意这个类不能添加注释，否则就会报错
 *  还有就是使用 javaPoet 生成对应的文件的时候空格等格式都要与普通的 Java 文件的格式一样。
 */


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
