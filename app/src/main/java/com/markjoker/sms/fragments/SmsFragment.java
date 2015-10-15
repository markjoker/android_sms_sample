package com.markjoker.sms.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Telephony;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjoker.sms.R;
import com.markjoker.sms.utils.DateUtil;
import com.markjoker.sms.views.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmsFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    
    private static final String ARG_PARAM2 = "param2";
    
    // TODO: Rename and change types of parameters
    private String mParam1;
    
    private String mParam2;
    
    private List<Map<String, Object>> mData;
    
    private RecyclerView mSmsListView;
    
    private SmsItemAdapter mAdapter;
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsFragment newInstance(String param1, String param2)
    {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    
    public SmsFragment()
    {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mData = new ArrayList<>();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
        mSmsListView = (RecyclerView)rootView.findViewById(R.id.rv_items);
        mAdapter = new SmsItemAdapter();
        mSmsListView.setAdapter(mAdapter);
        mSmsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSmsListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        new LoadDataTask().execute();
        return rootView;
    }
    
    private class LoadDataTask extends AsyncTask<Void, Integer, Boolean>
    {
        
        @Override
        protected Boolean doInBackground(Void... params)
        {
            mData.clear();
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),
                new String[]{Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.DATE, Telephony.Sms.BODY,
                    Telephony.Sms.TYPE},
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER);
            if (cursor != null && cursor.getCount() > 0)
            {
                Map<String, Object> map = null;
                while (cursor.moveToNext())
                {
                    map = new HashMap<>();
                    map.put(Telephony.Sms._ID, cursor.getString(0));
                    map.put(Telephony.Sms.ADDRESS, cursor.getString(1));
                    map.put(Telephony.Sms.DATE, cursor.getLong(2));
                    map.put(Telephony.Sms.BODY, cursor.getString(3));
                    map.put(Telephony.Sms.TYPE, cursor.getInt(4));
                    map.put(Telephony.Threads.SNIPPET, cursor.getString(0));
                    mData.add(map);
                }
            }
            return true;
        }
        
        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (result)
            {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    
    private class SmsItemAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sms_item, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            Map<String, Object> map = mData.get(position);
            holder.mNameView.setText((String)map.get(Telephony.Sms.ADDRESS));
            holder.mContentView.setText((String)map.get(Telephony.Sms.BODY));
            holder.mTimeView.setText(DateUtil.formatToMonthAndDay(new Date((Long)map.get(Telephony.Sms.DATE))));
        }
        
        @Override
        public int getItemCount()
        {
            return mData.size();
        }
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mThumbView;
        
        private TextView mNameView;
        
        private TextView mContentView;
        
        private TextView mTimeView;
        
        public ViewHolder(View itemView)
        {
            super(itemView);
            mThumbView = (ImageView)itemView.findViewById(R.id.iv_thumb);
            mNameView = (TextView)itemView.findViewById(R.id.tv_name);
            mContentView = (TextView)itemView.findViewById(R.id.tv_content);
            mTimeView = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }
}
