package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogUtil;

public class ChangeLog {
    private static final String PREF_KEY = "installedVersion";

    public static void showWFullChangelog(AppCompatActivity activity) {
        ChangelogBuilder builder = new ChangelogBuilder();
        builder.buildAndShowDialog(activity, false);
    }

    public static void showWhatsNew(AppCompatActivity activity) {
        int codeSaved = readSavedVersionCode(activity);

        if (isNewVersion(activity, codeSaved)) {
            ChangelogBuilder builder = new ChangelogBuilder();
            builder.withMinVersionToShow(codeSaved + 1);
            builder.buildAndShowDialog(activity, false);
        }

    }

    private static boolean isNewVersion(Context ctx, int codeInstaller) {
        boolean result = false;

//        int codeSaved = readSavedVersionCode(ctx);
        int codeCurrent = ChangelogUtil.getAppVersionCode(ctx);

        if (codeInstaller < codeCurrent) {
            writeCurrentVersionCode(ctx);
            result = true;
        }

        return result;
    }

    private static int readSavedVersionCode(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        int verCode = sp.getInt(PREF_KEY, 0);
        Log.d("XXXXX", String.valueOf(verCode));
        return verCode;
    }

    private static void writeCurrentVersionCode(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        int currentVerCode = ChangelogUtil.getAppVersionCode(ctx);
        sp.edit().putInt(PREF_KEY, currentVerCode).apply();
    }
}
