package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TestLog {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TestLog.class);
    protected static final String STEP = "STEP #{}";

    @Test
    public void testLog(){
        LOGGER.info(STEP, 1);
    }
}
