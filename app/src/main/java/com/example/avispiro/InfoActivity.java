package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    public static final String BIRD_SELECTED = "bird";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView descText = (TextView) findViewById(R.id.textDescription);
        descText.setMovementMethod(new ScrollingMovementMethod());

        //Testing purposes for dialogue boxes
        ImageButton testButton = (ImageButton) findViewById(R.id.buttonEdit);
    }

    public void onClick(View v){

        // import custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogue_edit);
        dialog.setTitle("Edit Profile");

        // configure views in dialog like usual
        EditText nameEdit = (EditText) findViewById(R.id.editName);
        EditText descEdit = (EditText) findViewById(R.id.editDesc);
        EditText timeEdit = (EditText) findViewById(R.id.editTime);
        EditText locationEdit = (EditText) findViewById(R.id.editLocation);


        //show the dialog
        dialog.show();
    }
}
