package com.example.plant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class IntroActivity extends AppCompatActivity {
    private Button video;
    private TextView introduce;
    private Button skip;
    private TabLayout tabLayout;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_intro);

        video = findViewById(R.id.video);
        introduce = findViewById(R.id.text_introduce);
        skip = findViewById(R.id.skip);
        // Set up the TabLayout
        tabLayout = findViewById(R.id.tab_layout);

        // 创建PagerAdapter实例
        IntroPagerAdapter pagerAdapter = new IntroPagerAdapter(getSupportFragmentManager(), getLifecycle());
        // Create and add tabs to the TabLayout
        TabLayout.Tab tab1 = tabLayout.newTab().setText("Novice guide");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setText("Team introduction");
        tabLayout.addTab(tab2);



        int[] images1 = { R.drawable.ic_image1, R.drawable.ic_image2, R.drawable.ic_image3, R.drawable.ic_image4, R.drawable.ic_image5,R.drawable.ic_image6, R.drawable.ic_image7, R.drawable.ic_image8, R.drawable.ic_image9, R.drawable.ic_image10, R.drawable.ic_image11, R.drawable.ic_image12};
        int[] images2 = {R.drawable.image1};

// 创建IntroFragment实例，并添加到PagerAdapter中
        IntroFragment fragment1 = new IntroFragment(R.layout.fragment_intro1,images1);
        pagerAdapter.addFragment(fragment1);

        IntroFragment fragment2 = new IntroFragment(R.layout.fragment_intro2,images2);
        pagerAdapter.addFragment(fragment2);

        ViewPager2 viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(pagerAdapter);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Novice guide");
                    } else if (position == 1) {
                        tab.setText("Team introduction");
                    }
                }
        ).attach();



        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a webpage when the webImageView is clicked
                String url = "https://www.bilibili.com/";  // Replace with your desired URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("IntroActivity", "Skip button clicked");
                finishIntroActivity();
            }
        });

    }
    private void finishIntroActivity() {
        SharedPreferences preferences = getSharedPreferences("APD Plant Growth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("introShown", true);
        editor.apply();

        finish();
    }

}