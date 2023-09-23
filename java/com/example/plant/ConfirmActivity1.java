package com.example.plant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ConfirmActivity1 extends AppCompatActivity {

    private Bitmap bitmap;

    private ImageView imageView;

    private Canvas canvas;

    private Uri uri;
    private static final String TAG = "ConfirmActivity1-";

    private Button back,next;
    private Bitmap resultBitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm1);

        uri = Uri.parse(getIntent().getStringExtra("uri"));
        Log.d("TAG !!!!!ConfirmActivity1-", "uri: " + uri);

        String name = getIntent().getStringExtra("name");
        Log.d("TAG !!!!!ConfirmActivity1-", "name: " + name);

        String var = getIntent().getStringExtra("var");
        Log.d("TAG !!!!!ConfirmActivity1-", "var " + var);

        int pot_height = getIntent().getIntExtra("pot_height",0);
        Log.d("TAG !!!!!ConfirmActivity1-", "pot_height: " + pot_height);


        imageView = findViewById(R.id.confirm1_image);
        back = findViewById(R.id.confirm1_back);
        next = findViewById(R.id.confirm1_next);


        // Get the file path from the MediaStore URI
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();

            // 此时 filePath 包含了正确的文件路径
            Log.d("File Path", "File path: " + filePath);


            if (uri != null) {
                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


                        try {
                            // 检测盆
                            int[] dataArray = MyDatabaseHelper.basinDetectorConfirm(this, uri);

                            if (dataArray[0] == 0 && dataArray[1] == 0 && dataArray[2] == 0 && dataArray[3] == 0) {
                                Toast.makeText(this, "Failed to detect basin.", Toast.LENGTH_LONG).show();
                                // 在这里添加适当的处理逻辑，例如显示错误消息或采取其他措施。
                            } else {
                                // 绘制矩形并显示
                                resultBitmap = drawingRectangle(dataArray, bitmap);
                                imageView.setImageBitmap(resultBitmap);
                            }

                            // 检测盆
                            int[] dataArray1 = MyDatabaseHelper.FlowerDetectorConfirm(this, uri);

                            if (dataArray1[0] == 0 && dataArray1[1] == 0 && dataArray1[2] == 0 && dataArray1[3] == 0) {
                                Toast.makeText(this, "Failed to detect Failed to detect flower.", Toast.LENGTH_LONG).show();
                                // 在这里添加适当的处理逻辑，例如显示错误消息或采取其他措施。
                            } else {
                                // 绘制矩形并显示
                                Bitmap flowerBitmap = drawingRectangle(dataArray, resultBitmap);

                                // 在原始图像上绘制花的图层
                                Bitmap resultBitmap1 = Bitmap.createBitmap(flowerBitmap.getWidth(), flowerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas1 = new Canvas(resultBitmap1);
                                canvas1.drawBitmap(bitmap, 0, 0, null); // 将原始图像绘制到画布上
                                canvas1.drawBitmap(flowerBitmap, 0, 0, null); // 叠加花的图层

                                imageView.setImageBitmap(resultBitmap1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            // 处理异常
                        }
                    } else {
                            // File does not exist
                            Log.e(TAG, "File does not exist");
                        }
                    } else {
                        // File path is null
                        Log.e(TAG, "File path is null");
                    }
                } else {
                    // URI is null
                    Log.e(TAG, "URI is null");
                }

            }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity1.this, AddArchiveActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmActivity1.this);
                builder.setMessage(R.string.dialog_message) // Replace "R.string.dialog_message" with your desired message
                        .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Handle the positive button click here

                                // 处理花盆数据
                                MyDatabaseHelper myDB = new MyDatabaseHelper(ConfirmActivity1.this);
                                try {
                                    myDB.addPlant(name, var, pot_height, uri);
                                } catch (IOException | URISyntaxException e) {
                                    e.printStackTrace();
                                }


                                Intent intent = new Intent(ConfirmActivity1.this, MainActivity.class);
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton(R.string.Confirm_not, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Handle the negative button click here
                                // 处理花盆数据
                                MyDatabaseHelper myDB = new MyDatabaseHelper(ConfirmActivity1.this);
                                try {
                                    myDB.addPlant(name, var, pot_height, uri);

                                    int plantId = myDB.getPlantIdByName(name);

                                    Intent intent = new Intent(ConfirmActivity1.this, UpdateActivity.class);
                                    intent.putExtra("position", plantId);
                                    Log.d(TAG,"The position is "+plantId);
                                    intent.putExtra("name", String.valueOf(name));

                                    startActivity(intent);
                                } catch (IOException | URISyntaxException e) {
                                    e.printStackTrace();
                                }


                            }
                        });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public static Bitmap drawingRectangle(int[] dataArray, Bitmap bitmap) {
        double height = dataArray[0];
        double width = dataArray[1];
        double x = dataArray[2];
        double y = dataArray[3];

        Log.d(TAG,"The value of x is " +x);
        Log.d(TAG,"The value of y is " +y);
        Log.d(TAG,"The value of height is " +height);
        Log.d(TAG,"The value of width is " +width);

        // Create a copy of the original bitmap to draw on
        Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), true);

        // Create a canvas associated with the resultBitmap
        Canvas canvas = new Canvas(resultBitmap);

        // Draw the rectangle on the canvas
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3); // Set the rectangle's line width
        canvas.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), paint);

        return resultBitmap;
    }


}