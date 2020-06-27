package com.example.testone.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.testone.Adapter.TabAdapter;
import com.example.testone.Fragment.ChannelFragment;
import com.example.testone.Fragment.HomeFragment;
import com.example.testone.Fragment.MessageFragment;
import com.example.testone.Fragment.MyFragment;
import com.example.testone.R;
import com.example.testone.Util.SPUtil;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private final String TAG = "MainActivity";
    private final int HOME = 0;
    private final int EYE = 1;
    private final int MESSAGE = 2;
    private final int MY = 3;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    SPUtil spUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spUtil = new SPUtil(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTabLayout();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected tab: " + tab.getPosition());
        switch (tab.getPosition()) {
            case HOME:
                tab.setIcon(R.drawable.tab_home_active);
                break;
            case EYE:
                tab.setIcon(R.drawable.tab_eye_active);
                break;
            case MESSAGE:
                tab.setIcon(R.drawable.bell_active);
                break;
            case MY:
                tab.setIcon(R.drawable.tab_my_active);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabUnselected tab: " + tab.getPosition());
        switch (tab.getPosition()) {
            case HOME:
                tab.setIcon(R.drawable.tab_home);
                break;
            case EYE:
                tab.setIcon(R.drawable.tab_eye);
                break;
            case MESSAGE:
                tab.setIcon(R.drawable.bell);
                break;
            case MY:
                tab.setIcon(R.drawable.tab_my);
                break;
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabReselected tab: " + tab.getPosition());
    }

    private void initTabLayout() {
        Log.d(TAG, "start to initialize TabLayout.");
        tabLayout.addOnTabSelectedListener(this);
        HomeFragment homeFragment = new HomeFragment();

        ChannelFragment channelFragment = new ChannelFragment();
        MessageFragment messageFragment = new MessageFragment();
        MyFragment myFragment = new MyFragment();
        List<Fragment> fragments = new ArrayList<Fragment>() {
            {
                add(homeFragment);
                add(channelFragment);
                add(messageFragment);
                add(myFragment);
            }
        };

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), fragments);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        try {
            Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.tab_eye);
            Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.bell);
            Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.tab_my);
        } catch (NullPointerException e) {
            Log.e(TAG, "initialize tab item icon exception: ", e);
        }
    }
    public SPUtil getSpUtil(){
        return  spUtil;
    }
}
