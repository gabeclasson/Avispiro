package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryDetailActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "categoryId";
    private int categoryId;
    private AdapterView.OnItemClickListener birdClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_detail);
        Intent intent = getIntent();
        categoryId = intent.getIntExtra(CATEGORY_ID, 1);
        Resources res = getResources();
        if (categoryId == 1)
            findViewById(R.id.categoryDetailButtonEdit).setVisibility(View.INVISIBLE);
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
        intent.putExtra(AddBirdActivity.DEFAULT_CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    public void editCategory(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        final CategoryDetailActivity currentActivity = this;
        builder.setTitle(R.string.edit_category_title_text).setView(dialogView).setPositiveButton(R.string.edit_category_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Update
                EditText editName = dialogView.findViewById(R.id.editNameAddCategory);
                String categoryName = editName.getText().toString();
                if (Category.reduce(categoryName).isEmpty() || Category.reduce(categoryName).equals(Category.UNCATEGORIZED)){
                    Toast.makeText(currentActivity, R.string.edit_category_invalid_message, Toast.LENGTH_LONG);
                    return;
                }
                MyDatabaseHelper.getInstance(getApplicationContext()).updateCategory(new Category(categoryName, categoryId));
                /**
                 * Adapted from: https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
                 * Purpose: Seamless editing of activity information
                 */
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            }
        }).setNegativeButton(R.string.edit_category_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Delete
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(R.string.delete_category_title).setMessage(R.string.delete_category_message).setPositiveButton(R.string.delete_category_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete confirmation button
                        MyDatabaseHelper.getInstance(getApplicationContext()).removeCategory(categoryId);
                        Toast.makeText(currentActivity, R.string.delete_category_success_message, Toast.LENGTH_LONG).show();
                        currentActivity.finish();
                    }
                }).setNegativeButton(R.string.delete_category_negative_button, null); // Delete cancel button
                builder.create().show();
            }
        }).setNeutralButton(R.string.edit_category_neutral_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cancel
            }
        });
        builder.create().show();
        EditText editName = dialogView.findViewById(R.id.editNameAddCategory);
        editName.setText(MyDatabaseHelper.getInstance(getApplicationContext()).getCategory(categoryId).getName());
    }
}
