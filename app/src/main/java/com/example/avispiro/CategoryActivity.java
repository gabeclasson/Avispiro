package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_categories));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCategoryList();
    }

    public void updateCategoryList(){
        final Category[] categories = MyDatabaseHelper.getInstance(getApplicationContext()).getCategories();
        ArrayAdapter<Category> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);

        ListView listCategories = (ListView) findViewById(R.id.listViewCategories);
        listCategories.setAdapter(listAdapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CategoryActivity.this, CategoryDetailActivity.class);
                int categoryId = categories[i].getId();
                intent.putExtra(CategoryDetailActivity.CATEGORY_ID, categoryId);
                startActivity(intent);
            }
        };

        listCategories.setOnItemClickListener(itemClickListener);
    }

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
