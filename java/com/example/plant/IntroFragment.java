package com.example.plant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class IntroFragment extends Fragment {
    private int layoutResource;
    private ViewPager2 viewPager;
    private int[] images; // 存储当前tab的图片资源

    public IntroFragment(int layoutResource, int[] images) {
        this.layoutResource = layoutResource;
        this.images = images;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResource, container, false);


        if (layoutResource == R.layout.fragment_intro1) {
            viewPager = view.findViewById(R.id.view_pager3);

            ImageAdapter imageAdapter = new ImageAdapter(images); // 创建适配器并传入当前tab的图片资源
            viewPager.setAdapter(imageAdapter);
            // 设置上下滑动的图片适配器和逻辑
        } else if (layoutResource == R.layout.fragment_intro2) {
            viewPager = view.findViewById(R.id.view_pager4);

            ImageAdapter imageAdapter = new ImageAdapter(images); // 创建适配器并传入当前tab的图片资源
            viewPager.setAdapter(imageAdapter);
            // 设置上下滑动的图片适配器和逻辑
        }


        // 设置页面切换的滑动手势
        viewPager.setUserInputEnabled(true);

        // 设置滑动方向为上下滑动
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);



        // 添加上下滑动的手势监听器
        view.setOnTouchListener(new View.OnTouchListener() {
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endY = event.getY();
                        if (endY - startY > 0) {
                            // 向下滑动
                            handleScrollDown();
                        } else {
                            // 向上滑动
                            handleScrollUp();
                        }
                        break;
                }
                return true;
            }
        });

        return view;
    }

    private void handleScrollUp() {
        // 向上滑动的逻辑处理
        // 例如切换到下一个图片
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < images.length - 1) {
            viewPager.setCurrentItem(currentItem + 1);
        }
    }

    private void handleScrollDown() {
        // 向下滑动的逻辑处理
        // 例如切换到上一个图片
        int currentItem = viewPager.getCurrentItem();
        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1);
        }
    }
}
