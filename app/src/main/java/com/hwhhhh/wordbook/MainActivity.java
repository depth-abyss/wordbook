package com.hwhhhh.wordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hwhhhh.wordbook.ui.home.HomeFragment;
import com.hwhhhh.wordbook.ui.home.wordInfo.WordInfoFragment;
import com.hwhhhh.wordbook.ui.notebook.NotebookFragment;
import com.hwhhhh.wordbook.ui.user.UserFragment;
import com.hwhhhh.wordbook.util.DBHelper;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Fragment currentFragment, homeFragment, userFragment, notebookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragment = HomeFragment.getInstance();
        userFragment = UserFragment.getInstance();
        notebookFragment = NotebookFragment.getInstance();
        currentFragment = homeFragment;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //使软键盘不会被顶起
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
//        DBHelper dbHelper = new DBHelper(this);
//        getSupportActionBar().hide();
        //底部导航栏
        Log.d(TAG, "onCreate: ");
        initFragment(savedInstanceState);
        BottomNavigationView navView = findViewById(R.id.nav_bottom_view);
        navView.setOnNavigationItemSelectedListener(mNavViewListener);

    }

    //初始化fragment
    private void initFragment(Bundle savedInstanceState) {
        //第一次初始化
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //添加三个fragment，并且隐藏“帮助”和“生词本”部分
            fragmentTransaction
                    .add(R.id.nav_host_fragment, homeFragment, homeFragment.getClass().getName())
                    .add(R.id.nav_host_fragment, userFragment, userFragment.getClass().getName())
                    .add(R.id.nav_host_fragment, notebookFragment, notebookFragment.getClass().getName())
                    .hide(userFragment)
                    .hide(notebookFragment);
            fragmentTransaction.commit();
        }
    }

    //底部导航栏点击切换视图
    private BottomNavigationView.OnNavigationItemSelectedListener mNavViewListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    if (currentFragment != homeFragment) {
                        switchFragment(currentFragment, homeFragment);
                        return true;
                    }break;
                case R.id.navigation_notebook:
                    if (currentFragment != notebookFragment) {
                        switchFragment(currentFragment, notebookFragment);
                        return true;
                    }break;
                case R.id.navigation_user:
                    if (currentFragment != userFragment) {
                        switchFragment(currentFragment, userFragment);
                        return true;
                    }break;
            }
            return false;
        }
    };

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .hide(from)
                    .show(to);
            fragmentTransaction.commit();
            currentFragment = to;
        }
    }

    /**
     * 重写返回键，homeFragment的回退。
     */
    @Override
    public void onBackPressed() {
        //获取回退栈中的数目
        int i = homeFragment.getChildFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: " + i);
       if (i > 0) {
            while (i > 0) {
                if (homeFragment.getChildFragmentManager().popBackStackImmediate("toLocalWord", 1)){
                    HomeFragment.getInstance().currentFragment = WordInfoFragment.getInstance();
                    break;
                }
                //回退到新闻列表界面
                homeFragment.getChildFragmentManager().popBackStack();
                i--;
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(MainActivity.this, "你点击了菜单选项", Toast.LENGTH_LONG).show();
        return true;
    }

}
