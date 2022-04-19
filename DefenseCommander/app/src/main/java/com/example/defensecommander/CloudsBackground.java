package com.example.defensecommander;

import static com.example.defensecommander.MainActivity.screenHeight;
import static com.example.defensecommander.MainActivity.screenWidth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CloudsBackground{
    private final Context context;
    private final ViewGroup layout;
    private ImageView backImageA;
    private ImageView backImageB;
    private final long duration;
    private final int resId;
    private static final String TAG = "CloudsBackground";
    private static boolean running = true;
    private AnimatorSet aSet = new AnimatorSet();



    CloudsBackground(Context context, ViewGroup layout, int resId, long duration) {
        this.context = context;
        this.layout = layout;
        this.resId = resId;
        this.duration = duration;

        setupBackground();
    }

    public static void stop() {
        running = false;
    }

    private void setupBackground() {
        backImageA = new ImageView(context);
        backImageB = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(screenWidth + getBarHeight(), screenHeight);
        backImageA.setLayoutParams(params);
        backImageB.setLayoutParams(params);

        layout.addView(backImageA);
        layout.addView(backImageB);

        Bitmap backBitmapA = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap backBitmapB = BitmapFactory.decodeResource(context.getResources(), resId);

        backImageA.setImageBitmap(backBitmapA);
        backImageB.setImageBitmap(backBitmapB);

        backImageA.setScaleType(ImageView.ScaleType.FIT_XY);
        backImageB.setScaleType(ImageView.ScaleType.FIT_XY);

        backImageA.setZ(10);
        backImageB.setZ(10);

        //backImageA.setImageAlpha(127);
        //backImageB.setImageAlpha(127);


        animateBack();
    }

    private void animateBack() {

        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);

        animator.addUpdateListener(animation -> {
            if (!running) {
                animator.cancel();
                return;
            }
            final float progress = (float) animation.getAnimatedValue();
            float width = screenWidth + getBarHeight();

            float a_translationX = width * progress;
            float b_translationX = width * progress - width;

            backImageA.setTranslationX(a_translationX);
            backImageB.setTranslationX(b_translationX);

        });
        animator.start();
        startCloudsFade();
    }

    private void startCloudsShow(){
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(backImageA, "alpha", 0.95f, 0.25f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(30000);
        alpha.addUpdateListener(animation -> {
            if (!running) {
                alpha.cancel();
                return;
            }
        });
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startCloudsFade();
            }
        });
        alpha.start();
        final ObjectAnimator alpha1 = ObjectAnimator.ofFloat(backImageB, "alpha", 0.95f, 0.25f);
        alpha1.setInterpolator(new LinearInterpolator());
        alpha1.setDuration(30000);
        alpha1.addUpdateListener(animation -> {
            if (!running) {
                alpha1.cancel();
                return;
            }
        });
        alpha1.start();
        //aSet.playTogether(alpha, alpha1);

    }

    private void startCloudsFade() {
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(backImageA, "alpha", 0.25f, 0.95f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(30000);
        alpha.addUpdateListener(animation -> {
            if (!running) {
                alpha.cancel();
                return;
            }
        });
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startCloudsShow();
            }
        });
        alpha.start();
        final ObjectAnimator alpha1 = ObjectAnimator.ofFloat(backImageB, "alpha", 0.25f, 0.95f);
        alpha1.setInterpolator(new LinearInterpolator());
        alpha1.setDuration(30000);
        alpha1.addUpdateListener(animation -> {
            if (!running) {
                alpha1.cancel();
                return;
            }
        });
        alpha1.start();
        //aSet.playTogether(alpha, alpha1);
    }


    private int getBarHeight() {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
