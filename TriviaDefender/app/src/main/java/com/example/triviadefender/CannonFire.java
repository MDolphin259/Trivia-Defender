package com.example.triviadefender;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class CannonFire {
    static final int SHOT_BLAST = 180;
    private static int shotNum = 0;
    private final int id;
    private final GameActivity gameActivity;
    private ImageView imageView;
    private ObjectAnimator moveX, moveY;
    private final float startX;
    private final float startY;
    private float endX;
    private float endY;
    public static int idVal = 1000;
    private static final double DISTANCE_TIME = 0.75;
    public static int activeShotCount = 0;

    //Instantiates Coordinates and the number provided to each taken shot
    CannonFire(GameActivity gameActivity, float startX, float startY, float endX, float endY) {
        this.gameActivity = gameActivity;
        System.out.println(gameActivity);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.id = shotNum++;
        initialize();
    }

    //Initializes each shot and sets the image to refer to when calling the drawable for imageview
    private void initialize() {
        activeShotCount++;
        imageView = new ImageView(gameActivity);
        //Why were we using negative numbers for the ID?
        imageView.setId(idVal++);
        imageView.setImageResource(R.drawable.interceptor);
        imageView.setTransitionName("Interceptor " + id);

        final int shotWidth = (int) (imageView.getDrawable().getIntrinsicWidth() * 0.5);

        imageView.setX(startX);
        imageView.setY(startY);
        //*****Review this later******
        imageView.setZ(-10);

        endX -= shotWidth;
        endY -= shotWidth;

        float angle = calculateAngle(imageView.getX(), imageView.getY(), endX, endY);

        imageView.setRotation(angle);
        gameActivity.getLayout().addView(imageView);

        double distance =  Math.sqrt((endY - imageView.getY()) * (endY - imageView.getY()) + (endX - imageView.getX()) * (endX - imageView.getX()));

        moveX = ObjectAnimator.ofFloat(imageView, "x", endX);
        moveX.setInterpolator(new AccelerateInterpolator());
        moveX.setDuration((long) (distance * DISTANCE_TIME));

        moveY = ObjectAnimator.ofFloat(imageView, "y", endY);
        moveY.setInterpolator(new AccelerateInterpolator());
        moveY.setDuration((long) (distance * DISTANCE_TIME));

        moveX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gameActivity.getLayout().removeView(imageView);
                makeBlast();
            }
        });

    }
    private void makeBlast() {
        activeShotCount--;
        SoundPlayer.getInstance().start("interceptor_blast");
        final ImageView explodeView = new ImageView(gameActivity);
        explodeView.setImageResource(R.drawable.i_explode);

        explodeView.setTransitionName("Interceptor blast");

        float w = explodeView.getDrawable().getIntrinsicWidth();
        explodeView.setX(this.getX() - (w/2));

        explodeView.setY(this.getY() - (w/2));

        explodeView.setZ(-15);

        gameActivity.getLayout().addView(explodeView);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(explodeView, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gameActivity.getLayout().removeView(explodeView);
            }
        });
        alpha.start();

        gameActivity.applyInterceptorHit(this, imageView.getId());
    }


    private float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (180.0f - angle);
    }
    void launch() {
        moveX.start();
        moveY.start();
    }

    float getX() {
        int xVar = imageView.getWidth() / 2;
        return imageView.getX() + xVar;
    }

    float getY() {
        int yVar = imageView.getHeight() / 2;
        return imageView.getY() + yVar;
    }

}
