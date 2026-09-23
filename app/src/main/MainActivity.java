package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE = 100;

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildLauncher();
    }

    private void buildLauncher() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER_VERTICAL);
        root.setBackgroundColor(Color.rgb(15, 15, 18));
        root.setPadding(30, 30, 20, 30);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));

        TextView title = new TextView(this);
        title.setText("CAR LAUNCHER");
        title.setTextColor(Color.WHITE);
        title.setTextSize(36);
        title.setGravity(Gravity.CENTER);

        main.addView(title);

        TextView wallpaper = new TextView(this);
        wallpaper.setText("🖼 انتخاب تصویر زمینه");
        wallpaper.setTextColor(Color.WHITE);
        wallpaper.setTextSize(20);
        wallpaper.setGravity(Gravity.CENTER);

        GradientDrawable wallpaperBg = new GradientDrawable();
        wallpaperBg.setColor(Color.rgb(40, 40, 45));
        wallpaperBg.setCornerRadius(25);
        wallpaper.setBackground(wallpaperBg);

        LinearLayout.LayoutParams wallpaperParams =
                new LinearLayout.LayoutParams(320, 70);

        wallpaperParams.setMargins(0, 40, 0, 0);

        main.addView(wallpaper, wallpaperParams);

        wallpaper.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Intent.ACTION_OPEN_DOCUMENT
            );

            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            startActivityForResult(intent, PICK_IMAGE);
        });

        LinearLayout edge = new LinearLayout(this);
        edge.setOrientation(LinearLayout.VERTICAL);
        edge.setGravity(Gravity.CENTER);

        for (int i = 1; i <= 5; i++) {

            TextView button = new TextView(this);
            button.setText("●");
            button.setTextColor(Color.WHITE);
            button.setTextSize(25);
            button.setGravity(Gravity.CENTER);

            GradientDrawable bg = new GradientDrawable();
            bg.setColor(Color.rgb(40, 40, 45));
            bg.setCornerRadius(30);

            button.setBackground(bg);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(65, 65);

            params.setMargins(0, 8, 0, 8);

            edge.addView(button, params);
        }

        root.addView(main);
        root.addView(edge);

        setContentView(root);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PICK_IMAGE &&
                resultCode == RESULT_OK &&
                data != null) {

            Uri imageUri = data.getData();

            if (imageUri != null) {

                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                getWindow().getDecorView().setBackground(
                        null
                );

                Toast.makeText(
                        this,
                        "تصویر انتخاب شد",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
