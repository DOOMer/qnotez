package org.doomer.qnotez.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

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

    public LiveData<List<NoteModel>> quickSearch(String text, boolean inTrash) {
        text = TextUtils.prepareToLikeQuery(text);

        String inTrashStr = inTrash ? "1" : "0";
        NoteUtils.SearchAsyncTask searchTask = new NoteUtils.SearchAsyncTask(database);

        try {
            noteItems = searchTask.execute(text, inTrashStr).get();
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


    public void setShowTrash(boolean trashVisible) {
        showInTrash = trashVisible;
    }

    public void moveToTrash(NoteModel item) {
        item.setInTrash(true);
        new NoteUtils.NoteUpdateAsyncTask(database).execute(item);
    }

    public void moveFromTrash(NoteModel item) {
        item.setInTrash(false);
        new NoteUtils.NoteUpdateAsyncTask(database).execute(item);
    }
}
