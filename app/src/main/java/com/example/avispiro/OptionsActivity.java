package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Settings");

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
    }

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
