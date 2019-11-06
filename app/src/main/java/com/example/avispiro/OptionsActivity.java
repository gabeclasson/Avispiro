package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_settings);

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
    }

    /**
     *
     * @param item
     * @return boolean
     * Purpose: Sets up the toobar menu, which was here by default when project was created
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_categories){
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_my_birds){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param v
     * @return: none
     * Purpose: gone. reduced to atoms.
     */
    public void deleteOnClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_bird_title).setMessage(R.string.delete_bird_message).setPositiveButton(R.string.delete_bird_positive_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete confirmation button
                MyDatabaseHelper.getInstance(getApplicationContext()).deleteAllBirds(getApplicationContext());
                Intent intent = new Intent(OptionsActivity.this, StartActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton(R.string.delete_bird_negative_button, null); // Delete cancel button
        builder.create().show();
    }
}
