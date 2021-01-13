package com.test;

import org.testng.annotations.Test;
import help.application.impl.MainApplication;

public class TestOpenBrowser {

    @Test
    public void testOpenBrowser(){
        new MainApplication().open();
    }
}
