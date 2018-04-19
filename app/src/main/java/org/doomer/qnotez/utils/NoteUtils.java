package org.doomer.qnotez.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.doomer.qnotez.App;
import org.doomer.qnotez.R;
import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;

public class NoteUtils {

    public static String QS_TEXT = App.getAppContext().getString(R.string.settings_qs_by_text);
    public static String QS_TITLE = App.getAppContext().getString(R.string.settings_qS_by_title);

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

    public static String getQuickSearchMode() {
        Context ctx = App.getAppContext();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String key = ctx.getString(R.string.settings_key_quick_search);

        return sp.getString(key, ctx.getString(R.string.settings_qs_by_text));
    }

    public static class NoteAddAsyncTask extends AsyncTask<NoteModel, Void, Void> {

        private AppDatabase db;

        public NoteAddAsyncTask(AppDatabase database) {
            db = database;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            db.getNoteModel().addItem(noteModels[0]);
            return null;
        }
    }

    public static class NoteUpdateAsyncTask extends AsyncTask<NoteModel, Void, Void> {
        private AppDatabase db;

        public NoteUpdateAsyncTask(AppDatabase database) {
            db = database;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            db.getNoteModel().updateItem(noteModels[0]);
            return null;
        }
    }

    public static class NoteDeleteAsyncTask extends AsyncTask<NoteModel, Void, Void> {

        private AppDatabase db;

        public NoteDeleteAsyncTask(AppDatabase database) {
            db = database;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            db.getNoteModel().deleteItem(noteModels[0]);
            return null;
        }
    }
}
