package com.example.avispiro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {
    public static CategoryActivity currentInstance = null;
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
        currentInstance = this;
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

    public void addCategoryOnClick(View v){
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.show(getSupportFragmentManager(), "dialogaddcategory");
    }

    public static class AddCategoryDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final CategoryActivity currentActivityParam = currentInstance;
            final View dialogView = currentActivityParam.getLayoutInflater().inflate(R.layout.dialog_add_category, null);
            builder.setMessage(R.string.add_category_dialog_message).setView(dialogView).setPositiveButton(R.string.add_category_positive_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Add the category
                    String name = ((EditText) dialogView.findViewById(R.id.editNameAddCategory)).getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(currentActivityParam, "Your category must have a name.", Toast.LENGTH_LONG).show();
                    return;
                    }
                    MyDatabaseHelper.getInstance(currentActivityParam.getApplicationContext()).addCategory(new Category(name));
                    currentActivityParam.updateCategoryList();
                    Toast.makeText(currentActivityParam, "Category added.", Toast.LENGTH_LONG).show();
                }
            }).setNegativeButton(R.string.add_category_negative_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Cancel
                        }
                    });
            return builder.create();
        }
    }

}
