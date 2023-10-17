package com.example.plant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DrawActivity2 extends AppCompatActivity{


    private double percent_hh;
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private MyDrawView myDrawView;
    private Context context;
    private float height;

    private float startX, startY;
    private Button  re_draw2, store2;
    private static final String FOLDER_NAME = "PlantImages";
    private double target_hh;
    private TimeLineAdapter adapter;

    @SuppressLint({"MissingInflatedId", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);
        imageView = findViewById(R.id.imageView);
        context = this;


        percent_hh = getIntent().getDoubleExtra("percent", 0.0);

        Log.d("TAG !!!!!!!!", "DrawActivity2-percent：" + percent_hh);
        // Get the image path from the intent
        String imagePath = getIntent().getStringExtra("image_path");
        Log.d("TAG !!!!!!!!", "DrawActivity2-file path: " + imagePath);
        File file = new File(imagePath);
        String name = getIntent().getStringExtra("name");

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity2-", "name: " + name);
        int  position = getIntent().getIntExtra("position",0);

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity2-position", "position: " + position);
        // 创建 Bitmap 和 Canvas 对象
        // Create a mutable Bitmap object from the file
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

// Set the mutable bitmap as the ImageView's image

        imageView.setImageBitmap(mutableBitmap);

// Create a Canvas object using the mutable bitmap
        canvas = new Canvas(mutableBitmap);


        // 将MyDrawView 覆盖在 ImageView 上面
        myDrawView = new MyDrawView(this);
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        frameLayout.addView(myDrawView);
        myDrawView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        float left = Math.min(startX, x);
                        float top = Math.min(startY, y);
                        float right = Math.max(startX, x);
                        float bottom = Math.max(startY, y);
                        height = bottom - top;
                        myDrawView.path.reset();
                        myDrawView.path.addRect(left, top, right, bottom, Path.Direction.CW);
                        Log.d("TAG !!!!!!!!", "DrawActivity2-height: " + height);
                        target_hh = height / percent_hh;
                        Log.d("TAG DrawActivity2-target_hh", String.valueOf(target_hh));
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        return false;
                }
                myDrawView.invalidate();
                return true;
            }
        });
        store2 = findViewById(R.id.store2);
        re_draw2 = findViewById(R.id.re_draw2);
        store2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawView.setOnTouchListener(null);
                Intent intent = new Intent(DrawActivity2.this, ShowActivity.class);

                intent.putExtra("position",position);
                intent.putExtra("target",(double)Math.round(target_hh));
                Log.d("DrawActivity2-target", String.valueOf(Math.round(target_hh)));
                startActivity(intent);
            }
        });


        re_draw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                startX = event.getX();
                                startY = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                float x = event.getX();
                                float y = event.getY();
                                float left = Math.min(startX, x);
                                float top = Math.min(startY, y);
                                float right = Math.max(startX, x);
                                float bottom = Math.max(startY, y);
                                //!!!!!!!!!
                                height = bottom - top;
                                myDrawView.path.reset();
                                myDrawView.path.addRect(left, top, right, bottom, Path.Direction.CW);
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            default:
                                return false;
                        }
                        myDrawView.invalidate();
                        return true;
                    }
                });
                target_hh = height/percent_hh;
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String newHeight = String.valueOf(target_hh); // 新的植物高度

                String tableName = "my_library";
                String columnName = "plant_target";
                String whereClause = "name = ?";
                String[] whereArgs = { name };

                ContentValues values = new ContentValues();
                values.put(columnName, newHeight);
                int rowsAffected = db.update(tableName, values, whereClause, whereArgs);

                if (rowsAffected > 0) {
                    // 更新成功
                    Log.d("TAG! DrawActivity2-数据库", "更新成功");
                } else {
                    // 更新失败或没有匹配的数据行
                    Log.d("TAG! DrawActivity2-数据库", "更新失败或没有匹配的数据行");
                }
                db.close();



            }
        });
    }

}