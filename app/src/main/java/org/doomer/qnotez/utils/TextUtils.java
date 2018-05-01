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

    public static String rowsToPreview(int count, String text) {
        String retStr = new String();
        String lines[] = text.split("\\n");

        switch (count) {
            case 2:
                retStr = arrayToString(2, lines);
                break;
            case 5:
                retStr = arrayToString(5, lines);
                break;
            case 10:
                retStr = arrayToString(10, lines);
                break;
            default:
                break;
        }

        return retStr;
    }

    private static String arrayToString(int count, String array[]) {
        StringBuilder strBuild = new StringBuilder();

        for (int i = 0; i < count; i = i + 1) {
            strBuild.append(array[i]);

            if (i < count - 1) {
                strBuild.append(" ||  ");
                strBuild.append("\n");
            } else {
                strBuild.append(" ... ");
            }
        }

        String retStr = strBuild.toString();
        return retStr;
    }
}
