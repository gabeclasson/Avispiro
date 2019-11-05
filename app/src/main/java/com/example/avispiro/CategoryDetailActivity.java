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
import android.widget.TextView;

public class CategoryDetailActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "categoryId";
    private int categoryId;
    private AdapterView.OnItemClickListener birdClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_detail);
        Intent intent = getIntent();
        categoryId = intent.getIntExtra(CATEGORY_ID, 0);
        Resources res = getResources();
        super.onCreate(savedInstanceState);
    }

    public void updateBirdList(){
        final Bird[] listBirds = MyDatabaseHelper.getInstance(getApplicationContext()).getAllBirdsInCategory(categoryId);
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
        TextView categoryName = findViewById(R.id.categoryDetailTextName);
        categoryName.setText(MyDatabaseHelper.getInstance(getApplicationContext()).getCategory(categoryId).getName());
    }

    @Override
    protected void onResume(){
        updateBirdList();
        super.onResume();
    }


    public void addBirdOnClick(View v){
        Intent intent = new Intent(this, AddBirdActivity.class);
        startActivity(intent);
    }
}
