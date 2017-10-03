package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.doomer.qnotez.App;
import org.doomer.qnotez.BuildConfig;

public class TextUtils {

    public static String prepareToLikeQuery(String text) {
        return "%" + text + "%";
    }

    public static String getVersionString() {
        String versionString;

        Context appContext = App.getAppContext();

        try {
            PackageInfo packageInfor = appContext.getPackageManager().getPackageInfo(
                    appContext.getPackageName(), 0
            );
            versionString = packageInfor.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionString = "Unknown";
        }

        return versionString;
    }

    public static String getBuildTime() {
        Date buildDate = new Date(BuildConfig.TIMESTAMP);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy k:mm");
        String buildString = sdf.format(buildDate);

        return buildString;
    }
}
