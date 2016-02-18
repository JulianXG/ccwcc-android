package com.kalyter.ccwcc.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.ui.main.tab.add.AddFragment;
import com.kalyter.ccwcc.ui.main.tab.home.HomeFragment;
import com.kalyter.ccwcc.ui.main.tab.local.LocalFragment;
import com.kalyter.ccwcc.ui.main.tab.statistics.StatisticsFragment;
import com.kalyter.ccwcc.ui.main.tab.user.UserFragment;

public class MainActivity extends AppCompatActivity {

    //百度地图相关全过程Activity级变量
    public static LocationClient locationClient;
    public static BDLocationListener myLocationListener;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //定义FragmentTabHost需要的变量
    private FragmentTabHost fragmentTabHost;

    private LayoutInflater layoutInflater;

    private Class fragmentArray[] = {HomeFragment.class
            , StatisticsFragment.class
            , AddFragment.class
            , LocalFragment.class
            , UserFragment.class};

    private int imageViewArray[] = {R.drawable.main_tab_item_home
                                    ,R.drawable.main_tab_item_statistics
                                    ,R.drawable.main_tab_item_add
                                    ,R.drawable.main_tab_item_local
                                    ,R.drawable.main_tab_item_user};

    private String strTextViewArray[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationClient = new LocationClient(getApplicationContext());
        initToolbar();
        initFragmentTabHost();

    }

    private void initToolbar() {
        //设置顶部标题栏
        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        drawerLayout=(DrawerLayout) findViewById(R.id.layout_main_left_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this
                , drawerLayout
                , toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void initFragmentTabHost() {
        strTextViewArray = getResources().getStringArray(R.array.toolbar_text_arrays);
        //绘制FragmentTabHost的过程
        layoutInflater = LayoutInflater.from(this);
        fragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.layout_main_relate_tab_content);
        fragmentTabHost.getTabWidget().setShowDividers(0);
        int count =fragmentArray.length;
        for (int i=0;i<count;i++) {
            //为每一个Tab设置图标和文字内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(strTextViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec,fragmentArray[i],null);
            //设置Tab按钮背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.app_background);
        }
    }

    //给Tab按钮设置图标和文字
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.layout_tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.main_tab_image);
        imageView.setImageResource(imageViewArray[index]);

        TextView textView = (TextView)view.findViewById(R.id.main_tab_text);
        textView.setText(strTextViewArray[index]);

        return view;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}