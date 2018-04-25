package org.doomer.qnotez.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.doomer.qnotez.R;
import org.doomer.qnotez.fragments.MainFragment;
import org.doomer.qnotez.fragments.TrashFragment;


public class ActivityUtils {
    public static final int BACK_SAVE_NO = 0;
    public static final int BACK_SAVE_CONFIRM = 1;
    public static final int BACK_SAVE_AUTO = 2;

    public static int noteEditBackBehavior(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String key = ctx.getString(R.string.settings_key_back_action_note_edit);

        return Integer.parseInt(sp.getString(key, String.valueOf(BACK_SAVE_CONFIRM)));
    }

    public static void changeFragment(AppCompatActivity act, int layoutId, String tag) {

        Fragment frg = null;

        if (tag.equals(TrashFragment.FRAGMENT_TAG)) {
            frg = new TrashFragment();
        } else {
            frg = new MainFragment();
        }

        if (frg != null) {
            act.getSupportFragmentManager().beginTransaction()
                    .replace(layoutId, frg, tag)
                    .commit();
        }
    }

}
