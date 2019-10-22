package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PictureActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

    private ImageButton imageButton = (ImageButton) findViewById(R.id.imageChosen);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
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
                System.out.println("take arb");
                break;

            case R.id.choiceChoose:
                System.out.println("choose arb");
                break;
        }
        return false;
    }
}
