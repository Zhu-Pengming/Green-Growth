package com.example.plant;

import static java.lang.System.loadLibrary;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;



class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "PlantLibrary.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "plant_id";
    private static final String COLUMN_NAME = "plant_name";
    private static final String COLUMN_VAR = "plant_var";
    private static final String COLUMN_HEIGHT = "plant_height";
    private static final String COLUMN_TARGET = "plant_target";
    private static final String COLUMN_TIME = "plant_time";
    private static final String TAG = "便签";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME + " TEXT, "+
                COLUMN_VAR + " TEXT, "+
                COLUMN_HEIGHT + " INTEGER, "+
                COLUMN_TIME + " TEXT, "+
                COLUMN_TARGET + " DOUBLE "+")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_TARGET + " DOUBLE DEFAULT 0.0");
        }
    }

    void addPlant(String name, String var, int height, Uri pic_uri,String time) throws URISyntaxException, IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues hhh = new ContentValues();

        hhh.put(COLUMN_NAME, name);
        hhh.put(COLUMN_VAR, var);
        hhh.put(COLUMN_HEIGHT, height);
        hhh.put(COLUMN_TIME, time);

        String path = getFile(pic_uri);
        Log.d(TAG, path);

        // Check if the file can be read
        File file = new File(path);
        if (file.canRead()) {
            // File can be read, continue with processing
            double plantHeight = plantDetector(context, pic_uri);
            double basinHeight = basinDetector(context ,pic_uri);
            double Height = plantHeight / basinHeight * height;
            hhh.put(COLUMN_TARGET, Height);
        } else {
            // File cannot be read
            Log.e(TAG, "File cannot be read");
        }

        long result = db.insert(TABLE_NAME, null, hhh);
        if (result == -1) {
            Toast.makeText(context, " Fail1", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, " Success1 ", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFile(Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }


    public void updateData(String row_id, String name, String var,double height,Uri pic_uri) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues hh = new ContentValues();

        hh.put(COLUMN_ID,row_id);
        hh.put(COLUMN_NAME,name);
        hh.put(COLUMN_VAR,var);
        hh.put(COLUMN_HEIGHT,height);

        //Uri pic_uri = UpdateActivity.uri2;
        //process
        double plantHeight = plantDetector(context,pic_uri);
        double basinHeight = basinDetector(context,pic_uri);
        double Height = plantHeight / basinHeight * height;
        hh.put(COLUMN_TARGET,Height);

        long result = db.update(TABLE_NAME,hh,"_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context," Fail2 ",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context," Success2 ",Toast.LENGTH_SHORT).show();
        }


    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context," Fail ",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context," Success ",Toast.LENGTH_SHORT).show();
        }
    }
    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE FROM " + TABLE_NAME);
    }

    /***
     * 植物的高度检测器
     * @param pic_uri - String 图片的路径
     * @return res - int 的高度输出
     */
    private static int plantDetector(Context context, Uri pic_uri) throws IOException {
        //导入
//        loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //创建检测器
        //Stream for cascade
        InputStream is = context.getAssets().open("cascade_plant.xml");
        File cDir = context.getDir("cascade",Context.MODE_PRIVATE);
        File cFile = new File(cDir, "cascade_plant.xml");
        FileOutputStream os = new FileOutputStream(cFile);

        byte[] buffer = new byte[4096];
        int bytesRead; while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        CascadeClassifier detector = new CascadeClassifier(cFile.getAbsolutePath());
        //stream close
        is.close();
        os.close();

        //Stream for image
        InputStream inputStream = context.getContentResolver().openInputStream(pic_uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        //---------------------test-------------------------
        if (image.empty()) {
            throw new IllegalArgumentException("失败！");
        }


        MatOfRect plantDetections = new MatOfRect();
        detector.detectMultiScale(image,plantDetections);
        //return接收
        int res = 1;
        //绘制矩形
        for(Rect rect:plantDetections.toArray()){
            Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,255),2);
            res = rect.height;//获取高度
        }
        //stream close
        inputStream.close();
        //osI.close();
        return res;
    }


    /***
     * 盆的高度检测器
     * @param  pic_uri - String 图片的路径
     * @return res - int 的高度输出
     */
    private static int basinDetector(Context context,Uri pic_uri) throws IOException {
        //导入
//        loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //创建检测器
        //Stream for cascade
        InputStream is = context.getAssets().open("cascade_basin.xml");
        File cDir = context.getDir("cascade",Context.MODE_PRIVATE);
        File cFile = new File(cDir, "cascade_basin.xml");
        FileOutputStream os = new FileOutputStream(cFile);

        byte[] buffer = new byte[4096];
        int bytesRead; while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        CascadeClassifier Detector = new CascadeClassifier(cFile.getAbsolutePath());
        //stream close
        is.close();
        os.close();

        //Stream for image

        InputStream isI = context.getContentResolver().openInputStream(pic_uri);
        //stream close
        Bitmap bitmap = BitmapFactory.decodeStream(isI);
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        isI.close();

        MatOfRect plantDetections = new MatOfRect();
        Detector.detectMultiScale(image, plantDetections);
        //return接收
        int res = 1;
        //绘制矩形
        for (Rect rect : plantDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 255), 2);
            res = rect.height;//获取高度
        }
        return res;
    }

}