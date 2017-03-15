package com.example.lars_peter.babydonthurtme;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



public class ImageAdapter extends BaseAdapter {

    private Context mContext;


    public ImageAdapter(Context c) {
        mContext = c;

    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        imageView.setTag(R.drawable.a1);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1,
    };
}