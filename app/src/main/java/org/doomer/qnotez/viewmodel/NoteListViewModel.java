package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import org.doomer.qnotez.App;
import org.doomer.qnotez.R;
import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.NoteUtils;
import org.doomer.qnotez.utils.TextUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteListViewModel extends AndroidViewModel {

    private  LiveData<List<NoteModel>> noteItems;
//    private MutableLiveData<List<NoteModel>> noteItems = new MutableLiveData<>();
    protected AppDatabase database;
    private boolean showInTrash = false;

    public NoteListViewModel(Application application) {
        super(application);

        database = AppDatabase.getDatabase(this.getApplication());
        noteItems = database.getNoteModel().getAllItems();

//        noteItems.setValue(database.getNoteModel().getAllItems().getValue());
    }

    public LiveData<List<NoteModel>> getNoteItems() {

        if (!showInTrash) {
            noteItems = database.getNoteModel().getAllItems();
        } else {
            noteItems = database.getNoteModel().getAllItemsInTrahs();
        }

        return noteItems;
    }

    public LiveData<List<NoteModel>> quickSearch(String text) {
        text = TextUtils.prepareToLikeQuery(text);

        searchAstncTask searchTask = new searchAstncTask(database);

        try {
            noteItems = searchTask.execute(text).get();
        } catch (InterruptedException e){
            Log.d("SSSSS", "INTERRUPT EXCEPTION");
        } catch (ExecutionException e) {
            Log.d("SSSSS", "EXECUTE EXCEPTION");
        }

        return noteItems;
    }

    public void deleteItem(NoteModel item) {
        new NoteUtils.NoteDeleteAsyncTask(database).execute(item);
    }

    private static class searchAstncTask extends AsyncTask<String, Void, LiveData<List<NoteModel>>> {
        private AppDatabase db;

        public searchAstncTask(AppDatabase database) {
            db = database;
        }

        @Override
        protected LiveData<List<NoteModel>> doInBackground(String... strings) {
            LiveData<List<NoteModel>> searchedItems;

            if (NoteUtils.getQuickSearchMode().equals(NoteUtils.QS_TITLE)) {
                searchedItems = db.getNoteModel().searchByTitle(strings[0]);
            } else {
                searchedItems = db.getNoteModel().searchByText(strings[0]);
            }

            return searchedItems;
        }
    }

    public void setShowTrash(boolean trashVisible) {
        showInTrash = trashVisible;
    }
}
