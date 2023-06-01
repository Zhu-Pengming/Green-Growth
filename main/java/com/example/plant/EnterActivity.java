package com.example.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EnterActivity extends AppCompatActivity {
    private double pot_height;
    private EditText input_enter;
    private TextView text_enter;
    private Button go_jisuan;
    private double percent;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        pot_height = getIntent().getDoubleExtra("height", 0.0f);
        Log.d("TAG ！!!!EnterActivity", String.valueOf(pot_height));
        // Get the image path from the intent
        String imagePath = getIntent().getStringExtra("image_path");


        String name = getIntent().getStringExtra("name");

        Log.d("TAG !!!!!!!!!!!!!!!EnterActivity-", "name: " + name);

        int  position = Integer.parseInt(getIntent().getStringExtra("position"));

        Log.d("TAG !!!!!!!!!!!!!!!-position", "position: " + position);

        input_enter = findViewById(R.id.input_enter);
        text_enter = findViewById(R.id.text_enter);
        go_jisuan = findViewById(R.id.go_jisuan);
        input_enter = findViewById(R.id.input_enter);
        input_enter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputString = s.toString().trim();
                if (inputString.isEmpty()) {
                    // 处理空输入的情况
                    Log.d("TAG ", "EnterActivity " + pot_height);
                } else {
                    double inputText = Double.parseDouble(inputString);
                    percent = pot_height / inputText;
                    Log.d("TAG!!!EnterActivity-percent", String.valueOf(percent));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });


        go_jisuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, DrawActivity2.class);
                // 将高度作为额外参数添加到Intent中
                intent.putExtra("percent", percent);
                intent.putExtra("image_path", imagePath);
                intent.putExtra("name",name);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            double processedData = data.getDoubleExtra("processedData", 0.0);
            int position = data.getIntExtra("position", 0);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("processedData", processedData);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}