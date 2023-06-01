package com.example.plant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PrivacyPolicyFragment extends Fragment {
    private ScrollView scrollView;
    private TextView privacyTitle;
    private TextView privacyText1;
    private TextView privacyText2;
    private TextView privacyText3;
    private TextView privacyText4;
    private TextView privacyText5;
    private TextView privacyText6;
    private TextView privacyText7;
    private TextView privacyText8;
    private TextView privacyText9;
    private TextView privacyText10;
    private TextView privacyText11;
    private TextView privacyText12;
    private TextView privacyText13;
    private TextView privacyText14;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        scrollView = view.findViewById(R.id.privacy_hh);
        privacyTitle = view.findViewById(R.id.privacy_title);
        privacyText1 = view.findViewById(R.id.privacy_text1);
        privacyText2 = view.findViewById(R.id.privacy_text2);
        privacyText3 = view.findViewById(R.id.privacy_text3);
        privacyText4 = view.findViewById(R.id.privacy_text4);
        privacyText5 = view.findViewById(R.id.privacy_text5);
        privacyText6 = view.findViewById(R.id.privacy_text6);
        privacyText7 = view.findViewById(R.id.privacy_text7);
        privacyText8 = view.findViewById(R.id.privacy_text8);
        privacyText9 = view.findViewById(R.id.privacy_text9);
        privacyText10 = view.findViewById(R.id.privacy_text10);
        privacyText11 = view.findViewById(R.id.privacy_text11);
        privacyText12= view.findViewById(R.id.privacy_text12);
        privacyText13 = view.findViewById(R.id.privacy_text13);
        privacyText14 = view.findViewById(R.id.privacy_text14);

        return view;
    }

}
