package io.syslogic.colorpicker.demo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.syslogic.colorpicker.demo.R;

/**
 * Main Activity
 * @author Martin Zeitler
 */
public class MainActivity extends AppCompatActivity {

    /** Kept for reference */
    private static final int resId = R.layout.fragment_nav_host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resId);
    }
}
