package com.campandroid.listviewtest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.widget.AdapterView;



public class mediaListAdapater extends BaseAdapter {

	private Context mContext = null;
    public  ArrayList<ListData> mListData = new ArrayList<ListData>();
    
    public mediaListAdapater(Context mContext) {
        super();
        this.mContext = mContext;
    }
    
    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void addItem(Drawable img, String mTitle, String mDate){
        ListData addInfo = null;
        addInfo = new ListData();
        addInfo.mImage  = img;
        addInfo.mTitle  = mTitle;
        addInfo.mDetail = mDate;
        
        mListData.add(addInfo);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);
            
            holder.mIcon = (ImageView) convertView.findViewById(R.id.imgBanner);
            holder.mText = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.mDate = (TextView) convertView.findViewById(R.id.txtDetail);
            
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        ListData mData = mListData.get(position);
        
        if (mData.mImage != null) {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageDrawable(mData.mImage);
        }else{
            holder.mIcon.setVisibility(View.GONE);
        }
        
        holder.mText.setText(mData.mTitle);
        holder.mDate.setText(mData.mDetail);
        
        return convertView;
    }
    
    class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mDate;
    }
}


