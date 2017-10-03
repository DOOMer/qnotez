package org.doomer.qnotez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.doomer.qnotez.fragments.SettingsFragment;
import org.doomer.qnotez.utils.ThemeChanger;

public class SettingsActivity extends AppCompatActivity {

    public static boolean themeIsChanged = false;
    public static boolean displayedRowsCountCHanged = false;

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
                if (themeIsChanged || displayedRowsCountCHanged) {
                    themeIsChanged = false;
                    displayedRowsCountCHanged = false;
                    return false;
                } else {
                    finish();
                    return true;


                }
        }

        return super.onOptionsItemSelected(item);
    }
}
