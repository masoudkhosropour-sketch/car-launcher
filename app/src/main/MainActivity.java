package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE = 100;

    private ImageView backgroundImage;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                "launcher",
                MODE_PRIVATE
        );

        buildHomeScreen();
        loadSavedWallpaper();
    }

    // -------------------------
    // صفحه اصلی
    // -------------------------

    private void buildHomeScreen() {

        FrameLayout root = new FrameLayout(this);

        root.setBackgroundColor(
                Color.rgb(12, 12, 15)
        );

        // تصویر زمینه
        backgroundImage = new ImageView(this);

        backgroundImage.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        backgroundImage.setBackgroundColor(
                Color.rgb(12, 12, 15)
        );

        root.addView(
                backgroundImage,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // لایه اصلی روی تصویر
        LinearLayout content = new LinearLayout(this);

        content.setOrientation(
                LinearLayout.HORIZONTAL
        );

        content.setGravity(
                Gravity.CENTER_VERTICAL
        );

        content.setPadding(
                35, 25, 20, 25
        );

        root.addView(
                content,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // قسمت مرکزی
        LinearLayout center = new LinearLayout(this);

        center.setOrientation(
                LinearLayout.VERTICAL
        );

        center.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams centerParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );

        content.addView(
                center,
                centerParams
        );

        // عنوان
        TextView title = new TextView(this);

        title.setText("CAR LAUNCHER");
        title.setTextColor(Color.WHITE);
        title.setTextSize(34);
        title.setGravity(Gravity.CENTER);

        center.addView(
                title,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        // دکمه تصویر زمینه
        TextView wallpaperButton =
                createButton("🖼 تصویر زمینه");

        LinearLayout.LayoutParams wallpaperParams =
                new LinearLayout.LayoutParams(
                        330,
                        75
                );

        wallpaperParams.setMargins(
                0, 35, 0, 15
        );

        center.addView(
                wallpaperButton,
                wallpaperParams
        );

        wallpaperButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseWallpaper();
                    }
                }
        );

        // دکمه برنامه‌ها
        TextView appsButton =
                createButton("▦ برنامه‌ها");

        center.addView(
                appsButton,
                new LinearLayout.LayoutParams(
                        330,
                        75
                )
        );

        appsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showApps();
                    }
                }
        );

        // نوار سمت راست
        LinearLayout edge =
                new LinearLayout(this);

        edge.setOrientation(
                LinearLayout.VERTICAL
        );

        edge.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams edgeParams =
                new LinearLayout.LayoutParams(
                        80,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

        content.addView(
                edge,
                edgeParams
        );

        // پنج جایگاه برنامه
        for (int i = 0; i < 5; i++) {

            TextView slot =
                    new TextView(this);

            slot.setText("＋");
            slot.setTextColor(Color.WHITE);
            slot.setTextSize(25);
            slot.setGravity(Gravity.CENTER);

            GradientDrawable slotBackground =
                    new GradientDrawable();

            slotBackground.setColor(
                    Color.rgb(45, 45, 50)
            );

            slotBackground.setCornerRadius(30);

            slot.setBackground(
                    slotBackground
            );

            LinearLayout.LayoutParams slotParams =
                    new LinearLayout.LayoutParams(
                            62,
                            62
                    );

            slotParams.setMargins(
                    0, 7, 0, 7
            );

            edge.addView(
                    slot,
                    slotParams
            );
        }

        setContentView(root);
    }

    // -------------------------
    // ساخت دکمه
    // -------------------------

    private TextView createButton(String text) {

        TextView button =
                new TextView(this);

        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);
        button.setGravity(Gravity.CENTER);

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.rgb(40, 40, 45)
        );

        background.setCornerRadius(25);

        button.setBackground(
                background
        );

        return button;
    }

    // -------------------------
    // انتخاب تصویر زمینه
    // -------------------------

    private void chooseWallpaper() {

        Intent intent =
                new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.setType("image/*");

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        intent.addFlags(
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        startActivityForResult(
                intent,
                PICK_IMAGE
        );
    }

    // -------------------------
    // بارگذاری تصویر ذخیره شده
    // -------------------------

    private void loadSavedWallpaper() {

        String savedImage =
                preferences.getString(
                        "wallpaper",
                        null
                );

        if (savedImage == null) {
            return;
        }

        try {

            backgroundImage.setImageURI(
                    Uri.parse(savedImage)
            );

        } catch (Exception e) {

            preferences.edit()
                    .remove("wallpaper")
                    .apply();
        }
    }

    // -------------------------
    // دریافت تصویر انتخاب شده
    // -------------------------

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

        if (requestCode != PICK_IMAGE) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        if (data == null) {
            return;
        }

        Uri imageUri =
                data.getData();

        if (imageUri == null) {
            return;
        }

        try {

            getContentResolver()
                    .takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

        } catch (Exception ignored) {
        }

        backgroundImage.setImageURI(
                imageUri
        );

        preferences.edit()
                .putString(
                        "wallpaper",
                        imageUri.toString()
                )
                .apply();

        Toast.makeText(
                this,
                "تصویر زمینه ذخیره شد",
                Toast.LENGTH_SHORT
        ).show();
    }

    // -------------------------
    // نمایش برنامه‌ها
    // -------------------------

    private void showApps() {

        final FrameLayout root =
                new FrameLayout(this);

        root.setBackgroundColor(
                Color.rgb(12, 12, 15)
        );

        // عنوان
        TextView title =
                new TextView(this);

        title.setText("برنامه‌ها");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams titleParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        80
                );

        titleParams.gravity =
                Gravity.TOP;

        root.addView(
                title,
                titleParams
        );

        // لیست برنامه‌ها
        ScrollView scroll =
                new ScrollView(this);

        LinearLayout appList =
                new LinearLayout(this);

        appList.setOrientation(
                LinearLayout.VERTICAL
        );

        appList.setPadding(
                35, 100, 35, 30
        );

        scroll.addView(appList);

        root.addView(
                scroll,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // دکمه برگشت
        TextView back =
                createButton("← بازگشت");

        FrameLayout.LayoutParams backParams =
                new FrameLayout.LayoutParams(
                        180,
                        60
                );

        backParams.gravity =
                Gravity.TOP | Gravity.RIGHT;

        backParams.setMargins(
                0, 10, 20, 0
        );

        root.addView(
                back,
                backParams
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildHomeScreen();
                        loadSavedWallpaper();
                    }
                }
        );

        // دریافت برنامه‌های نصب‌شده
        PackageManager pm =
                getPackageManager();

        Intent launcherIntent =
                new Intent(
                        Intent.ACTION_MAIN
                );

        launcherIntent.addCategory(
                Intent.CATEGORY_LAUNCHER
        );

        List<ApplicationInfo> apps =
                pm.getInstalledApplications(
                        PackageManager.GET_META_DATA
                );

        for (ApplicationInfo app : apps) {

            Intent launchIntent =
                    pm.getLaunchIntentForPackage(
                            app.packageName
                    );

            if (launchIntent == null) {
                continue;
            }

            LinearLayout appRow =
                    new LinearLayout(this);

            appRow.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            appRow.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            appRow.setPadding(
                    20, 10, 20, 10
            );

            ImageView icon =
                    new ImageView(this);

            icon.setImageDrawable(
                    app.loadIcon(pm)
            );

            appRow.addView(
                    icon,
                    new LinearLayout.LayoutParams(
                            65,
                            65
                    )
            );

            TextView name =
                    new TextView(this);

            name.setText(
                    app.loadLabel(pm).toString()
            );

            name.setTextColor(
                    Color.WHITE
            );

            name.setTextSize(20);

            name.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            LinearLayout.LayoutParams nameParams =
                    new LinearLayout.LayoutParams(
                            0,
                            75,
                            1
                    );

            nameParams.setMargins(
                    25, 0, 0, 0
            );

            appRow.addView(
                    name,
                    nameParams
            );

            appRow.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {

                                Intent launch =
                                        pm.getLaunchIntentForPackage(
                                                app.packageName
                                        );

                                if (launch != null) {
                                    startActivity(launch);
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        MainActivity.this,
                                        "اجرای برنامه ممکن نیست",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );

            appList.addView(
                    appRow
            );
        }

        setContentView(root);
    }

    // -------------------------
    // دکمه برگشت اندروید
    // -------------------------

    @Override
    public void onBackPressed() {

        buildHomeScreen();
        loadSavedWallpaper();
    }
}
