package com.mtime.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by LiJiaZhi on 16/11/7.
 * <p>
 * base
 */

public abstract class MBaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    // 内容承载
    protected FrameLayout mContentFrameLayout;
    // loading empty error 页面
    protected MDataErrorView mControlLayout;
    protected View mRootView;

    protected Toolbar mToolbar;
    protected ActionBar mActionBar;
    protected View mTitle;

    private TextView mTitleTv;
    private TextView mRightTv;
    private ImageView mRightIv;
    private View mNeedLoginView;
    // 是否第一次进来
    private boolean mIsInit = false;

    private String mTitleStr = TAG;
    private boolean mUmengPageStart = false;
    private boolean hasFragment = false;

    private View.OnClickListener mLeftClickListener;// 左侧返回按钮点击处理

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 1.获取get Intent数据 2.状态保存数据读取
     *
     * @param savedInstanceState
     */
    protected void initBundleExtra(Bundle savedInstanceState) {
    }

    /**
     * 初始化UI view
     */
    protected abstract void initViews();

    /**
     * 初始化view监听
     */
    protected void initListeners() {
    }

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     *
     * @return
     */
    protected View getLoginView() {
        return null;
    }

    /**
     * 是否开启接收eventbus事件 默认开启:接收重新登录事件
     *
     * @return
     */
    protected boolean isStartEventBus() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        if (bundle != null) {
            // 如果系统回收的Activity， 但是系统却保留了Fragment， 当Activity被重新初始化， 此时， 系统保存的Fragment 的getActivity为空，
            // 所以要移除旧的Fragment ， 重新初始化新的Fragment
            String FRAGMENTS_TAG = "android:support:fragments";
            bundle.remove(FRAGMENTS_TAG);
        }
        super.onCreate(bundle);
        mRootView = LayoutInflater.from(this).inflate(R.layout.m_act_base, null);
        setContentView(mRootView);

        mContentFrameLayout = (FrameLayout) findViewById(R.id.m_act_base_content_fl);
        mControlLayout = (MDataErrorView) findViewById(R.id.m_act_base_control_fl);

        int layoutId = getLayoutId();
        if (layoutId != 0) {
            mContentFrameLayout.addView(LayoutInflater.from(this).inflate(layoutId, null));
        }

        mTitleTv = (TextView) findViewById(R.id.layout_title_title_tv);
        mToolbar = (Toolbar) findViewById(R.id.layout_title_toolbar);
        mRightTv = (TextView) findViewById(R.id.layout_title_right_tv);
        mRightIv = (ImageView) findViewById(R.id.layout_title_right_iv);
        // 添加一个没登录时 展示的登录view
        mNeedLoginView = getLoginView();
        if (mNeedLoginView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, mToolbar.getId());
            mNeedLoginView.setLayoutParams(params);
            mContentFrameLayout.addView(mNeedLoginView);
        }

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);// 是否显示返回按钮
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setTitle("");// 返回右边的软件名字设置为空
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_title_back);// 修改返回按钮icon
        }

        if (isStartEventBus()) {
            EventBus.getDefault().register(this);
        }

        initPopupFragment(getPopupFragment());

        initBundleExtra(bundle);
        initViews();
        initListeners();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        if (!mUmengPageStart) {
            hasFragment = true;
        }
        return super.getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsInit) {
            initDatas();
            mIsInit = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStartEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 简化findViewById
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
    /**
     * 初始化Fragment 本activity也可以作为一个frament的载体
     *
     * @param popupFragment
     */
    protected void initPopupFragment(Fragment popupFragment) {
        if (popupFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.m_act_base_content_fl, popupFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 获取当前Fragment
     *
     * @return
     */
    protected Fragment getPopupFragment() {
        return null;
    }

    /**
     * 显示loading页面
     */
    public void showLoading() {
        mControlLayout.setVisibility(View.VISIBLE);
        mControlLayout.showLoading();
    }

    /**
     * 隐藏loading页面
     */
    public void hideLoading() {
        mControlLayout.setVisibility(View.GONE);
        mControlLayout.hideLoading();
    }

    /**
     * 设置空页面图
     * 
     * @param drawable
     */
    public void setEmptyDrawable(int drawable) {
        mControlLayout.setEmptyDrawable(drawable);
    }

    /**
     * 设置空页面字符串
     * 
     * @param text
     */
    public void setEmptyTitle(String text) {
        mControlLayout.setEmptyTitle(text);
    }

    /**
     * 显示empty页面
     */
    public void showEmpty() {
        mControlLayout.setVisibility(View.VISIBLE);
        mControlLayout.showEmptyView();
    }

    /**
     * 隐藏empty页面
     */
    public void hideEmpty() {
        mControlLayout.setVisibility(View.GONE);
        mControlLayout.hideEmptyView();
    }

    /**
     * 显示error页面
     *
     * @param listener：点击页面的回调--重新请求数据
     */

    public void showError(View.OnClickListener listener) {
        mControlLayout.showError(listener);
    }

    /**
     * 设置空页面字符串
     *
     * @param text
     */
    public void setErrorTitle(String text) {
        mControlLayout.setErrorTitle(text);
    }




    /**
     * 控制login view 显示或隐藏
     * 
     * @param visible
     */
    public void showLoginView(int visible) {
        if (mNeedLoginView != null) {
            mNeedLoginView.setVisibility(visible);
        }
    }

    /**
     * 是否显示titlebar 默认显示
     *
     * @param isShow
     */
    public void setTitleShow(boolean isShow) {
        mToolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public TextView getRightTv() {
        return mRightTv;
    }

    public ImageView getmRightIv() {
        return mRightIv;
    }

    public TextView getTitleTv() {
        return mTitleTv;
    }

    /**
     * 设置显示返回按钮
     */
    protected void setBack() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);// 是否显示返回按钮
        }
        if (mToolbar == null) {
            return;
        }
        mLeftClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
        mToolbar.setNavigationOnClickListener(mLeftClickListener);
    }

    /**
     * 设置显示返回按钮-自定义监听
     */
    protected void setBack(final View.OnClickListener listener) {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);// 是否显示返回按钮
        }
        if (mToolbar == null) {
            return;
        }
        mLeftClickListener = listener;
        mToolbar.setNavigationOnClickListener(mLeftClickListener);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }
    /**
     * 设置toolbar中间文字
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitleStr = title;
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    /**
     * 设置toolbar右边文字
     *
     * @param text
     * @param listener
     */
    public void setRight(String text, View.OnClickListener listener) {
        if (mRightTv != null) {
            mRightTv.setVisibility(View.VISIBLE);
            mRightIv.setVisibility(View.GONE);
            mRightTv.setText(text);
            mRightTv.setOnClickListener(listener);
        }
    }

    /**
     * 设置toolbar右边图标
     *
     * @param drawableId
     * @param listener
     */
    public void setRight(int drawableId, View.OnClickListener listener) {
        if (mRightIv != null) {
            mRightTv.setVisibility(View.GONE);
            mRightIv.setVisibility(View.VISIBLE);
            mRightIv.setBackgroundResource(drawableId);
            mRightIv.setOnClickListener(listener);
        }
    }

}
