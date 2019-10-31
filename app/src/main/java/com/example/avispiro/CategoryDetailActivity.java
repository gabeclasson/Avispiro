package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CategoryDetailActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "categoryId";
    private AdapterView.OnItemClickListener birdClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        Resources res = getResources();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PLACEHOLDER");
        super.onCreate(savedInstanceState);
    }

    public void updateBirdList(){
        final Bird[] listBirds = MyDatabaseHelper.getInstance(getApplicationContext()).getAllBirdsInCategory();
        final ListView listView = (ListView) findViewById(R.id.listView);

        AdapterView.OnItemClickListener birdClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CategoryDetailActivity.this, InfoActivity.class);
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
        updateBirdList();
        super.onResume();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_categories){
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_my_birds){
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addBirdOnClick(View v){
        Intent intent = new Intent(this, AddBirdActivity.class);
        startActivity(intent);
    }
}
