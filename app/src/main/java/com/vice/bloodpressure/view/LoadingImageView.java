package com.vice.bloodpressure.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class LoadingImageView extends AppCompatImageView {
    private AnimationDrawable mAnimationDrawable;
    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startLoadingAnim(){
        if (this.getBackground() instanceof AnimationDrawable) {
            mAnimationDrawable = (AnimationDrawable) this.getBackground();
            this.post(new Runnable() {

                @Override
                public void run() {
                    mAnimationDrawable.start();
                }
            });
        }
    }
    public void stopLoaddingAnim() {
        if (this==null){
            return;
        }
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
    }
}
