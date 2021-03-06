package com.gcr.quartz;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by GuaiWenWo on 2021/3/6 15:20
 */
@Component
public class testSchedule implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //taskRegistrar1 = taskRegistrar;
//        String cron = "0/1 * * * * ?";
//        taskRegistrar.addCronTask(() -> {
//            //System.out.println("测试动态执行1：" + new Date());
//        }, cron);
//
//        taskRegistrar.addCronTask(() -> {
//            //System.out.println("测试动态执行2：" + new Date());
//        }, getTIme());
//
//        List<CronTask> cronTaskList = taskRegistrar.getCronTaskList();
//        cronTaskList.forEach(item -> {
//            System.out.println(item + item.getExpression());
//
//        });


        taskRegistrar.addTriggerTask(() -> {
            System.out.println("执行测试:" + new Date());
        }, triggerContext -> {
            //2.1 从数据库获取执行周期
            String cron = getTimer();// "0/1 * * * * ?";
            //2.2 合法性校验.
            if (StringUtils.isEmpty(cron)) {
                // Omitted Code ..
            }
            //2.3 返回执行周期(Date)
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });

    }

    //测试定时器
    private Boolean isTest = true;

    private String getTimer() {
        if (isTest) {
            isTest = false;
            return "0/1 * * * * ?";
        }
        isTest = true;
        return "0/5 * * * * ?";
    }

}


