package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    public static final int RESULT_RETURN_IMG = 7;
    static final int REQUEST_TAKE_PHOTO = 1;

    private ImageButton imageButton = (ImageButton) findViewById(R.id.imageChosen);
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        currentPhotoPath = "";
    }

    /**
     * Source:http://www.apnatutorials.com/android/using-popup-menu-as-options-menu.php?categoryId=2&subCategoryId=30&myPath=android/using-popup-menu-as-options-menu.php
     * Purpose: Force menu to show icons using an unnecessarily complicated process
     * @param view
     */
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(PictureActivity.this, view);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.menu_picture, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            //Feel free to replace the arbitrary things
            case R.id.choiceSave:
                System.out.println("Arb");
                break;

            case R.id.choiceShare:
                Intent intent = new Intent(Intent.ACTION_SEND);
                
                intent.setType("image/jpg");
                break;

            case R.id.choiceTake:
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
                break;

            case R.id.choiceChoose:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_RETURN_IMG);
                break;
        }
        return false;
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
        Bitmap image;
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == RESULT_RETURN_IMG) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(imageStream);
                    imageButton.setImageBitmap(image);
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
                    imageButton.setImageBitmap(image);
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
}
