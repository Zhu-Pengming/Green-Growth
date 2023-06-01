package com.example.plant;

import static androidx.core.app.ActivityCompat.recreate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

public class AboutUsFragment extends Fragment {

    private TextView aboutTitle;
    private ImageView imageViewAbout;
    private TextView version;
    private CardView card1;
    private TextView textDevelop;
    private TextView androidDeveloper;
    private TextView openDeveloper1;
    private TextView openDeveloper2;
    private TextView androidDeveloper2;
    private CardView card2;
    private TextView contactsTitle;
    private Switch languageSwitch;
    private FragmentManager fragmentManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // 关联XML布局中的视图
        aboutTitle = view.findViewById(R.id.about_title);
        imageViewAbout = view.findViewById(R.id.imageView_about);
        version = view.findViewById(R.id.version);
        card1 = view.findViewById(R.id.card_1);
        textDevelop = view.findViewById(R.id.text_develop);
        androidDeveloper = view.findViewById(R.id.android_developer);
        openDeveloper1 = view.findViewById(R.id.open_developer1);
        openDeveloper2 = view.findViewById(R.id.open_developer2);
        androidDeveloper2 = view.findViewById(R.id.android_developer2);
        card2 = view.findViewById(R.id.card_2);
        contactsTitle = view.findViewById(R.id.contacts_title);

        // 在这里可以对视图进行进一步的操作和设置

        ImageView webImageView = view.findViewById(R.id.img_web);
        ImageView emailImageView = view.findViewById(R.id.img_email);
        ImageView facebookImageView = view.findViewById(R.id.img_facebook);

        webImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a webpage when the webImageView is clicked
                String url = "https://samgan@apdskeg.com";  // Replace with your desired URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipientEmail = "zhup@kean.edu";
                String subject = "Hello";
                String body = "This is the body of the email";

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + recipientEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a webpage when the webImageView is clicked
                String url = "https://www.facebook.com/APDLab.com";  // Replace with your desired URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        aboutUsFragment.setFragmentManager(getChildFragmentManager());

        LanguageChangeReceiver receiver = new LanguageChangeReceiver();
        receiver.setFragmentManager(getFragmentManager());
        IntentFilter filter = new IntentFilter("ACTION_LANGUAGE_CHANGE");
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter);




        languageSwitch = view.findViewById(R.id.bt_language);
// 默认英文
        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent("ACTION_LANGUAGE_CHANGE");
                intent.putExtra("isEnglish", !isChecked);
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
            }
        });





        return view;
    }

    // 刷新界面或更新界面元素的方法
    public void refreshUI() {
        // 在此方法中执行刷新界面的逻辑
        // 例如，重新设置文本、更新图片等
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

}

