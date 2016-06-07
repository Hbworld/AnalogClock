package com.demo.analog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.demo.analog.Utils.Hour;
import com.demo.analog.Utils.Minute;
import com.demo.analog.Utils.Second;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ImageView imgHour, imgMinute, imgSecond;
    private Hour hour;
    private Minute minute;
    private Second second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hour = new Hour();
        minute = new Minute();
        second = new Second();

        findViews();
        getCurrentTime();
        setCurrentTime();
        Thread thread;

        Runnable runnable = new SecondCountDownRunner();
        thread = new Thread(runnable);
        thread.start();

    }

    private void findViews() {
        imgHour = (ImageView) findViewById(R.id.imgHour);
        imgMinute = (ImageView) findViewById(R.id.imgMinute);
        imgSecond = (ImageView) findViewById(R.id.imgSecond);
    }


    private void getCurrentTime() {

        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        hour.setHour(Float.parseFloat(TimeFormat.format(calendar.getTime())));
        minute.setMinute(calendar.get(Calendar.MINUTE));
        second.setSecond(calendar.get(Calendar.SECOND));

    }

    private void setCurrentTime() {
        RotateAnimation rotateAnimationSecond = new RotateAnimation(
                0, second.getSecond() * 6,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);


        rotateAnimationSecond.setDuration(1000);
        rotateAnimationSecond.setFillAfter(true);

        RotateAnimation rotateAnimationMinute = new RotateAnimation(
                0, minute.getMinute() * 6,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);


        rotateAnimationMinute.setDuration(1000);
        rotateAnimationMinute.setFillAfter(true);

        hour.setTotalHour(hour.getHour() + minute.getMinute() / 60);
        RotateAnimation rotateAnimationHour = new RotateAnimation(
                0, hour.getTotalHour() * 30,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);


        rotateAnimationHour.setDuration(1000);
        rotateAnimationHour.setFillAfter(true);

        imgHour.startAnimation(rotateAnimationHour);
        imgMinute.startAnimation(rotateAnimationMinute);
        imgSecond.startAnimation(rotateAnimationSecond);

    }

    class SecondCountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    performRotation();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Log.e("log", "Error:" + e.toString());
                }
            }
        }
    }

    public void performRotation() {

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    getCurrentTime();

                    RotateAnimation rotateAnimation = new RotateAnimation(
                            (second.getSecond() - 1) * 6, second.getSecond() * 6,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(1000);
                    rotateAnimation.setFillAfter(true);
                    imgSecond.startAnimation(rotateAnimation);

                    if (second.getSecond() <= 0.0) {

                        RotateAnimation rotateAnimationMinute = new RotateAnimation(
                                (minute.getMinute() - 1) * 6, minute.getMinute() * 6,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnimationMinute.setInterpolator(new LinearInterpolator());
                        rotateAnimationMinute.setDuration(1000);
                        rotateAnimationMinute.setFillAfter(true);

                        RotateAnimation rotateAnimationHour = new RotateAnimation(
                                (hour.getHour() + (minute.getMinute() - 1) / 60) * 30, hour.getTotalHour() * 30,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnimationHour.setInterpolator(new LinearInterpolator());
                        rotateAnimationHour.setDuration(1000);
                        rotateAnimationHour.setFillAfter(true);

                        imgMinute.startAnimation(rotateAnimationMinute);
                        imgHour.startAnimation(rotateAnimationHour);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
