package org.doomer.qnotez.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ActivityUtils {

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
