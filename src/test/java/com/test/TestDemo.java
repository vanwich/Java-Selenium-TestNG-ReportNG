package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.testng.annotations.Test;

public class TestDemo {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TestDemo.class);

    public static void testDemo() throws InterruptedException{
        StopWatch stopWatch = new StopWatch("aaa");
        stopWatch.start("task1");
        Thread.sleep(100);
        stopWatch.stop();

        stopWatch.start("task2");
        Thread.sleep(200);
        stopWatch.stop();
       LOGGER.info(stopWatch.prettyPrint());

    }


    public static void main(String[] args) throws InterruptedException{
        testDemo();
    }

    @Test
    public void test() throws InterruptedException{
        testDemo();
    }
}
