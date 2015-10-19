package com.markjoker.sms.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 */
public class ConversationsFragment extends Fragment
{
    private String mConversationId;
    
    private String mNumber;
    
    private RecyclerView mRecyclerView;
    
    private ConversationAdapter mConversationAdapter;
    
    private List<Map<String, Object>> mData;
    
    public static ConversationsFragment newInstance(String conversionId, String number)
    {
        ConversationsFragment fragment = new ConversationsFragment();
        Bundle args = new Bundle();
        args.putString(Telephony.ThreadsColumns._ID, conversionId);
        args.putString(Telephony.CanonicalAddressesColumns.ADDRESS, number);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mConversationId = getArguments().getString(Telephony.ThreadsColumns._ID);
            mNumber = getArguments().getString(Telephony.CanonicalAddressesColumns.ADDRESS);
        }
        mData = new ArrayList<>();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.rv_conversation);
        mConversationAdapter = new ConversationAdapter();
        mRecyclerView.setAdapter(mConversationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        new LoadDataTask().execute();
        return rootView;
    }
    
    private class LoadDataTask extends AsyncTask<Void, Void, Boolean>
    {
        
        @Override
        protected Boolean doInBackground(Void... params)
        {
            mData.clear();
            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            Cursor cursor = contentResolver.query(uri,
                new String[]{Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE,
                    Telephony.Sms.TYPE},
                Telephony.Sms.THREAD_ID + " = " + mConversationId,
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
                    map.put(Telephony.Sms.BODY, cursor.getString(2));
                    map.put(Telephony.Sms.DATE, cursor.getLong(3));
                    map.put(Telephony.Sms.TYPE, cursor.getInt(4));
                    mData.add(map);
                }
            }
            return true;
        }
        
        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            mConversationAdapter.notifyDataSetChanged();
        }
    }
    
    private class ConversationAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getContext())
                .inflate(viewType == Telephony.Sms.MESSAGE_TYPE_INBOX ? R.layout.layout_conversation_left_item :
                    R.layout.layout_conversation_right_item, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            Map<String, Object> map = mData.get(position);
            holder.timeView.setText(DateUtil.formatToMonthAndDay(new Date((Long)map.get(Telephony.Sms.DATE))));
            holder.contentView.setText((CharSequence)map.get(Telephony.Sms.BODY));
            Drawable drawable = holder.bubbleView.getBackground();
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),
                R.color.material_pink_400), PorterDuff.Mode.SRC_IN));
            holder.bubbleView.setBackgroundDrawable(drawable);
        }
        
        @Override
        public int getItemViewType(int position)
        {
            return (int)mData.get(position).get(Telephony.Sms.TYPE);
        }
        
        @Override
        public int getItemCount()
        {
            return mData.size();
        }
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView thumbView;
        
        private TextView contentView;
        
        private TextView timeView;
        
        private LinearLayout bubbleView;
        
        public ViewHolder(View itemView)
        {
            super(itemView);
            thumbView = (ImageView)itemView.findViewById(R.id.iv_thumb);
            contentView = (TextView)itemView.findViewById(R.id.tv_content);
            timeView = (TextView)itemView.findViewById(R.id.tv_time);
            bubbleView = (LinearLayout)itemView.findViewById(R.id.ll_bubble);
        }
    }
}
