package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.NoteUtils;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class NoteDetailViewModel extends AndroidViewModel {
    protected AppDatabase database;
    private MutableLiveData<NoteModel> noteItem = new MutableLiveData<NoteModel>();

    public NoteDetailViewModel(Application application) {
        super(application);
        database = AppDatabase.getDatabase(this.getApplication());
    }

    public void getData(int id) {
        String idStr = String.valueOf(id);
        Log.d("NNNN", "GET DATA " + idStr);
        GetItemAsyncTask giTask = new GetItemAsyncTask(database);

        try {
            noteItem.setValue(giTask.execute(idStr).get());
        } catch (InterruptedException e){
            Log.d("SSSSS", "INTERRUPT EXCEPTION");
        } catch (ExecutionException e) {
            Log.d("SSSSS", "EXECUTE EXCEPTION");
        }
    }

    public void updateItem(NoteModel note) {
        note.setUpdated(new Date());
        new NoteUtils.NoteUpdateAsyncTask(database).execute(note);
        noteItem.setValue(note);
    }

    public LiveData<NoteModel> getNoteItem() {
        return noteItem;
    }

    private static class GetItemAsyncTask extends AsyncTask<String, Void, NoteModel> {

        private AppDatabase db;

        GetItemAsyncTask(AppDatabase database) {
            db = database;
        }

        @Override
        protected NoteModel doInBackground(String... strings) {
            return db.getNoteModel().getItem(strings[0]);
        }
    };
}
