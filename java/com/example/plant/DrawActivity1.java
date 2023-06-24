package com.example.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;


public class DrawActivity1 extends AppCompatActivity {
    private Bitmap bitmap;
    private Canvas canvas;
    private MyDrawView myDrawView;
    private Context context;
    private Uri uri1;
    private ImageView imageView;
    private double height;

    private float startX, startY;
    private static final String FOLDER_NAME = "PlantImages";

    private Button store1,re_draw1;
    private static final String TAG = "便签";


    @SuppressLint({"WrongThread", "WrongViewCast", "MissingInflatedId", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw1);



        String filePath = getIntent().getStringExtra("FilePath");

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity1-", "file path: " + filePath);


        String name = getIntent().getStringExtra("name");

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity1-", "name: " + name);

        int  position = getIntent().getIntExtra("position",0);

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity2-position", "position: " + position);

        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                // Create a mutable Bitmap object from the file
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Bitmap mutableBitmap = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mutableBitmap = bitmap.copy(Bitmap.Config.RGBA_F16, true);
                }

                // Set the mutable bitmap as the ImageView's image
                imageView = findViewById(R.id.draw_1);
                imageView.setImageBitmap(mutableBitmap);

                // Create a Canvas object using the mutable bitmap
                canvas = new Canvas(mutableBitmap);
            } else {
                // File cannot be read
                Log.e(TAG, "File cannot be read");
            }
        } else {
            // File path is null
            Log.e(TAG, "File path is null");
        }


        // 将MyDrawView 覆盖在 ImageView 上面
        myDrawView = new MyDrawView(this);
        FrameLayout frameLayout = findViewById(R.id.draw_frame_layout);
        frameLayout.addView(myDrawView);


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
                        height = bottom - top;
                        myDrawView.path.reset();
                        myDrawView.path.addRect(left, top, right, bottom, Path.Direction.CW);
                        Log.d("TAG DrawActivity1-", "height: " + height);
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


        store1 = findViewById(R.id.store1);
        re_draw1 = findViewById(R.id.re_draw1);

        store1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawView.setOnTouchListener(null);


                Intent intent = new Intent(DrawActivity1.this, EnterActivity.class);
                intent.putExtra("height", height);
                intent.putExtra("name", name);
                intent.putExtra("image_path", filePath);
                startActivity(intent);
            }
        });
        re_draw1.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(DrawActivity1.this, EnterActivity.class);
                // 将高度作为额外参数添加到Intent中
                intent.putExtra("height", height);
                intent.putExtra("image_path", uri1.toString());
                intent.putExtra("pposition",position);
                startActivity(intent);
            }
        });
    }




}
