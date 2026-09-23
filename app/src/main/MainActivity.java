package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
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
}
