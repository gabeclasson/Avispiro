package com.example.avispiro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {

    /**
     *  Source: https://abhiandroid.com/ui/listview
     *  Purpose: Making custom Listview adapter
     */
    private Bird[] birdArray;
    private LayoutInflater inflater;

    public ListviewAdapter(Context applicationContext, Bird[] birdArray) {
        this.birdArray = birdArray;
        inflater = LayoutInflater.from(applicationContext);
    }

    public int getCount(){
        return birdArray.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup){
        view = inflater.inflate(R.layout.listview_layout, null);
        ImageView image = (ImageView) view.findViewById(R.id.imageBird);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
        nameText.setText(birdArray[i].getName());
        timeText.setText(birdArray[i].getTime().toString());

        /**
         *  Source: https://stackoverflow.com/questions/26850780/bitmap-circular-crop-in-android
         *  Purpose: Converting bitmap to drawable and rounding its corners
         */
        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), birdArray[i].getImage());

        image.setImageDrawable(imageDrawable);
        return view;
    }
}
