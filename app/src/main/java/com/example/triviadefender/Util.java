package com.example.triviadefender;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class Util {
    private static Util instance;

    public static Util getInstance() {
        if (instance == null)
            instance = new Util();
        return instance;
    }
    /**
     * Remove all System UI elements from the screen
     * @param activity
     */
    public void fullScreenMode(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Returns the screen width and height
     * @param activity
     */
    public ScreenWidthHeight getScreenDimensions(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new ScreenWidthHeight(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    /**
     * Loads all sounds at the beginning
     */
    public void loadSounds(Activity activity){
        SoundPlayer.getInstance().setupSound(activity, "background", R.raw.background, true);
        SoundPlayer.getInstance().setupSound(activity, "base_blast", R.raw.base_blast, false);
        SoundPlayer.getInstance().setupSound(activity, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(activity, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);
        SoundPlayer.getInstance().setupSound(activity, "launch_interceptor", R.raw.launch_interceptor,false);
        SoundPlayer.getInstance().setupSound(activity, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(activity, "missile_miss", R.raw.missile_miss,false);
    }

    public class ScreenWidthHeight{
        private int width;
        private int height;

        ScreenWidthHeight(int width, int height){
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
