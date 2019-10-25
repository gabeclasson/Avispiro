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

import java.io.Serializable;
import java.util.List;

public class StartActivity extends AppCompatActivity implements Serializable {
    private AdapterView.OnItemClickListener birdClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        Resources res = getResources();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.onCreate(savedInstanceState);
    }

    public void updateBirdList(){
        final Bird[] listBirds = MyDatabaseHelper.getInstance(getApplicationContext()).getAllBirds();
        final ListView listView = (ListView) findViewById(R.id.listView);

        AdapterView.OnItemClickListener birdClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StartActivity.this, InfoActivity.class);
                int birdId = listBirds[i].getId();
                intent.putExtra(InfoActivity.BIRD_ID, birdId);
                startActivity(intent);
            }
        };
        ListviewAdapter adapter = new ListviewAdapter(getApplicationContext(), listBirds);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(birdClickListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateBirdList();
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
