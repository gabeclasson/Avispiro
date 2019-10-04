package com.example.avispiro;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListviewAdapter extends BaseAdapter {

    private String[] nameArray;
    private String[] timeArray;
    private int[] imageArray;
    private LayoutInflater inflater;

    public ListviewAdapter(Context applicationContext, String[] nameArray, String[] timeArray, int[] imageArray) {
        this.nameArray = nameArray;
        this.timeArray = timeArray;
        this.imageArray = imageArray;
        inflater = LayoutInflater.from(applicationContext);
    }

    public int getCount(){
        return nameArray.length;
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
        nameText.setText(nameArray[i]);
        timeText.setText(timeArray[i]);
        image.setImageResource(imageArray[i]);
        return view;
    }
}
