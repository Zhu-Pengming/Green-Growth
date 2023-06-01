package com.example.plant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    ImageView imageView_update;
    private final int CAMERA_REQ_CODE = 200;
    private Uri uri3;
    Button btn_re_draw,btn_draw;
    // Define constant variable for folder name
    private static final String FOLDER_NAME = "PlantImages";
    @SuppressLint({"MissingInflatedId", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        String name = getIntent().getStringExtra("name");

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity1-name", "name: " + name);

        int  position = Integer.parseInt(getIntent().getStringExtra("position"));

        Log.d("TAG !!!!!!!!!!!!!!!DrawActivity1-position", "position: " + position);

        imageView_update = findViewById(R.id.imageView_update);
        imageView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        });
        btn_re_draw = findViewById(R.id.btn_re_draw);
        btn_draw = findViewById(R.id.btn_draw);
        btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the file path from the MediaStore URI
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri3, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    cursor.close();

                    // Pass the file path to the DrawActivity1
                    Intent intent = new Intent(UpdateActivity.this, DrawActivity1.class);
                    intent.putExtra("FilePath", filePath);
                    intent.putExtra("name",name);
                    intent.putExtra("position",position);
                    Log.d("TAG", "file path: " + filePath);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                //camera
                if (data != null) {
                    Bitmap img = (Bitmap) data.getExtras().get("data");
                    imageView_update.setImageBitmap(img);

                    // Save the image to the device's MediaStore
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File folder = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String fileName = timeStamp + ".jpg";
                    File file = new File(folder, fileName);

                    //Log.d("TAG", "file path: " + file.getAbsolutePath());
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + FOLDER_NAME);
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    try {
                        OutputStream outputStream = getContentResolver().openOutputStream(uri);
                        if (outputStream != null) {
                            // Compress and save the image to the OutputStream
                            img.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                            outputStream.close();
                            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                            uri3 = uri;

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }else if (requestCode == 2) {
                // 处理重新处理数据的返回结果
                        if (data != null && data.hasExtra("processedData") && data.hasExtra("position")) {
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
            }

}