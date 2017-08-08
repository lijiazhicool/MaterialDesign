package com.av.videoplayer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.av.videoplayer.adapter.PictureAdapter;
import com.av.videoplayer.bean.Picture;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private List<Picture> fruitList = new ArrayList<>();
    private PictureAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    private Picture[] pictures =
        { new Picture("水漫金山", R.mipmap.aa), new Picture("湖光山色", R.mipmap.bb), new Picture("郁郁葱葱", R.mipmap.cc),
            new Picture("草长莺飞", R.mipmap.dd), new Picture("春山如笑", R.mipmap.ee), new Picture("柳绿花红 ", R.mipmap.ff),
            new Picture("江山如画", R.mipmap.xx), new Picture("青山不老", R.mipmap.yy), new Picture("大好河山", R.mipmap.zz) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // App Logo
        mToolbar.setLogo(R.mipmap.ic_launcher);
        // Title
        mToolbar.setTitle("My Title");
        // Sub Title
        mToolbar.setSubtitle("Sub title");
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        // 创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle =
            new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
        mDrawerToggle.syncState();// 实现箭头和三条杠图案切换和抽屉拉合的同步
        mDrawerLayout.setDrawerListener(mDrawerToggle);// 监听实现侧边栏的拉开和闭合,即抽屉drawer的闭合和打开

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        // 头部信息
        View headerView = mNavigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.mail)).setText("测试");
        // item
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_call) {
                    Toast.makeText(MainActivity.this, "按钮被点击了", Toast.LENGTH_SHORT).show();
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        initDatas();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDatas();
            }
        });
    }

    private void refreshDatas() {

        // 网络获取数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 获取数据
                initDatas();
                swipeRefresh.setRefreshing(false);
            }
        }, 2000);

    }

    private void initDatas() {

        for (int i = 0; i < pictures.length; i++) {

            fruitList.add(pictures[i]);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        // 通过MenuItem得到SearchView
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        // 设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(false);
        // 设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        // mSearchView.setIconifiedByDefault(false);
        // 设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//         mSearchView.onActionViewExpanded();

        // 设置最大宽度
//        mSearchView.setMaxWidth(500);
        // 设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(true);
        // 设置输入框提示语
        mSearchView.setQueryHint("hint");


        // 搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // 搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
        // 搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("CSDN_LQR", "TextSubmit : " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("CSDN_LQR", "TextChange --> " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "按钮被点击了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "按钮被点击了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "按钮被点击了", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }
    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}
