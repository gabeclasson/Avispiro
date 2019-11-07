package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBirdActivity extends AppCompatActivity {
    public static final String TAG = "AddBirdActivityLog";
    public static final int RESULT_RETURN_IMG = 7;
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    public static final String DEFAULT_CATEGORY_ID = "defaultCategoryId";
    private static AddBirdActivity currentActivity = null;

    private String imageUri;
    private Time time;
    private int defaultCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird);
        imageUri = "";
        time = new Time();
        Intent intent = getIntent();
        defaultCategoryId = intent.getIntExtra(DEFAULT_CATEGORY_ID, 1);
    }


    @Override
    protected void onResume(){
        super.onResume();
        currentActivity = this;
        // get categories
        Category [] categories = MyDatabaseHelper.getInstance(getApplicationContext()).getCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item,  categories);
        Spinner addBirdCategorySpinner = findViewById(R.id.addBirdCategorySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        addBirdCategorySpinner.setAdapter(adapter);
        int i = 0;
        while(i < categories.length && categories[i].getId() != defaultCategoryId){
            i++;
        }
        if (i < categories.length)
            addBirdCategorySpinner.setSelection(i);
    }

    /**
     * Adapted from https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
     * and https://developer.android.com/training/camera/photobasics and https://www.baeldung.com/convert-input-stream-to-a-file
     * After the user chooses/takes a photo, this method is run. It deals with the result of these pickers.
     * @param reqCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == RESULT_RETURN_IMG) { // Chosen an image
            if (resultCode == RESULT_OK) {
                try {
                    //Tries to decode the image and write it to storage
                    Uri imageUriLocal = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUriLocal);
                    byte[] buffer = new byte[imageStream.available()];
                    imageStream.read(buffer);
                    File targetFile = createImageFile();
                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);
                    imageStream.close();
                    outStream.close();
                    this.imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", targetFile).toString();
                    setImageText(getString(R.string.image_selected));
                } catch (Exception e) {
                    Toast.makeText(this, R.string.image_selected_error, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, R.string.image_missing_error, Toast.LENGTH_LONG).show();
            }
        }
        if (reqCode == REQUEST_TAKE_PHOTO){ // take a photo
            if (resultCode == RESULT_OK){
                try {
                    data.getData();
                    setImageText(getString(R.string.photo_taken));
                }
                catch (Exception e){
                    Toast.makeText(this, R.string.image_selected_error, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, R.string.image_missing_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Begins the process of picking a time at which this bird was seen. This method will start a date picker and then a time picker.
     * @param v
     */
    public void pickTime(View v) {
        time = new Time();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Adapted from https://developer.android.com/guide/topics/ui/controls/pickers
     * Class to pick a date
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
     * Class to pick a time
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

    /**
     * Sets the text content of the visible "Time" field within the activity
     * @param time The Time object which represents the time the bird was seen.
     */
    public void setTimeText(Time time){
        EditText birdTimeEdit = findViewById(R.id.addBirdTimeEdit);
        birdTimeEdit.setText(time.toString());
    }

    /**
     * Sets the text content of the visible "Image" field wtihin the activity
     * @param name Some message indicating the nature of the image selected for the user to see.
     */
    public void setImageText(String name){
        EditText birdImageEdit = findViewById(R.id.addBirdImageEdit);
        birdImageEdit.setText(name);
    }

    /**
     * Adapted from https://developer.android.com/training/permissions/requesting.html
     * Begins the process of picking an image from storage
     * @param v
     */
    public void pickImage(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the requests
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_RETURN_IMG);
    }


    /**
     * https://developer.android.com/training/camera/photobasics
     * Creates an image file
     * @return The image file it created
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
        return image;
    }


    /**
     * Adapted from https://developer.android.com/training/camera/photobasics
     * Begins the process of getting an image from the ACTION_IMAGE_CAPTURE intent
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
                Toast.makeText(this, R.string.image_selected_error, Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                imageUri = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     *
     * @param v
     * Purpose: Adds a bird with the given information to the designated category
     */
    public void addBird(View v){
        Bird bird = new Bird();
        EditText addBirdNameEdit = findViewById(R.id.addBirdNameEdit);
        EditText addBirdDescriptionEdit = findViewById(R.id.addBirdDescriptionEdit);
        EditText addBirdPlaceEdit = findViewById(R.id.addBirdPlaceEdit);
        Spinner addBirdCategorySpinner = findViewById(R.id.addBirdCategorySpinner);
        String birdName = addBirdNameEdit.getText().toString().trim();
        String birdDescription = addBirdDescriptionEdit.getText().toString().trim();
        String birdPlace = addBirdPlaceEdit.getText().toString().trim();
        Category birdCategory = (Category) addBirdCategorySpinner.getSelectedItem();
        String birdImage = imageUri;
        Time birdTime = time;
        if (birdName.isEmpty()){
            Toast.makeText(this, R.string.bird_added_error, Toast.LENGTH_LONG).show();
            return;
        }
        bird.setName(birdName);
        bird.setDescription(birdDescription);
        bird.setPlace(birdPlace);
        bird.setCategory(birdCategory);
        bird.setImageURI(birdImage);
        bird.setTime(birdTime);
        bird.setId(MyDatabaseHelper.getInstance(getApplicationContext()).addBird(bird));
        Intent intent = new Intent(AddBirdActivity.this, StartActivity.class);
        Toast.makeText(this, R.string.bird_added_success, Toast.LENGTH_LONG).show();
        finish();
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
