/*
 * Copyright © 2019 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package help.utils.base;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import help.config.LogConfig;
import help.utils.log.LogUtil;
import help.utils.report.TestReport;
import help.utils.time.DateUtil;

/**
 * Created by lgu2 on 11/29/2019.
 */
public abstract class BaseCase {
    public static LogConfig logConfig;
    public static LogUtil Log ;

    private int retryCounter = 0;

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("BaseCase: beforeSuite");
        initConfig();
        TestReport.startTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
        TestReport.startMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("BaseCase: afterSuite");
        Log.close();
        TestReport.endTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
        TestReport.endMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("case总数--->" + TestReport.caseCount);
        System.out.println("fail个数--->"+TestReport.failureCount);
        System.out.println("success个数--->"+TestReport.successCount);
        System.out.println("skip个数--->"+TestReport.skipedCount);
        if(! LogConfig.receivers.equals("")){
            new TestReport(LogConfig.receivers, LogConfig.subject).sendReport();
        }

    }

    @BeforeClass
    public void beforeClass(){
        System.out.println("BaseCase: beforeClass");
        if(logConfig == null){
            System.err.println("init log config failed, pls overwrite initConfig().");
            return;
        }
        if(LogConfig.logType == 0){
            Log = new LogUtil(logConfig, this.getClass().getSimpleName());
        }else{
            Log = new LogUtil(logConfig, this.getClass());
        }
    }

    @AfterClass
    public void afterClass(){
        System.out.println("BaseCase: afterClass");
        Log.commit();
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("BaseCase: beforeMethod");
        Log.initStat();
        TestReport.caseCount++;
        Log.info("==============测试执行开始==============");
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        System.out.println("BaseCase: afterMethod");
        String testClassName = String.format("%s.%s", result.getMethod()
                .getRealClass().getName().toString(), result.getMethod().getMethodName());
        System.out.println("---->"+testClassName);
        if (!result.isSuccess()) {
            Log.fail(result.getThrowable());
        }
        Log.info("==============测试执行完毕==============");
        if(Log.isPass()){
            TestReport.successCount ++;
        }else{
            if(LogConfig.retryTimes > 0){
                try {
                    Class<?> c = Class.forName(result.getMethod().getRealClass().getName());
                    Method m =   c.getMethod(result.getMethod().getMethodName());
                    while(retryCounter < LogConfig.retryTimes){
                        Log.flush();
                        Log.info("<<<<<<<<<<<<<<<<<< 开始重新执行\""+ result.getMethod().getMethodName() +"\"方法 >>>>>>>>>>>>>>>>>>");
                        m.invoke(c.newInstance(), (Object[])null);
                        Log.info("<<<<<<<<<<<<<<<<<< 重新执行\""+ result.getMethod().getMethodName() +"\"方法完毕 >>>>>>>>>>>>>>>>>>");
                        retryCounter ++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(retryCounter >= LogConfig.retryTimes) {
                    retryCounter = 0;//初始化
                    TestReport.failureCount ++;
                }
            }else{
                TestReport.failureCount ++;
            }
        }

        Log.commit();
    }


    public abstract void initConfig();


    public boolean assertEquals(Object actual, Object expected, boolean isPrintLog){
        try {
            Assert.assertEquals(actual, expected);
            if(isPrintLog){
                Log.info("实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 相匹配");
            }
            return true;
        } catch (Error e) {
            Log.error("实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 不匹配");
            Log.errorCount++;
            return false;
        }
    }

    public boolean assertEquals(Object actual, Object expected, String msg){
        try {
            Assert.assertEquals(actual, expected);
            Log.info("实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 相匹配");
            return true;
        } catch (Error e) {
            Log.error("实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 相匹配");
            Log.errorCount++;
            return false;
        }
    }
}
