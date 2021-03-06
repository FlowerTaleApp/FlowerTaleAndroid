package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private BottomNavigationBar mBottomNavigationBar;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ImageView mToolbarNavImage;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbarNavImage = findViewById(R.id.toolbar_nav_image);
        mToolbarTitle = findViewById(R.id.toolbar_title_text);
        mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setAutoHideEnabled(true);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_1, "Discover")
                        .setActiveColorResource(R.color.mypink2))
                .addItem(new BottomNavigationItem(R.drawable.icon_2, "Recognise")
                        .setActiveColorResource(R.color.mypink2))
                .addItem(new BottomNavigationItem(R.drawable.icon_3, "Flower")
                        .setActiveColorResource(R.color.mypink2))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        setBottomNavigationItem(mBottomNavigationBar, 6, 24, 18);
        mFragments = getFragments();
        mBottomNavigationBar.selectTab(0);
        setToolbarTitle("发现");
        mToolbarNavImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    default:
                        break;
                    case R.id.logout:
                        FlowerTaleApiService.getInstance().doLogout();
                        PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext()).edit().clear().apply();
                        finish();
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        break;
                    case R.id.nav_group:
                        Intent intent = new Intent(MainActivity.this, InvitationRecordActivity.class);
                        startActivity(intent);
                        break;
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                        dialog.setTitle("您有新的群组邀请");
//                        dialog.setMessage("是否立即前往查看？");
//                        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                        dialog.show();
                    case R.id.nav_profile:
                        Intent intent_profile = new Intent(MainActivity.this,UserInfoActivity.class);
                        startActivity(intent_profile);
                        break;
                    case R.id.nav_settings:
//                        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                        Notification notification = new Notification.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("每日推送")
//                        .setContentText("每日推送内容")
//                        .setWhen(System.currentTimeMillis()).build();
//                        manager.notify(0, notification);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onTabSelected(int position) {
        if (mFragments != null) {
            if (position < mFragments.size()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new DiscoverFragment();
                        setToolbarTitle("发现");
                        break;
                    case 1:
                        fragment = new RecogniseFragment();
                        setToolbarTitle("识别");
                        break;
                    case 2:
                        fragment = new FlowerFragment();
                        setToolbarTitle("养护");
                        break;
                    default:
                        break;
                }
                mFragments.remove(position);
                mFragments.add(position, fragment);
                if (fragment.isAdded()) {
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                } else {
                    fragmentTransaction.add(R.id.frame_layout, fragment);
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {
        if (mFragments != null) {
            if (position < mFragments.size()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = mFragments.get(position);
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

    private void setToolbarTitle(String title) {
        mToolbarTitle.setText(title);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DiscoverFragment());
        fragments.add(new RecogniseFragment());
        fragments.add(new FlowerFragment());
        return fragments;
    }

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try {
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));
                        //获取到Tab内的文字控件
                        TextView labelView = view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));
                        //获取到Tab内的图像控件
                        ImageView iconView = view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


//    private void getDailyPush(){
////        FlowerTaleApiService.getInstance().doGetDailyPushFlowers();
////    }

}


