package com.gcr.Timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by GuaiWenWo on 2021/3/6 9:25
 */
public class testTimer {

    public static void main222(String[] args) {
        Timer myTImer = new Timer();
        myTImer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                    }
                },
                1000, 1000
        );
    }

    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 100; i++) {
            
        }

    }
}
