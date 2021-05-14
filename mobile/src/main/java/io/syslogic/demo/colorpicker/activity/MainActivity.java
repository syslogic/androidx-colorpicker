package io.syslogic.demo.colorpicker.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.syslogic.demo.colorpicker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_navhost);
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
        }
        */
    }
}
