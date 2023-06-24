package com.example.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddArchiveActivity extends AppCompatActivity {

    EditText name_input, var_input, height_input;
    TextView text_add_pic;
    Button btn_add;

    private final int CAMERA_REQ_CODE = 100;
    private Uri uri_up;
    ImageView imgCamera;
    // Define constant variable for folder name
    private static final String FOLDER_NAME = "PlantImages";
    private Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name_input = findViewById(R.id.name_input);
        var_input = findViewById(R.id.var_input);
        height_input = findViewById(R.id.height_input);
        btn_add = findViewById(R.id.btn_add);
        imgCamera = findViewById(R.id.imageView_add);
        text_add_pic = findViewById(R.id.text_add_pic);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddArchiveActivity.this);
                try {
                    myDB.addPlant(name_input.getText().toString().trim(), var_input.getText().toString().trim(), Integer.parseInt(height_input.getText().toString().trim()), uri_up);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(AddArchiveActivity.this, ArchiveFragment.class);
                startActivity(intent);

            }
        });

    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                //camera
                if (data != null) {
                    Bitmap img = (Bitmap) data.getExtras().get("data");
                    imgCamera.setImageBitmap(img);

                    // Save the image to the device's MediaStore
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File folder = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String fileName = timeStamp + ".jpg";
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
                            uri_up = uri;

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}