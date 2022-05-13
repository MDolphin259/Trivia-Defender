package com.example.triviadefender;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Question {
    private final GameActivity gameActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final int screenWidth;
    private final long screenTime;
    private static final String TAG = "Question";
    private final boolean hit = false;

    Question(int screenWidth, int screenHeight, long screenTime, final GameActivity gameActivity) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenTime = screenTime;
        this.gameActivity = gameActivity;

        imageView = new ImageView(gameActivity);
        SoundPlayer.getInstance().start("launch_missile");
        gameActivity.runOnUiThread(() -> gameActivity.getLayout().addView(imageView));

    }

    AnimatorSet setData(final int drawId) {
        gameActivity.runOnUiThread(() -> imageView.setImageResource(drawId));

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
                gameActivity.runOnUiThread(() -> {
                    final ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f);
                    alpha.setInterpolator(new LinearInterpolator());
                    alpha.setDuration(500);
                    alpha.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            gameActivity.getLayout().removeView(imageView);
                            gameActivity.removeQuestion(Question.this);
                        }
                    });
                    alpha.start();
                    Log.d(TAG, "run: NUM VIEWS " +
                            gameActivity.getLayout().getChildCount());
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
        //TODO: fix this
        //aSet.cancel();
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


    void interceptorBlast(float x, float y){
        aSet.cancel();
        PopUpHandler.CallPopUp(gameActivity); //Makes a pop up for a trivia question
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

        final ImageView iv = new ImageView(gameActivity);
        iv.setImageResource(R.drawable.blast);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset - 100);
        iv.setZ(1);

        iv.setRotation((float) (360.0 * Math.random()));

        gameActivity.getLayout().removeView(imageView);
        gameActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gameActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();
    }



}
