package com.gcr.quartz;

import lombok.SneakyThrows;
import org.quartz.*;

import java.util.Date;

/**
 * Created by GuaiWenWo on 2021/3/6 9:56
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class myJobb implements Job {
    private String name;

    public void setName(String name) {
        this.name = name;
    }


    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("myJob is QU:" + new Date());
//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        JobDataMap jobDataMap1 = context.getTrigger().getJobDataMap();
//        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
//        System.out.println(jobDataMap.get("jbo1"));
//        System.out.println(jobDataMap1.get("jbo1"));
//        System.out.println(mergedJobDataMap.get("jbo1"));
//        System.out.println("name:" + name);
//        System.out.println("测试对象:" + System.identityHashCode(context.getJobDetail()));
//        System.out.println("测试对象2:" + System.identityHashCode(context.getJobInstance()));
        System.out.println("myJob is QU:" + new Date());
        Thread.sleep(3000);
    }
}
