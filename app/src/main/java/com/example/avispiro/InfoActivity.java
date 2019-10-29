package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class InfoActivity extends AppCompatActivity {

    public static final String BIRD_ID = "birdId";
    private Bird birdSelected ;
    private static InfoActivity currentActivity = null;
    private Time time;
    private View dialogView;

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
        birdSelected = MyDatabaseHelper.getInstance(getApplicationContext()).getBird(birdId);
        time = birdSelected.getTime();
        descText.setText(birdSelected.getDescription());
        nameText.setText(birdSelected.getName());
        locationText.setText(birdSelected.getPlace());
        timeText.setText(birdSelected.getTime().toString());
        imageBird.setImageDrawable(birdSelected.bitmapToDrawable(this, birdSelected.getImage(this)));
        ImageButton editButton = (ImageButton) findViewById(R.id.buttonEdit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    /**
     * Adapted from https://developer.android.com/guide/topics/ui/dialogs
     * Begins bird edit dialog
     * @param v
     */
    public void onClick(View v){
        // building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialogue_edit, null);
        // final parameters for passing to internal class
        final EditText nameEdit = (EditText) dialogView.findViewById(R.id.editName);
        final EditText descEdit = (EditText) dialogView.findViewById(R.id.editDesc);
        final EditText timeEdit = (EditText) dialogView.findViewById(R.id.editTime);
        final EditText placeEdit = (EditText) dialogView.findViewById(R.id.editPlace);
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, MyDatabaseHelper.getInstance(getApplicationContext()).getCategories());
        final Spinner categorySpinner = dialogView.findViewById(R.id.spinnerCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        categorySpinner.setAdapter(adapter);
        final InfoActivity currentActivityParam = this;
        final Time timeParam = time;
        final Bird birdSelectedParam = birdSelected;
        // customizing dialog
        builder.setTitle(R.string.edit_bird_title_text).setView(dialogView).setPositiveButton(R.string.edit_bird_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Update bird button
                String name = nameEdit.getText().toString().trim();
                String desc = descEdit.getText().toString().trim();
                String place = placeEdit.getText().toString().trim();
                Category category = (Category) categorySpinner.getSelectedItem();
                if (name.isEmpty()) {
                    name = birdSelectedParam.getName();
                }
                birdSelectedParam.setName(name);
                birdSelectedParam.setDescription(desc);
                birdSelectedParam.setPlace(place);
                birdSelectedParam.setCategory(category);
                birdSelectedParam.setTime(timeParam);
                MyDatabaseHelper.getInstance(getApplicationContext()).updateBird(birdSelectedParam);
            }
        }).setNegativeButton(R.string.edit_bird_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Delete button
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivityParam);
                builder.setTitle(R.string.delete_bird_title).setMessage(R.string.delete_bird_message).setPositiveButton(R.string.delete_bird_positive_button, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete confirmation button
                        MyDatabaseHelper.getInstance(getApplicationContext()).removeBird(birdSelectedParam.getId());
                        Intent intent = new Intent(currentActivityParam, StartActivity.class);
                        Toast.makeText(currentActivityParam, "Bird deleted.", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.delete_bird_negative_button, null); // Delete cancel button
                builder.create().show();
            }
        }).setNeutralButton(R.string.edit_bird_neutral_button, null); // Edit cancel button
        builder.create().show();
        // Start dialog with fields prefilled in with bird's information
        nameEdit.setText(birdSelected.getName());
        descEdit.setText(birdSelected.getDescription());
        timeEdit.setText(birdSelected.getTime().toString());
        placeEdit.setText(birdSelected.getPlace());
        categorySpinner.setSelection(Category.search(adapter, birdSelected.getCategory().getName()));
    }

    public void onButtonClick(View v){
        Intent intent = new Intent(InfoActivity.this, PictureActivity.class);
        intent.putExtra(PictureActivity.CURRENT_BIRD_ID, birdSelected.getId());
        startActivity(intent);
    }

    /**
     * Adapted from https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int date){
            Time tempTime = currentActivity.getTime();
            tempTime.setYear(year);
            tempTime.setMonth(month);
            tempTime.setDate(date);
            DialogFragment newFragment = new InfoActivity.TimePickerFragment();
            newFragment.show(currentActivity.getSupportFragmentManager(), "timePicker");
        }
    }

    /**
     * Pickers adapted from https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time tempTime = currentActivity.getTime();
            tempTime.setHour(hourOfDay);
            tempTime.setMinute(minute);
        }

        public void onDestroy(){
            super.onDestroy();
            currentActivity.setTimeText(currentActivity.getTime());
        }
    }

    public void setTimeText(Time time){
        EditText editTime = dialogView.findViewById(R.id.editTime);
        editTime.setText(time.toString());
    }

    public void pickTimeEdit(View v) {
        DialogFragment newFragment = new InfoActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
