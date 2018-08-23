package org.doomer.qnotez.ui.fragments

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log

import dagger.android.support.AndroidSupportInjection

import org.doomer.qnotez.R
import org.doomer.qnotez.ui.SettingsActivity
import org.doomer.qnotez.utils.ThemeChanger

import javax.inject.Inject

/**Improvecode for adding fragments to main activity
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
//
//        super.onCreate(savedInstanceState ) // ?: Bundle()
//    }

    override fun onResume() {
        super.onResume()

        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val parent = activity as SettingsActivity

        if (key == getString(R.string.settings_key_darkskin)) {
            parent.themeIsChanged = !parent.themeIsChanged

            parent.finish()

            ThemeChanger.setFromSettings(parent)

            val intent = parent.intent
            parent.startActivity(intent)
        }

        if (key == getString(R.string.settings_key_count_rows)) {
            Log.d("ZZZZZZ", "roe count settings changed")
            parent.displayedRowsCountCHanged = !parent.displayedRowsCountCHanged
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Indicate here the XML resource you created above that holds the preferenc
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}
