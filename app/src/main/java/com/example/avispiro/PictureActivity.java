package com.example.avispiro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PictureActivity extends AppCompatActivity {
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

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

        // commented out bc errors
//        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(this);
//        popup.show();
    }
}
