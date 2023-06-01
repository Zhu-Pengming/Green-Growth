package com.example.plant;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

public class LanguageChangeReceiver extends BroadcastReceiver {

    private Context context;

    private FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
    @SuppressLint("ResourceType")
    private void setLocale(String languageCode) {
        // 保存选定的语言设置到SPUtil中
        SPUtil.putBoolean(context, "isEN", languageCode.equals("en"));

        // 创建一个新的Locale对象
        Locale newLocale;
        if (languageCode.equals("en")) {
            newLocale = Locale.ENGLISH;
        } else {
            newLocale = Locale.SIMPLIFIED_CHINESE;
        }

        // 使用MyContextWrapper.wrap方法包装BaseContext，以应用新的语言设置
        Context wrappedContext = MyContextWrapper.wrap(context, newLocale);
        Resources resources = wrappedContext.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());



        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        fragmentTransaction.replace(R.id.aboutus_layout, aboutUsFragment);
        fragmentTransaction.commit();

        // ...
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnglish = intent.getBooleanExtra("isEnglish", true);
        this.context = context;
        // 执行语言切换的逻辑，例如调用setLocale方法
        setLocale(isEnglish ? "en" : "zh");

        // 刷新界面或更新界面元素
        AboutUsFragment fragment = new AboutUsFragment();
        if (fragmentManager != null) {
            fragment.setFragmentManager(fragmentManager);
            fragment.refreshUI();
        }
    }


}

