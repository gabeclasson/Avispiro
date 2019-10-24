package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBirdActivity extends AppCompatActivity {
    public static final String TAG = "AddBirdActivityLog";
    public static final int RESULT_RETURN_IMG = 7;
    static final int REQUEST_TAKE_PHOTO = 1;

    private static AddBirdActivity currentActivity = null;

    private Bitmap image;
    private Time time;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird);
        image = null;
        time = new Time();
        currentPhotoPath = "";
    }


    @Override
    protected void onResume(){
        super.onResume();
        currentActivity = this;
    }

    /**
     * Adapted from https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
     * and https://developer.android.com/training/camera/photobasics
     * @param reqCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == RESULT_RETURN_IMG) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(imageStream);
                    setImageText("Selected");
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
            }
        }
        if (reqCode == REQUEST_TAKE_PHOTO){
            if (resultCode == RESULT_OK){
                try {
                    image = BitmapFactory.decodeFile(currentPhotoPath);
                    setImageText("From Camera");
                }
                catch (Exception e){
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "You didn't take a photo", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pickTime(View v) {
        time = new Time();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
            DialogFragment newFragment = new TimePickerFragment();
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
        EditText birdTimeEdit = findViewById(R.id.addBirdTimeEdit);
        birdTimeEdit.setText(time.toString());
    }

    public void setImageText(String name){
        EditText birdImageEdit = findViewById(R.id.addBirdImageEdit);
        birdImageEdit.setText(name);
    }

    public void pickImage(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_RETURN_IMG);
    }


    /**
     * https://developer.android.com/training/camera/photobasics
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Adapted from https://developer.android.com/training/camera/photobasics
     * @param v
     */
    public void takeImage(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void addBird(View v){
        Bird bird = new Bird();
        EditText addBirdNameEdit = findViewById(R.id.addBirdNameEdit);
        EditText addBirdDescriptionEdit = findViewById(R.id.addBirdDescriptionEdit);
        EditText addBirdPlaceEdit = findViewById(R.id.addBirdPlaceEdit);
        EditText addBirdCategoryEdit = findViewById(R.id.addBirdCategoryEdit);
        String birdName = addBirdNameEdit.getText().toString().trim();
        String birdDescription = addBirdDescriptionEdit.getText().toString().trim();
        String birdPlace = addBirdPlaceEdit.getText().toString().trim();
        String birdCategory = addBirdCategoryEdit.getText().toString().trim().toLowerCase(); // NOTE: All BIRD CATEGORIES ARE LOWER CASE! CASE IS NOT IMPORTANT.
        Bitmap birdImage = image;
        Time birdTime = time;
        if (birdName.isEmpty()){
            Toast.makeText(this, "You must give your bird a name.", Toast.LENGTH_LONG).show();
            return;
        }
        bird.setName(birdName);
        bird.setDescription(birdDescription);
        bird.setPlace(birdPlace);
        bird.setCategory(birdCategory);
        bird.setImage(birdImage);
        bird.setTime(birdTime);
        bird.setId(MyDatabaseHelper.getInstance(getApplicationContext()).addBird(bird));
        Intent intent = new Intent(this, StartActivity.class);
        Toast.makeText(this, "Bird added.", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
