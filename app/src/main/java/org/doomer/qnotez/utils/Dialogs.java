package org.doomer.qnotez.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;

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
}
