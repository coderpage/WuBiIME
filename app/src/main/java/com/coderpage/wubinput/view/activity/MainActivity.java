package com.coderpage.wubinput.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.TestActivity;
import com.coderpage.wubinput.model.Wubi;
import com.coderpage.wubinput.view.adapter.MViewPagerAdapter;
import com.coderpage.wubinput.view.widget.SelectDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abner
 * @since 2015-08-29
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mainViewPager;
    private FragmentManager fragmentManager;
    private MViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>(4);

    private RelativeLayout footerHomeContainer;
    private RelativeLayout footerPracticeContainer;
    private RelativeLayout footerTestContainer;
    private RelativeLayout footerSettingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        Fragment homeFragment = new HomeFragment();
        Fragment practiceFragment = new PracticeFragment();
        Fragment testFragment = new TestFragment();
        Fragment settingFragment = new SettingFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(practiceFragment);
        fragmentList.add(testFragment);
        fragmentList.add(settingFragment);

        fragmentManager = getSupportFragmentManager();
        mViewPagerAdapter = new MViewPagerAdapter(fragmentManager, fragmentList);
        mainViewPager.setAdapter(mViewPagerAdapter);
        mainViewPager.setOnPageChangeListener(this);

        footerHomeContainer = (RelativeLayout) findViewById(R.id.footer_home_container);
        footerPracticeContainer = (RelativeLayout) findViewById(R.id.footer_practice_container);
        footerTestContainer = (RelativeLayout) findViewById(R.id.footer_test_container);
        footerSettingContainer = (RelativeLayout) findViewById(R.id.footer_setting_container);

        footerHomeContainer.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
    }

    public void enableIME(View view) {
        Intent intent = new Intent("android.settings.INPUT_METHOD_SETTINGS");
        startActivity(intent);
    }

    public void pickIME(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showInputMethodPicker();
    }

    public void testTmp(View view) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
    }

    public void practiceSingle(View view) {
        SelectDialog dialog = new SelectDialog(MainActivity.this, Wubi.TypingMode.PRACTICE_SINGLE_WORD);
        dialog.showMe();
    }

    public void practicePhrase(View view) {
        Intent intent = new Intent(MainActivity.this, PracticeInputActivity.class);
        intent.putExtra(PracticeInputActivity.BUNDLE_KEY_MODE, Wubi.TypingMode.PRACTICE_PHRASH);
        intent.putExtra(PracticeInputActivity.BUNDLE_KEY_SELECT, 0);
        startActivity(intent);
    }

    public void practiceArticle(View view) {
        SelectDialog dialog = new SelectDialog(MainActivity.this, Wubi.TypingMode.PRACTICE_ARTICLE);
        dialog.showMe();
    }

    public void testSingle(View view) {

        SelectDialog dialog = new SelectDialog(MainActivity.this, Wubi.TypingMode.TEST_SINGLE_WORD);
        dialog.showMe();
    }

    public void testPhrase(View view) {
        Intent intent = new Intent(MainActivity.this, TestInputActivity.class);
        intent.putExtra(PracticeInputActivity.BUNDLE_KEY_MODE, Wubi.TypingMode.TEST_PHRASH);
        intent.putExtra(PracticeInputActivity.BUNDLE_KEY_SELECT, 0);
        startActivity(intent);
    }

    public void testArticle(View view) {
        SelectDialog dialog = new SelectDialog(MainActivity.this, Wubi.TypingMode.TEST_ARTICLE);
        dialog.showMe();
    }

    //=====================================On footer item click=====================================

    public void footerHomeClick(View view) {
        mainViewPager.setCurrentItem(0);
    }

    public void footerPracticeClick(View view) {
        mainViewPager.setCurrentItem(1);
    }

    public void footerTestClick(View view) {
        mainViewPager.setCurrentItem(2);
    }

    public void footerSettingClick(View view) {
        mainViewPager.setCurrentItem(3);
    }

    //=====================================OnViewPagerChange========================================
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateFooterItemBackgroundColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //==============================================================================================

    private void updateFooterItemBackgroundColor(int position){
        switch (position){
            case 0:
                footerHomeContainer.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
                footerPracticeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerTestContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerSettingContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 1:
                footerHomeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerPracticeContainer.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
                footerTestContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerSettingContainer.setBackgroundColor(getResources().getColor(R.color.transparent));                break;
            case 2:
                footerHomeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerPracticeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerTestContainer.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
                footerSettingContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 3:
                footerHomeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerPracticeContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerTestContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                footerSettingContainer.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
                break;
            default:
                break;
        }
    }
}
