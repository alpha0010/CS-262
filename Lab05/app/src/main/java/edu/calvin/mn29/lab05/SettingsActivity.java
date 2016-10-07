package edu.calvin.mn29.lab05;


import android.app.Activity;
import android.os.Bundle;

/**
 * Activity to contain the settings fragment.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
