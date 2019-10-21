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
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    public static final String BIRD_ID = "birdId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView descText = (TextView) findViewById(R.id.textDescription);
        TextView nameText = (TextView) findViewById(R.id.textName);
        TextView locationText = (TextView) findViewById(R.id.textLocation);
        TextView timeText = (TextView) findViewById(R.id.textTime);
        ImageView imageBird = (ImageView) findViewById(R.id.imageBird);

        /**
         *  Source: https://stackoverflow.com/questions/1748977/making-textview-scrollable-on-android
         *  Purpose: Scrolling textview
         */
        descText.setMovementMethod(new ScrollingMovementMethod());

        /**
         * Source: https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
         * Purpose: Serializable, to pass objects through intent
         */
        Intent intent = getIntent();
        int birdId = intent.getIntExtra(BIRD_ID, 0);
        MyDatabaseHelper helper = new MyDatabaseHelper(this, null, null, 0);
        Bird birdSelected = helper.getBird(birdId);

        descText.setText(birdSelected.getDescription());
        nameText.setText(birdSelected.getName());
        locationText.setText(birdSelected.getPlace());
        timeText.setText(birdSelected.getTime().toString());
        imageBird.setImageDrawable(birdSelected.bitmapToDrawable(this, birdSelected.getImage()));

        ImageButton editButton = (ImageButton) findViewById(R.id.buttonEdit);


    }

    /**
     * Source: https://www.mkyong.com/android/android-custom-dialog-example/
     * @param v
     */
    public void onClick(View v){

        // import custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogue_edit);
        dialog.setTitle("Edit Profile");

        // configure views in dialog like usual
        EditText nameEdit = (EditText) findViewById(R.id.editName);
        EditText descEdit = (EditText) findViewById(R.id.editDesc);
        EditText timeEdit = (EditText) findViewById(R.id.editTime);
        EditText locationEdit = (EditText) findViewById(R.id.editPlace);


        //show the dialog
        dialog.show();
    }
}
