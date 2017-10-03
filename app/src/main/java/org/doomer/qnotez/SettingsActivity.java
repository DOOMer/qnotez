package org.doomer.qnotez;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.doomer.qnotez.fragments.SettingsFragment;
import org.doomer.qnotez.utils.ThemeChanger;

public class SettingsActivity extends AppCompatActivity {

    public static boolean themeIsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager()
                .beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!themeIsChanged) {
                    finish();
                    return true;
                } else {
                    themeIsChanged = false;
                    return false;
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
