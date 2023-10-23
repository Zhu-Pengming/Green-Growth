package com.example.plant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction; // 导入正确的 FragmentTransaction 类
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {


    private Fragment archiveFragment = null;// 用于显示存档界面
    private Fragment aboutUsFragment = null;// 用于显示关于我们界面
    private Fragment privacyPolicyFragment = null;// 用于显示隐私条款界面

    private View archiveLayout = null;// 存档显示布局
    private View aboutUsLayout = null;// 关于我们显示布局
    private View privacyPolicyLayout = null;// 隐私条款显示布局

    /*声明组件变量*/
    private ImageView archiveImg = null;
    private ImageView aboutUsImg = null;
    private ImageView privacyPolicyImg = null;

    private TextView archiveText = null;
    private TextView aboutUsText = null;
    private TextView privacyPolicyText = null;

    private AlertDialog dialog;

    private boolean introActivityShown = false;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final String TAG = "hhhhhh";
    private boolean isActivityDestroyed = false;


    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV initialization failed");
        } else {
            Log.i(TAG, "OpenCV initialization succeeded");
        }
    }


    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivacyCheck();
        checkIntroShown();

        // Check if the app has permission to access the camera and write to external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }


        requestWindowFeature(Window.FEATURE_NO_TITLE); // 在setContentView之前调用

        setContentView(R.layout.activity_main);


        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);


    }

    /**
     * 在这里面获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件
     */
    @SuppressLint({"NewApi", "ResourceType"})
    public void initViews() {
        fragmentManager = getFragmentManager(); // 使用正确的 FragmentManager
        archiveLayout = findViewById(R.id.archive_layout);
        aboutUsLayout = findViewById(R.id.aboutus_layout);
        privacyPolicyLayout = findViewById(R.id.privacy_layout);

        archiveImg = findViewById(R.drawable.ic_archive);
        aboutUsImg = findViewById(R.drawable.ic_about_us);
        privacyPolicyImg = findViewById(R.drawable.ic_privacy);

        archiveText = findViewById(R.id.archive_text);
        aboutUsText = findViewById(R.id.aboutus_text);
        privacyPolicyText = findViewById(R.id.privacy_text);


        // 处理点击事件
        archiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0); // 当点击了archive时，选中第1个tab
            }
        });


        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1); // 当点击了about_us时，选中第3个tab
            }
        });

        privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(2); // 当点击了privacy时，选中第4个tab
            }
        });




    }
    /**
     * 根据传入的index参数来设置选中的tab页 每个tab页对应的下标。
     */
    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 首先隐藏所有的Fragment
        hideFragments(transaction);

        switch (index) {
            case 0:
                aboutUsText.setTextColor(Color.parseColor("#B3EE3A"));
                if (aboutUsFragment == null) {
                    aboutUsFragment = new AboutUsFragment();
                    transaction.replace(R.id.container_about_us, aboutUsFragment);
                } else {
                    transaction.show(aboutUsFragment);
                }
                break;
            case 1:
                archiveText.setTextColor(Color.parseColor("#B3EE3A"));
                if (archiveFragment == null) {
                    archiveFragment = new ArchiveFragment();
                    transaction.add(R.id.container_archive, archiveFragment); // 添加archiveFragment到事务中
                } else {
                    transaction.show(archiveFragment);
                }
                break;
            case 2:
                privacyPolicyText.setTextColor(Color.parseColor("#B3EE3A"));
                if (privacyPolicyFragment == null) {
                    privacyPolicyFragment = new PrivacyPolicyFragment();
                    transaction.replace(R.id.container_privacy_policy, privacyPolicyFragment);
                } else {
                    transaction.show(privacyPolicyFragment);
                }
                break;
        }

        transaction.commit();
    }


    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {

        archiveText.setTextColor(Color.parseColor("#82858b"));
        //?
        aboutUsText.setTextColor(Color.parseColor("#82858b"));

        privacyPolicyText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (aboutUsFragment != null) {
            transaction.hide(aboutUsFragment);
        }
        if (archiveFragment != null) {
            transaction.hide(archiveFragment);
        }
        if (privacyPolicyFragment != null) {
            transaction.hide(privacyPolicyFragment);
        }
    }
    public void showPrivacy(String privacyFileName){
        String str = initAssets(privacyFileName);
        final View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_privacy, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv_title = (TextView) inflate.findViewById(R.id.dialog_title);
        tv_title.setText("Privacy policy authorization prompts");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv_content = (TextView) inflate.findViewById(R.id.privacy_content);
        tv_content.setText(str);
        dialog = new AlertDialog
                .Builder(MainActivity.this)
                .setView(inflate)
                .show();
        // 通过WindowManager获取
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = dm.widthPixels*4/5;
        params.height = dm.heightPixels*1/2;
        dialog.setCancelable(false);//屏蔽返回键
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        dismissPrivacyDialog(); // Dismiss the dialog when the activity is paused
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPrivacyDialog(); // Dismiss the dialog when the activity is destroyed
    }

    private void dismissPrivacyDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public void onClickDisagree(View v) {
        finish(); // Close the activity
    }


    public void onClickAgree(View v) {
        dismissPrivacyDialog();
        //下面将已阅读标志写入文件，再次启动的时候判断是否显示。
        SharedPreferences sharedPreferences = this.getSharedPreferences("file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("AGREE", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }



    //封装一个AlertDialog
    private void exitDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Friendly reminder")
                .setMessage("Are you sure you want to exit the program?")
                .setPositiveButton("Close the app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create();
        dialog.show();//显示对话框
    }

    /**
     * 返回菜单键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果是返回按钮
            exitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 从assets下的txt文件中读取数据
     */
    public String initAssets(String fileName) {
        String str = null;
        try {
            InputStream inputStream = getAssets().open(fileName);

            str = getString(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return str;
    }
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public void PrivacyCheck() {
        boolean status = this.getSharedPreferences("file", Context.MODE_PRIVATE)
                .getBoolean("AGREE", false);
        if (!status) {
            dismissPrivacyDialog(); // Dismiss any existing dialog
            showPrivacy("privacy.txt"); // Show the privacy dialog
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if IntroActivity has been shown
        if (introActivityShown) {
            introActivityShown = false; // Reset the flag
        }
    }

    private void showIntroActivity() {
        startActivity(new Intent(MainActivity.this, IntroActivity.class));
        introActivityShown = true; // Set the flag to indicate IntroActivity has been shown
    }
    private void checkIntroShown() {
        SharedPreferences preferences = getSharedPreferences("APD Plant Growth", Context.MODE_PRIVATE);
        boolean introShown = preferences.getBoolean("introShown", false);
        if (!introShown && !introActivityShown) {
            // Show IntroActivity only if it hasn't been shown before and introActivityShown is false
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("introShown", true);
            editor.apply();

            showIntroActivity();
        }
    }
}
