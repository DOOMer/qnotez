package org.doomer.qnotez.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v14.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.doomer.qnotez.R;
import org.doomer.qnotez.SettingsActivity;
import org.doomer.qnotez.utils.ThemeChanger;

/**Improvecode for adding fragments to main activity
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    SharedPreferences prefs;

    @Override
    public void onResume() {
        super.onResume();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SettingsActivity parent = (SettingsActivity) getActivity();

        if (key.equals(getString(R.string.settings_key_darkskin)) ) {
            parent.themeIsChanged = !parent.themeIsChanged;

            parent.finish();

            ThemeChanger.setFromSettings(parent);

            final Intent intent = parent.getIntent();
            parent.startActivity(intent);
        }

        if (key.equals(getString(R.string.settings_key_count_rows))) {
            Log.d("ZZZZZZ", "roe count settings changed");
            parent.displayedRowsCountCHanged = !parent.displayedRowsCountCHanged;
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferenc
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
