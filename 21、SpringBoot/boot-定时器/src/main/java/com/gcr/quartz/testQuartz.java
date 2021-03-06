package com.gcr.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.Executors;

/**
 * Created by GuaiWenWo on 2021/3/6 10:00
 */
public class testQuartz {
    public static void main(String[] args) throws SchedulerException {
        JobDetail jd = JobBuilder.newJob(myJobb.class)
                .withIdentity("abc", "groud")
                .usingJobData("jbo1", "jbodetial333")
                .usingJobData("name", "name1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("tr", "tr")
                .usingJobData("jbo1", "jbodetial22")
                .usingJobData("name", "name2")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();


        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jd, trigger);
        scheduler.start();
    }
}
