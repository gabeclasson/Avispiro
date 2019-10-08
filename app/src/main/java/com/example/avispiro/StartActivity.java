package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Resources res = getResources();

        String[] nameList = {"hi"};
        String[] dateList = {"01-01-01"};
        Drawable[] imageList = {ResourcesCompat.getDrawable(res, R.drawable.winter_solstice,null)};
        ListView listView = (ListView) findViewById(R.id.listView);
        ListviewAdapter adapter = new ListviewAdapter(getApplicationContext(), nameList, dateList, imageList);
        listView.setAdapter(adapter);
    }
}
