package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.doomer.qnotez.R;

public class ActivityUtils {
    public static final int BACK_SAVE_NO = 0;
    public static final int BACK_SAVE_CONFIRM = 1;
    public static final int BACK_SAVE_AUTO = 2;

    public static int noteEditBackBehavior(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String key = ctx.getString(R.string.settings_key_back_action_note_edit);

        return Integer.parseInt(sp.getString(key, String.valueOf(BACK_SAVE_CONFIRM)));
    }

    public static boolean fragmentInLayout(FragmentManager fm, String tag, Class className) {
        boolean result = false;

        fm.executePendingTransactions();

        Fragment f = fm.findFragmentByTag(tag);

        if (f != null) {
            if (f.getClass().getName().equals(className)) {
                return true;
            }

        }

        return result;
    }
}
