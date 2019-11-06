package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureActivity extends AppCompatActivity{
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    public static final int RESULT_RETURN_IMG = 7;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Bird selectedBird;
    private int birdID;

    public static final String CURRENT_BIRD_ID = "birdID";

    private String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        birdID = intent.getIntExtra(CURRENT_BIRD_ID, 0);
        imageUri = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedBird = MyDatabaseHelper.getInstance(getApplicationContext()).getBird(birdID);
        updateImage();
    }

    public void updateImage(){
        ImageButton imageButton = (ImageButton) findViewById(R.id.chosenImage);
        if(selectedBird.getImage(this) != null) {
            imageButton.setImageBitmap(selectedBird.getImage(this));
            ImageView dogImage = (ImageView) findViewById(R.id.imageDefault2);
            TextView dogText = (TextView) findViewById(R.id.dogText2);
            dogImage.setVisibility(View.INVISIBLE);
            dogText.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickPopup(View v){
        showPopup(v, R.style.Widget_AppCompat_Light_PopupMenu);
    }

    /**
     * Source: https://www.androhub.com/android-popup-menu/
     * Purpose: Force menu to show icons using an unnecessarily complicated process
     * @param view
     */
    public void showPopup(View view, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, view);

        /*  The below code in try catch is responsible to display icons*/
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.menu_picture, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return PictureActivity.this.onMenuItemClick(menuItem);
            }
        });

        popup.show();
    }

    /**
     * @param item
     * @return boolean
     * Purpose: The menu for when the menu button is clicked. Allows the user to share the photo, take a new photo, or
     *          choose a new one.
     */
    public boolean onMenuItemClick(MenuItem item){
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this, null, null, 0);
        Intent intent = getIntent();
        int birdID = intent.getIntExtra(CURRENT_BIRD_ID, 0);
        Bird birdSelected = MyDatabaseHelper.getInstance(getApplicationContext()).getBird(birdID);

        switch (item.getItemId()){
            /**
             * Source: https://developer.android.com/training/camera/photobasics.html
             * Purpose: Save images into gallery
             */

            case R.id.choiceShare:
                /**
                 * Source: https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
                 * Purpose: Sharing images
                 */
                if(birdSelected.getImage(this) != null) {
                    Intent intentSend = new Intent(Intent.ACTION_SEND);
                    intentSend.putExtra(Intent.EXTRA_STREAM, Uri.parse(birdSelected.getImageURI()));
                    intentSend.setType("image/jpg");
                    startActivity(Intent.createChooser(intentSend, getString(R.string.bird_info_share_message)));
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence string = getString(R.string.image_missing_error);
                    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                }
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
                        Toast.makeText(this, R.string.image_missing_error, Toast.LENGTH_LONG).show();
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
                    deleteImage(new File(Uri.parse(selectedBird.getImageURI()).getPath()));
                    Uri imageUriLocal = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUriLocal);
                    byte[] buffer = new byte[imageStream.available()];
                    imageStream.read(buffer);
                    File targetFile = createImageFile();
                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);
                    imageStream.close();
                    outStream.close();
                    imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", targetFile).toString();
                    selectedBird.setImageURI(imageUri);
                    MyDatabaseHelper.getInstance(getApplicationContext()).updateBird(selectedBird);
                    updateImage();
                } catch (Exception e) {
                    Toast.makeText(this, R.string.image_selected_error, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, R.string.image_missing_error, Toast.LENGTH_LONG).show();
            }
        }
        if (reqCode == REQUEST_TAKE_PHOTO){
            if (resultCode == RESULT_OK){
                deleteImage(new File(Uri.parse(selectedBird.getImageURI()).getPath()));
                selectedBird.setImageURI(imageUri);
                MyDatabaseHelper.getInstance(getApplicationContext()).updateBird(selectedBird);
                updateImage();
            }
            else {
                Toast.makeText(this, R.string.image_missing_error, Toast.LENGTH_LONG).show();
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

        return image;
    }

    /**
     * Adapted from https://stackoverflow.com/questions/39530663/delete-image-file-from-device-programmatically/39531107
     * @param file
     */
    private void deleteImage(File file) {
        // Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        c.close();
    }
}
