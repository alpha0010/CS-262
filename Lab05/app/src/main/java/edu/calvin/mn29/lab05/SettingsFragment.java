package edu.calvin.mn29.lab05;


import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Definition of settings page.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
