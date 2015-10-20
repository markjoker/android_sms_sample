package com.markjoker.sms.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class AppUtils
{
    
    private AppUtils()
    {
        /**cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
        
    }
    
    public static String getPkgName(Context ctx)
    {
        return ctx.getPackageName();
    }
    
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
            
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static int getVersionCode(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
            
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static boolean hasHoneycomb()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    
    public static boolean hasM()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    
    public static boolean hasHoneycombMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    
    public static boolean hasKitkat()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    
    public static boolean hasLollipop()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    
    public static String getMetaValue(Context context, String metaKey)
    {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null)
        {
            return null;
        }
        try
        {
            ApplicationInfo ai =
                context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
            {
                metaData = ai.metaData;
            }
            if (null != metaData)
            {
                apiKey = metaData.getString(metaKey);
            }
        }
        catch (NameNotFoundException e)
        {
            apiKey = null;
            e.printStackTrace();
        }
        return apiKey;
    }
    
    public static String getIMEI(Context ctx)
    {
        TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    
    public static String getIMSI(Context ctx)
    {
        TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }
}
