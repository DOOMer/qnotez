package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;

public class NoteAddViewModel extends AndroidViewModel {
    protected AppDatabase database;

    public NoteAddViewModel(Application application) {
        super(application);

        database = AppDatabase.getDatabase(this.getApplication());
    }

    public void addNote(final NoteModel item) {
        new addAsyncTast(database).execute(item);
    }

    private static class addAsyncTast extends AsyncTask<NoteModel, Void, Void> {

        private AppDatabase db;

        public addAsyncTast(AppDatabase database) {
            db = database;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            db.getNoteModel().addItem(noteModels[0]);
            return null;
        }
    }
}
