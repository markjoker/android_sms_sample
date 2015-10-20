package com.markjoker.sms.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment
{
    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";
    private BroadcastReceiver receiver;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if(SENT.equals(action))
                {
                    onSend(getResultCode(), intent);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(SENT);
        getActivity().registerReceiver(receiver, filter);
    }
    protected void onSend(int code, Intent intent)
    {
        
    }
    protected void onDeliver(int code)
    {
        
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
