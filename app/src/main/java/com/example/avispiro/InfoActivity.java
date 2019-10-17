package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView descText = (TextView) findViewById(R.id.textDescription);
        TextView nameText = (TextView) findViewById(R.id.textName);
        TextView locationText = (TextView) findViewById(R.id.textLocation);
        TextView timeText = (TextView) findViewById(R.id.textTime);

        descText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        Bird birdSelected = (Bird) intent.getSerializableExtra("wowKey");

        descText.setText(birdSelected.getDescription());
        nameText.setText(birdSelected.getName());
        locationText.setText(birdSelected.getPlace());
        timeText.setText(birdSelected.getTime().toString());

        ImageButton editButton = (ImageButton) findViewById(R.id.buttonEdit);


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
