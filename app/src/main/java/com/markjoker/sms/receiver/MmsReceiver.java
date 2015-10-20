package com.markjoker.sms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

public class MmsReceiver extends BroadcastReceiver
{
    public MmsReceiver()
    {
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (Telephony.Sms.Intents.WAP_PUSH_DELIVER_ACTION.equals(action))
        {
            
        }
    }
}
