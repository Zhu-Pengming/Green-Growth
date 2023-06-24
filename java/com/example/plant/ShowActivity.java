package com.example.plant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

    private TextView show_text,target_text;
    private Button next;
    private int position;
    private double target;

    @SuppressLint({"LongLogTag", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        position = getIntent().getIntExtra("position",0);
        Log.d("ShowActivity-position", "position: " + position);


        target = getIntent().getDoubleExtra("target", 0);


        Log.d("ShowActivity", "target: " + target);

        show_text = findViewById(R.id.show_text);
        target_text = findViewById(R.id.target_text);
        target_text.setText(String.valueOf(target));

        next = findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 获取ArchiveFragment实例
                        ArchiveFragment archiveFragment = ArchiveFragment.getInstance(position, target);

                        // 获取FragmentManager并开始Fragment事务
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        // 将ArchiveFragment添加到容器中
                        fragmentTransaction.replace(R.id.archive_fragment_container, archiveFragment);
                        fragmentTransaction.commit();
                    }
                }, 500); // 延迟500毫秒后执行
                // Make show_text and target_text transparent
                show_text.setAlpha(0.0f);
                target_text.setAlpha(0.0f);
                next.setAlpha(0.0f);
            }
        });

    }
}