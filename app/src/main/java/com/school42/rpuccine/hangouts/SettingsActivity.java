package com.school42.rpuccine.hangouts;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.SharedPreferences;

public class SettingsActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        recreate();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set le theme en fonction du fichier de pref
        String them = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getApplicationContext().getString(R.string.pref_theme_key), "def");
        if (them.equals("1"))
            setTheme(R.style.AppTheme_NoActionBar);
        else if (them.equals("2"))
            setTheme(R.style.AppTheme_NoActionBar_Blue);
        else if (them.equals("3"))
            setTheme(R.style.AppTheme_NoActionBar_Hipster);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                //.replace(android.R.id.content, new SettingsFragment())
                .add(R.id.content_settings, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
