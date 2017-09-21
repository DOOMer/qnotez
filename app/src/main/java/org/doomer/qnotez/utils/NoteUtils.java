package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.Intent;

import org.doomer.qnotez.R;
import org.doomer.qnotez.db.NoteModel;

public class NoteUtils {
    public static void shareNote(NoteModel note, Context context) {
        String title = note.getTitle();

        String contentText = title + "\n\n" + note.getText();

        Intent inShare = new Intent();
        inShare.setAction(Intent.ACTION_SEND);
        inShare.setType("text/plain");

        inShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        inShare.putExtra(Intent.EXTRA_SUBJECT, title);
        inShare.putExtra(Intent.EXTRA_TEXT, contentText);

//        Context appContext = App.getAppContext();
        context.startActivity( Intent.createChooser(inShare, context.getString(R.string.action_item_share_chooser)) );
    }
}
