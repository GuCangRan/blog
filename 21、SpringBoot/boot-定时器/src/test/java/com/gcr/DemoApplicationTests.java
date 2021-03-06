package com.gcr;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


//@SpringBootTest
@Log4j2
class DemoApplicationTests {
    //Logger log = LoggerFactory.getLogger(getClass());

    @Test
    void contextLoads() {
        //log.info("haaaa23232344444");
        //log.error("sdsfsfsdfq2342342342344444");
        String str1 = new String("hello");
        String str2 = new String("hello");
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        System.out.println(str1 == str2);
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));
    }

}
