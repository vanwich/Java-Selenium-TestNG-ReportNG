/*
 * Copyright © 2019 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package help.utils.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lgu2 on 11/28/2019.
 */
public class DateUtil {
    public static void main(String[] args){
        System.out.println(getNewDate(-7));
    }

    public static String getNow(String format){
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }

    public static String getNewDate(int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, days);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
    }

    public static String getTimestamp(){
        String curDate = getNow("yyyy-MM-dd");
        Calendar curCalendar = Calendar.getInstance();
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);//24H
        int minute = curCalendar.get(Calendar.MINUTE);
        int second = curCalendar.get(Calendar.SECOND);

        return curDate + "_" + hour + "_" + minute + "_" + second;
    }
}
