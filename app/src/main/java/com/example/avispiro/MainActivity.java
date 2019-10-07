package com.example.avispiro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // DEBUG DELETE LATER

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

        // DEBUG
        Drawable drawable = getResources().getDrawable(R.drawable.ic_plus);
        Bitmap icon = Bird.drawableToBitmap(drawable);
        MyDatabaseHelper myD = new MyDatabaseHelper(this, null, null, 1);
        myD.deleteAllBirds("yes");
        Bird osprey = new Bird("Osprey", "A majestic bird", "OBX, NC", "Birds of prey", icon, new Time(2019, 10, 30, 3, 45));
        osprey.setId(myD.addBird(osprey));
        osprey.setImage(icon);
        Drawable d = Bird.bitmapToDrawable(this, myD.getBird(osprey.getId()).getImage());
        d.toString();
        osprey.setName("Osprey 2");
        myD.updateBird(osprey);
        Log.d(TAG, myD.databasetoString());
        Log.d(TAG, "test finish");
        // DEBUG END

        return super.onOptionsItemSelected(item);
    }
}
