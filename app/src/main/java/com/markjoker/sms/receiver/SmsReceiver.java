package com.markjoker.sms.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver
{
    public SmsReceiver()
    {
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (Telephony.Sms.Intents.SMS_DELIVER_ACTION.equals(intent.getAction()))
        {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msgBody;
            String msgFrom;
            long timeStamp;
            if (bundle != null)
            {
                Object[] pdus = (Object[])bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++)
                {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    msgFrom = msgs[i].getOriginatingAddress();
                    timeStamp = msgs[i].getTimestampMillis();
                    msgBody = msgs[i].getMessageBody();
                    saveSms(context, msgs[i]);
                    Log.d("1021", msgFrom + "," + msgBody + "," + timeStamp);
                }
            }
        }
    }
    private void saveSms(Context context, SmsMessage smsMessage)
    {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS, smsMessage.getOriginatingAddress());
        values.put(Telephony.Sms.DATE, smsMessage.getTimestampMillis());
        values.put(Telephony.Sms.READ, 0);
        values.put(Telephony.Sms.BODY, smsMessage.getMessageBody());
        resolver.insert(Uri.parse("content://sms"),values);
    }
}
