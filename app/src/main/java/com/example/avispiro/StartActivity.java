package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Resources res = getResources();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // testing listviewAdapter
        String[] nameList = {"hi", "bye"};
        String[] dateList = {"01-01-01", "02-02-02"};
        Drawable[] imageList = {ResourcesCompat.getDrawable(res, R.drawable.winter_solstice,null),
                ResourcesCompat.getDrawable(res,R.drawable.ic_launcher_foreground, null)};

        ListView listView = (ListView) findViewById(R.id.listView);
        ListviewAdapter adapter = new ListviewAdapter(getApplicationContext(), nameList, dateList, imageList);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener birdClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StartActivity.this, InfoActivity.class);
                // intent.putExtra(InfoActivity.BIRD_SELECTED,(int) l)
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(birdClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addBirdOnClick(View v){
        Intent intent = new Intent(StartActivity.this, AddBirdActivity.class);
        startActivity(intent);
    }
}
