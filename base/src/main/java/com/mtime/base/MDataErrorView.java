package com.mtime.base;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by LiJiaZhi on 16/11/7.
 * <p>
 * loading empty error 页面
 */

public class MDataErrorView extends FrameLayout {
    //
    private ViewGroup mLoadingVG, mErrorVG, mEmptyVG;
    // loading
    private ImageView mLoadingIv;
    private TextView mLoadTitleTv;

    // empty
    private ImageView mEmptyIv;
    private TextView mEmptyTitleTv;
    private TextView mEmptySubTitleTv;

    // error 图标，标题，副标题
    private ImageView mErrorIv;
    private TextView mErrorTitleTv;
    private TextView mErrorRetryButtonTv;

    private DataErrorListener mDataErrorListener;
    private ObjectAnimator mAnimator;

    /**
     * error retry按钮
     * @param listener
     */
    public void setRetryListener(DataErrorListener listener) {
        this.mDataErrorListener = listener;
    }

    public MDataErrorView(Context context) {
        super(context);
        init();
    }

    public MDataErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MDataErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.m_layout_base_default_view, this);
        mLoadingVG = (ViewGroup) findViewById(R.id.loading_layout);
        mEmptyVG = (ViewGroup) findViewById(R.id.empty_layout);
        mErrorVG = (ViewGroup) findViewById(R.id.error_layout);

        mLoadingIv = (ImageView) findViewById(R.id.m_layout_load_title_iv);
        mLoadTitleTv = (TextView) findViewById(R.id.m_layout_load_title_tv);

        mEmptyIv = (ImageView) findViewById(R.id.m_layout_empty_iv);
        mEmptyTitleTv = (TextView) findViewById(R.id.m_layout_empty_title_tv);
        mEmptySubTitleTv = (TextView) findViewById(R.id.m_layout_empty_subtitle_tv);


        mErrorIv = (ImageView) findViewById(R.id.m_layout_error_iv);
        mErrorTitleTv = (TextView) findViewById(R.id.m_layout_error_title_iv);
        mErrorRetryButtonTv = (TextView) findViewById(R.id.m_layout_error_subtitle_tv);
        mErrorRetryButtonTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataErrorListener != null) {
                    mDataErrorListener.retry();
                }

            }
        });
    }

    /**
     * 显示loading页面
     */
    public void showLoading() {
        setVisibility(VISIBLE);
        mLoadingVG.setVisibility(View.VISIBLE);
        mErrorVG.setVisibility(View.GONE);
        mEmptyVG.setVisibility(View.GONE);
        mLoadingIv.setVisibility(VISIBLE);
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(mLoadingIv, "rotation", 0f, 360f);
            mAnimator.setDuration(1200);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(-1);
        }
        if (mAnimator.isRunning() || mAnimator.isStarted()) {
            return;
        }
        mAnimator.start();
    }

    public void hideLoading() {
        if (mAnimator != null) {
            mAnimator.end();
        }
        mLoadingVG.setVisibility(GONE);
        mLoadingIv.setVisibility(GONE);
    }

    /**
     * 展示为空页面
     */
    public void showEmptyView() {
        setVisibility(VISIBLE);
        hideLoading();
        mErrorVG.setVisibility(View.GONE);
        mEmptyVG.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        mEmptyVG.setVisibility(GONE);
    }

    /**
     * 显示error页面
     */
    public void showErrorView() {
        setVisibility(VISIBLE);
        hideLoading();
        hideEmptyView();
        mErrorVG.setVisibility(View.VISIBLE);
    }

    public void hideErrorView() {
        mErrorVG.setVisibility(View.GONE);
    }

    /**
     * 显示error页面----默认显示retry按钮，通过setRetryListener监听回调
     */
    public void showError(View.OnClickListener clickListener) {
        setVisibility(VISIBLE);
        hideLoading();
        hideEmptyView();
        mErrorVG.setVisibility(View.VISIBLE);
        if (null != clickListener) {
            mErrorRetryButtonTv.setVisibility(VISIBLE);
            mErrorRetryButtonTv.setOnClickListener(clickListener);
        } else {
            mErrorRetryButtonTv.setVisibility(GONE);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility != VISIBLE) {
            hideLoading();
        }
        super.setVisibility(visibility);
    }

    /**
     * loading
     * @return
     */
    public ImageView getLoadingIv() {
        return mLoadingIv;
    }
    public TextView getLoadingTitleTv() {
        return mLoadTitleTv;
    }

    /**
     * empty
     * @return
     */
    public TextView getEmptyTitleTv() {
        return mEmptyTitleTv;
    }

    public TextView getEmptySubTitleTv() {
        return mEmptySubTitleTv;
    }

    public void setEmptyDrawable(int drawable) {
        mEmptyIv.setImageResource(drawable);
    }

    public void setEmptyTitle(String text) {
        mEmptyTitleTv.setText(text);
    }

    /**
     * error
     * @param text
     */
    public void setErrorTitle(String text) {
        mErrorTitleTv.setText(text);
    }

    public ImageView getErrorIv() {
        return mErrorIv;
    }

    public TextView getErrorTitleTv() {
        return mErrorTitleTv;
    }

    public TextView getErrorRetryButtonTv() {
        return mErrorRetryButtonTv;
    }






    public interface DataErrorListener {
        void retry();
    }

}
