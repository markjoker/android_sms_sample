package com.markjoker.sms.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadlessSmsSendService extends Service
{
    public HeadlessSmsSendService()
    {
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
