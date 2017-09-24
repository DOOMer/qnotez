package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;

import org.doomer.qnotez.R;

public class ThemeChanger {
    public static final boolean USE_DARK_DEFAULT = false;

    public static void setFromSettings(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ket = ctx.getString(R.string.settings_key_darkskin);

        boolean useDark = sp.getBoolean(ket, USE_DARK_DEFAULT);

        Log.d("USE DARK", String.valueOf(useDark));

        if (useDark) {
            ctx.setTheme(R.style.AppThemeDark);
        } else {
            ctx.setTheme(R.style.AppThemeLight);
        }
    }
}
