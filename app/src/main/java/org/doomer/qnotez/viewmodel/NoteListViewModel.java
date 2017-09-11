package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;

import java.util.List;

public class NoteListViewModel extends AndroidViewModel {

    private final LiveData<List<NoteModel>> noteItems;
    protected AppDatabase database;

    public NoteListViewModel(Application application) {
        super(application);

        database = AppDatabase.getDatabase(this.getApplication());
        noteItems = database.getNoteModel().getAllItems();
    }

    public LiveData<List<NoteModel>> getNoteItems() {
        return noteItems;
    }

    public void deleteItem(NoteModel item) {
        new deleteAsyncTast(database).execute(item);
    }

    private static class deleteAsyncTast extends AsyncTask<NoteModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTast(AppDatabase database) {
            db = database;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            db.getNoteModel().deleteItem(noteModels[0]);
            return null;
        }
    }
}
