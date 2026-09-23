package com.carlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("CAR LAUNCHER");
        text.setTextSize(32);
        text.setGravity(Gravity.CENTER);

        setContentView(text);
    }
}
