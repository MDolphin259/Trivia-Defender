package com.example.defensecommander;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class Missile {
    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final int screenWidth;
    private final long screenTime;
    private static final String TAG = "Missile";
    private final boolean hit = false;

    Missile(int screenWidth, int screenHeight, long screenTime, final MainActivity mainActivity) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenTime = screenTime;
        this.mainActivity = mainActivity;

        imageView = new ImageView(mainActivity);
        //imageView.setX(-500); //TODO change??
        SoundPlayer.getInstance().start("launch_missile");
        mainActivity.runOnUiThread(() -> mainActivity.getLayout().addView(imageView));

    }

    AnimatorSet setData(final int drawId) {
        mainActivity.runOnUiThread(() -> imageView.setImageResource(drawId));

        int startX = (int) (Math.random() * screenWidth); //x
        int endX = (int) (Math.random() * screenWidth);
        float a = calculateAngle(startX, 0, endX, screenHeight-100);
        imageView.setRotation(a);

        ObjectAnimator yAnim = ObjectAnimator.ofFloat(imageView, "y", -200, screenHeight-100);

        yAnim.setInterpolator(new LinearInterpolator());
        yAnim.setDuration(screenTime);
        yAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.runOnUiThread(() -> {
                    if (!hit) {
                        hitTheGround();
                        mainActivity.getLayout().removeView(imageView);
                        mainActivity.removeMissile(Missile.this);
                    }

                    Log.d(TAG, "run: NUM VIEWS " +
                            mainActivity.getLayout().getChildCount());
                });

            }
        });

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "x", startX, endX);
        xAnim.setInterpolator(new LinearInterpolator());
        xAnim.setDuration(screenTime);

        aSet.playTogether(xAnim, yAnim);
        return aSet;

    }

    private float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (180.0f - angle);
    }

    void stop() {
        aSet.cancel();
    }

    float getX() {
        return imageView.getX();
    }

    float getY() {
        return imageView.getY();
    }

    float getWidth() {
        return imageView.getWidth();
    }

    float getHeight() {
        return imageView.getHeight();
    }

    void hitTheGround(){
        final ImageView explodeView = new ImageView(mainActivity);
        explodeView.setImageResource(R.drawable.explode);
        explodeView.setTransitionName("Missile Miss");
        float w = explodeView.getDrawable().getIntrinsicWidth();
        explodeView.setX(this.getX() - (w/2)+50);
        explodeView.setY(this.getY() - (w/2));
        explodeView.setZ(0);

        mainActivity.getLayout().addView(explodeView);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(explodeView, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(explodeView);
            }
        });
        alpha.start();

        mainActivity.applyMissileBlast(this, imageView.getId());
    }

    void interceptorBlast(float x, float y){
        aSet.cancel();
/*
        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.explode);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset - 100);
        iv.setZ(1);

        iv.setRotation((float) (360.0 * Math.random()));

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();
*/

    }


    void missileBlast(float x, float y) {

        aSet.cancel();

        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.blast);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset - 100);
        iv.setZ(1);

        iv.setRotation((float) (360.0 * Math.random()));

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();




    }

}
