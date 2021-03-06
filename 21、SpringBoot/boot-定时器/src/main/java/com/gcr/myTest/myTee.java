package com.gcr.myTest;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by GuaiWenWo on 2021/2/18 16:24
 */
@Log4j2
public class myTee {
    //static Logger logger = LoggerFactory.getLogger(myTee.class);
    static Logger logger = LoggerFactory.getLogger(myTee.class);

    public static void main(String[] args) {
        log.info("haaaa2222222222222");
        log.error("sdsfsfsdf222222222222222222");
        //日志的级别  低 到 高
        //可以调整输出的日志级别，日志就只会在这个级别以后的高级别生效
        logger.trace("这是trace日志....");
        logger.debug("这是debug日志...");
        //SpringBoot默认给我们使用的是info级别的，没有指定级别的就用SpringBoot默认级别
        logger.info("这是info日志...");
        logger.warn("这是warn日志...");
        logger.error("这是error日志...");

        //Collections.synchronizedMap()
        //UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "pwd");

        
    }

}
