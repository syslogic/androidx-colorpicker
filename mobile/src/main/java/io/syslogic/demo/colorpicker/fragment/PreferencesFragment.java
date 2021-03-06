package io.syslogic.demo.colorpicker.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import io.syslogic.demo.colorpicker.R;

/**
 * Preferences Fragment
 *
 * @author Martin Zeitler
 */
public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
