package projectmanager.dada;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import projectmanager.dada.fragment.AcceptedTaskFragment;
import projectmanager.dada.fragment.NearbyFragment;
import projectmanager.dada.fragment.PublishedTaskFragment;
import projectmanager.dada.fragment.UserProfileFragment;
import projectmanager.dada.pages.PublishTaskStepOneActivity;

public class MainActivity extends FragmentActivity implements OnClickListener{

    private TextView publishTask;

    // 底部菜单4个Linearlayout
    private LinearLayout nearby;
    private LinearLayout accepted;
    private LinearLayout published;
    private LinearLayout profile;

    // 底部菜单4个ImageView
    private ImageView img_nearby;
    private ImageView img_accepted;
    private ImageView img_published;
    private ImageView img_profile;

    // 底部菜单4个菜单标题
    private TextView tv_nearby;
    private TextView tv_accepted;
    private TextView tv_published;
    private TextView tv_profile;

    private Fragment nearbyFragment;
    private Fragment acceptedFragment;
    private Fragment publishedFragment;
    private Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        onClick(nearby);

    }

    private void initEvent() {
        // 设置按钮监听
        nearby.setOnClickListener(this);
        accepted.setOnClickListener(this);
        published.setOnClickListener(this);
        profile.setOnClickListener(this);
        publishTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPage = new Intent(MainActivity.this, PublishTaskStepOneActivity.class);
                startActivity(nextPage);
            }
        });
    }

    private void initView() {

        // 底部菜单4个Linearlayout
        this.nearby = (LinearLayout) findViewById(R.id.nearby);
        this.accepted = (LinearLayout) findViewById(R.id.accepted);
        this.published = (LinearLayout) findViewById(R.id.published);
        this.profile = (LinearLayout) findViewById(R.id.profile);

        // 底部菜单4个ImageView
        this.img_nearby = (ImageView) findViewById(R.id.img_nearby);
        this.img_accepted = (ImageView) findViewById(R.id.img_accepted);
        this.img_published = (ImageView) findViewById(R.id.img_published);
        this.img_profile = (ImageView) findViewById(R.id.img_profile);

        // 底部菜单4个菜单标题
        this.tv_nearby = (TextView) findViewById(R.id.txt_nearby);
        this.tv_accepted = (TextView) findViewById(R.id.txt_accepted);
        this.tv_published = (TextView) findViewById(R.id.txt_published);
        this.tv_profile = (TextView) findViewById(R.id.txt_profile);

        this.publishTask = (TextView) findViewById(R.id.publishTask);

    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment

        hideFragment(transaction);

        switch (index) {
            case 0:
                if (nearbyFragment == null) {
                    nearbyFragment = new NearbyFragment();
                    transaction.add(R.id.fl_content, nearbyFragment);
                } else {

                    transaction.show(nearbyFragment);
                }
                break;
            case 1:
                if (acceptedFragment == null) {
//                    addressFragment = new AddressFragment();
                    acceptedFragment = new AcceptedTaskFragment();
                    transaction.add(R.id.fl_content, acceptedFragment);
                } else {
                    transaction.show(acceptedFragment);
                }

                break;
            case 2:
                if (publishedFragment == null) {
//                    publishedFragment = new FriendFragment();
                    publishedFragment = new PublishedTaskFragment();
                    transaction.add(R.id.fl_content, publishedFragment);
                } else {
                    transaction.show(publishedFragment);
                }

                break;
            case 3:
                if (profileFragment == null) {
//                    settingFragment = new SettingFragment();
                    profileFragment = new UserProfileFragment();
                    transaction.add(R.id.fl_content, profileFragment);
                } else {
                    transaction.show(profileFragment);
                }

                break;

            default:
                break;
        }
        transaction.addToBackStack(null);
        // 提交事务
        transaction.commit();

    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (nearbyFragment != null) {
            transaction.hide(nearbyFragment);
        }
        if (acceptedFragment != null) {
            transaction.hide(acceptedFragment);
        }
        if (publishedFragment != null) {
            transaction.hide(publishedFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }

    }


    @Override
    public void onClick(View v) {
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.nearby:
                img_nearby.setImageResource(R.drawable.main_tab_item_user_focus);
                tv_nearby.setTextColor(0xff1B940A);
                initFragment(0);
                break;
            case R.id.accepted:
                img_accepted.setImageResource(R.drawable.main_tab_item_user_focus);
                tv_accepted.setTextColor(0xff1B940A);
                initFragment(1);
                break;
            case R.id.published:
                img_published.setImageResource(R.drawable.main_tab_item_user_focus);
                tv_published.setTextColor(0xff1B940A);
                initFragment(2);
                break;
            case R.id.profile:
                img_profile.setImageResource(R.drawable.main_tab_item_user_focus);
                tv_profile.setTextColor(0xff1B940A);
                initFragment(3);
                break;

            default:
                break;
        }

    }

    private void restartBotton() {
        // ImageView置为灰色
        img_nearby.setImageResource(R.drawable.main_tab_item_user_normal);
        img_accepted.setImageResource(R.drawable.main_tab_item_user_normal);
        img_published.setImageResource(R.drawable.main_tab_item_user_normal);
        img_profile.setImageResource(R.drawable.main_tab_item_user_normal);
        // TextView置为白色
        tv_nearby.setTextColor(0xffffffff);
        tv_accepted.setTextColor(0xffffffff);
        tv_published.setTextColor(0xffffffff);
        tv_profile.setTextColor(0xffffffff);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
