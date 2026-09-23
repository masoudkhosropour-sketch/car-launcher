package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE = 100;
    private ImageView backgroundImage;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("launcher", MODE_PRIVATE);
        buildLauncher();

        String savedImage = preferences.getString("wallpaper", null);

        if (savedImage != null) {
            try {
                backgroundImage.setImageURI(Uri.parse(savedImage));
            } catch (Exception e) {
                // اگر عکس دیگر در دسترس نبود، پس‌زمینه پیش‌فرض می‌ماند
            }
        }
    }

    private void buildLauncher() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setBackgroundColor(Color.rgb(15, 15, 18));
        root.setPadding(30, 30, 20, 30);

        backgroundImage = new ImageView(this);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backgroundImage.setBackgroundColor(Color.rgb(15, 15, 18));

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(20, 20, 20, 20);

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

        GradientDrawable buttonBg = new GradientDrawable();
        buttonBg.setColor(Color.rgb(40, 40, 45));
        buttonBg.setCornerRadius(25);

        wallpaper.setBackground(buttonBg);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(320, 70);

        buttonParams.setMargins(0, 40, 0, 0);

        main.addView(wallpaper, buttonParams);

        wallpaper.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

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

        root.addView(backgroundImage,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                ));

        backgroundImage.addView(main);

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

                try {
                    getContentResolver().takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                } catch (Exception e) {
                    // بعضی گالری‌ها اجازه ذخیره دائمی نمی‌دهند
                }

                backgroundImage.setImageURI(imageUri);

                preferences.edit()
                        .putString("wallpaper", imageUri.toString())
                        .apply();

                Toast.makeText(
                        this,
                        "تصویر زمینه ذخیره شد",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
