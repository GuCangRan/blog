package com.gcr;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
class DemoApplicationTests {
    //Logger log = LoggerFactory.getLogger(getClass());

    @Test
    void contextLoads() {
        log.info("haaaa23232344444");
        log.error("sdsfsfsdfq2342342342344444");
    }

}
