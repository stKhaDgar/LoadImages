package com.example.stmak.loadimages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomGridAdapter extends BaseAdapter{
    private Context context;

    public CustomGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;


        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            customView = li.inflate(R.layout.grid_photos_item, null);
        }

        if(position == 0){
            customView.findViewById(R.id.item_button_view).setVisibility(View.VISIBLE);
            customView.findViewById(R.id.item_image_view).setVisibility(View.VISIBLE);
            customView.findViewById(R.id.item_photo_view).setVisibility(View.VISIBLE);
        }
        return customView;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public boolean isEnabled(int pos){
        if(pos == 0 ){
            return true;
        }
        return false;
    }
}