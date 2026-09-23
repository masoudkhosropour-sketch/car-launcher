package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout main;
    private LinearLayout edge;

    private SharedPreferences prefs;
    private PackageManager packageManager;

    private final int SLOT_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        packageManager = getPackageManager();
        prefs = getSharedPreferences("car_launcher", MODE_PRIVATE);

        createLauncher();
    }

    private void createLauncher() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setBackgroundColor(Color.BLACK);

        // قسمت اصلی لانچر
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setBackgroundColor(Color.BLACK);

        TextView title = new TextView(this);
        title.setText("CAR LAUNCHER");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        main.addView(title);

        LinearLayout.LayoutParams mainParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );

        // نوار Edge سمت راست
        edge = new LinearLayout(this);
        edge.setOrientation(LinearLayout.VERTICAL);
        edge.setGravity(Gravity.CENTER);
        edge.setPadding(8, 20, 8, 20);

        GradientDrawable edgeBackground = new GradientDrawable();
        edgeBackground.setColor(Color.rgb(30, 30, 30));
        edgeBackground.setCornerRadius(30);

        edge.setBackground(edgeBackground);

        LinearLayout.LayoutParams edgeParams =
                new LinearLayout.LayoutParams(
                        90,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

        root.addView(main, mainParams);
        root.addView(edge, edgeParams);

        // ساخت ۵ جایگاه برنامه
        for (int i = 0; i < SLOT_COUNT; i++) {
            addAppSlot(i);
        }

        setContentView(root);
    }

    private void addAppSlot(final int slot) {

        final ImageView button = new ImageView(this);

        String savedPackage = prefs.getString(
                "slot_" + slot,
                ""
        );

        if (!savedPackage.isEmpty()) {

            try {
                ApplicationInfo info =
                        packageManager.getApplicationInfo(
                                savedPackage,
                                0
                        );

                button.setImageDrawable(
                        packageManager.getApplicationIcon(info)
                );

            } catch (Exception e) {
                setDefaultIcon(button);
            }

        } else {
            setDefaultIcon(button);
        }

        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        button.setPadding(12, 12, 12, 12);

        GradientDrawable buttonBackground =
                new GradientDrawable();

        buttonBackground.setColor(
                Color.rgb(45, 45, 45)
        );

        buttonBackground.setCornerRadius(22);

        button.setBackground(buttonBackground);

        // لمس معمولی = اجرای برنامه
        button.setOnClickListener(v -> {

            String pkg = prefs.getString(
                    "slot_" + slot,
                    ""
            );

            if (pkg.isEmpty()) {
                showAppPicker(slot);
            } else {
                launchApp(pkg, slot);
            }
        });

        // نگه داشتن انگشت = انتخاب برنامه
        button.setOnLongClickListener(v -> {
            showAppPicker(slot);
            return true;
        });

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        68,
                        68
                );

        params.setMargins(0, 8, 0, 8);

        edge.addView(button, params);
    }

    private void setDefaultIcon(ImageView button) {

        button.setImageResource(
                android.R.drawable.ic_menu_add
        );
    }

    private void launchApp(String packageName, int slot) {

        try {

            Intent launchIntent =
                    packageManager.getLaunchIntentForPackage(
                            packageName
                    );

            if (launchIntent != null) {

                launchIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                );

                startActivity(launchIntent);

            } else {

                Toast.makeText(
                        this,
                        "برنامه پیدا نشد",
                        Toast.LENGTH_SHORT
                ).show();

                showAppPicker(slot);
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "خطا در اجرای برنامه",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showAppPicker(final int slot) {

        final List<ApplicationInfo> apps =
                packageManager.getInstalledApplications(
                        PackageManager.GET_META_DATA
                );

        final List<ApplicationInfo> launchableApps =
                new ArrayList<>();

        for (ApplicationInfo app : apps) {

            Intent launchIntent =
                    packageManager.getLaunchIntentForPackage(
                            app.packageName
                    );

            if (launchIntent != null) {
                launchableApps.add(app);
            }
        }

        Collections.sort(
                launchableApps,
                new Comparator<ApplicationInfo>() {
                    @Override
                    public int compare(
                            ApplicationInfo a,
                            ApplicationInfo b) {

                        String nameA =
                                a.loadLabel(packageManager)
                                        .toString();

                        String nameB =
                                b.loadLabel(packageManager)
                                        .toString();

                        return nameA.compareToIgnoreCase(nameB);
                    }
                }
        );

        String[] names =
                new String[launchableApps.size()];

        for (int i = 0;
             i < launchableApps.size();
             i++) {

            names[i] =
                    launchableApps.get(i)
                            .loadLabel(packageManager)
                            .toString();
        }

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "انتخاب برنامه برای جایگاه "
                                        + (slot + 1)
                        )
                        .setItems(
                                names,
                                (d, which) -> {

                                    ApplicationInfo selected =
                                            launchableApps.get(which);

                                    prefs.edit()
                                            .putString(
                                                    "slot_" + slot,
                                                    selected.packageName
                                            )
                                            .apply();

                                    refreshLauncher();
                                }
                        )
                        .setNegativeButton(
                                "لغو",
                                null
                        )
                        .create();

        dialog.show();
    }

    private void refreshLauncher() {

        edge.removeAllViews();

        for (int i = 0; i < SLOT_COUNT; i++) {
            addAppSlot(i);
        }
    }
}
