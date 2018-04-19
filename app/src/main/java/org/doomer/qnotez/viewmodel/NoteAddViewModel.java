package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.NoteUtils;

public class NoteAddViewModel extends AndroidViewModel {
    protected AppDatabase database;

    public NoteAddViewModel(Application application) {
        super(application);

        database = AppDatabase.getDatabase(this.getApplication());
    }

    public void addNote(final NoteModel item) {
        new NoteUtils.NoteAddAsyncTask(database).execute(item);
    }
}
