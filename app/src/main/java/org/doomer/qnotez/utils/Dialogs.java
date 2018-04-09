package org.doomer.qnotez.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import android.content.DialogInterface.OnClickListener;

import org.doomer.qnotez.R;

public class Dialogs {
    public static MaterialDialog createListDialog(final Context context, int strIdTitle, int strIdArrayList,
                                          ListCallback callback) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(strIdTitle);
        builder.items(strIdArrayList);
        builder.itemsCallback(callback);

        MaterialDialog dialog = builder.build();
        return dialog;
    }

    public static MaterialDialog createConfirmDialog(final Context context,
                                                     int strIdTitle, int strIdContent,
                                                     MaterialDialog.SingleButtonCallback positive,
                                                     MaterialDialog.SingleButtonCallback negative) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);

        builder.title(strIdTitle);
        builder.content(strIdContent);
        builder.positiveText(context.getString(R.string.msg_yes));
        builder.negativeText(context.getString(R.string.msg_no));
        builder.onPositive(positive);
        builder.onNegative(negative);

        MaterialDialog dialog = builder.build();
        return dialog;
    }

    public static MaterialDialog createCustomSingleDialog(final Context context, int strIdTitle,
                                                          int custom_view) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(strIdTitle);
        builder.customView(custom_view, false);

        MaterialDialog dialog = builder.build();
        return dialog;
    }
}
