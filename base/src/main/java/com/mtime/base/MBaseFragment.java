package com.mtime.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mtime.base.bean.event.MEventBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiJiaZhi on 16/11/7.
 * <p>
 * base
 */

public abstract class MBaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();
    // 为了防止getActivity()空指针，用mActivity代替getActivity()
    protected MBaseActivity mActivity;
    //是否第一次进来
    private boolean mIsInit = false;

    /**
     * Fragment Content view
     */
    private View mInflateView;

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化view监听
     */
    protected abstract void initView(View parentView, Bundle savedInstanceState);

    /**
     * 初始化view监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 是否开启接收eventbus事件 默认不开启
     *
     * @return
     */
    protected boolean isStartEventBus() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MBaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isStartEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mInflateView) {
            // 强制竖屏显示
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int layoutResId = getLayoutId();
            if (layoutResId > 0)
                mInflateView = inflater.inflate(getLayoutId(), container, false);

            // 解决点击穿透问题
            /*
             * mInflateView.setOnTouchListener(new View.OnTouchListener() {
             * 
             * @Override public boolean onTouch(View v, MotionEvent event) { return true; } });
             */
        }
        return mInflateView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view, savedInstanceState);
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsInit) {
            initDatas();
            mIsInit = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MEventBean event) {
    }

    @Override
    public void onDestroy() {
        if (isStartEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 通过ID查找控件
     *
     * @param viewId 控件资源ID
     * @param <VIEW> 泛型参数，查找得到的View
     * @return 找到了返回控件对象，否则返回null
     */
    final public <VIEW extends View> VIEW findViewById(@IdRes int viewId) {
        return (VIEW) mInflateView.findViewById(viewId);
    }

}
