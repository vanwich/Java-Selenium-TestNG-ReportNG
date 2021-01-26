/*
 * Copyright © 2019 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package help.utils.base;

import help.config.LogConfig;

/**
 * Created by lgu2 on 11/29/2019.
 */
public class BaseCase1 extends BaseCase {

    //实现BaseCase的initConfig()方法
    @Override
    public void initConfig() {
        // TODO Auto-generated method stub
        logConfig = new LogConfig.Builder("D:\\aa") //设置共享目录
                .setLogType(0)      //设置日志类型
                .setRetryTimes(1) //设置失败重跑次数
                .setSmtpHost("smtp.qq.com") //设置发送邮箱的smtp host
                .setSender("glx1175411@vip.qq.com") //设置发送邮箱的账号
                .setSendPassword("g@870724") //设置发送邮箱的账号密码
                .setReceivers("glx1175411@vip.qq.com") //邮件接收者账号，多个账号用分号隔开
                .setSubject("Test Log4Reports") //邮件主题
                .build();
        System.out.println("init log config finished.");
    }
}
