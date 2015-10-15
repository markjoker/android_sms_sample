package com.markjoker.sms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MaJian on 2015/10/15.
 */
public class DateUtil
{
    private static SimpleDateFormat mdFormat = new SimpleDateFormat("MM-dd");
    
    public static String formatToMonthAndDay(Date date)
    {
        if (null == date)
        {
            return null;
        }
        return mdFormat.format(date);
    }
}
