package org.doomer.qnotez.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log

import org.doomer.qnotez.db.AppDatabase
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.NoteUtils
import org.doomer.qnotez.utils.TextUtils
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class NoteListViewModel @Inject constructor(private val db : AppDatabase) : ViewModel() {

    private var noteItems: LiveData<List<NoteModel>> //? = null
    private var showInTrash = false

    init {
        noteItems = db.noteModel().allItems
    }

    fun getNoteItems(): LiveData<List<NoteModel>> {

        if (!showInTrash) {
            noteItems = db.noteModel().allItems
        } else {
            noteItems = db.noteModel().allItemsInTrahs
        }

        return noteItems
    }

    fun quickSearch(text: String, inTrash: Boolean): LiveData<List<NoteModel>> {
        var text = text
        text = TextUtils.prepareToLikeQuery(text)

        val inTrashStr = if (inTrash) "1" else "0"
        val searchTask = NoteUtils.SearchAsyncTask(db)

        try {
            noteItems = searchTask.execute(text, inTrashStr).get()
        } catch (e: InterruptedException) {
            Log.d("SSSSS", "INTERRUPT EXCEPTION")
        } catch (e: ExecutionException) {
            Log.d("SSSSS", "EXECUTE EXCEPTION")
        }

        return noteItems
    }

    fun deleteItem(item: NoteModel) {
        NoteUtils.NoteDeleteAsyncTask(db).execute(item)
    }


    fun setShowTrash(trashVisible: Boolean) {
        showInTrash = trashVisible
    }

    fun moveToTrash(item: NoteModel) {
        item.isInTrash = true
        NoteUtils.NoteUpdateAsyncTask(db).execute(item)
    }

    fun moveFromTrash(item: NoteModel) {
        item.isInTrash = false
        NoteUtils.NoteUpdateAsyncTask(db).execute(item)
    }
}

//class NoteListViewModel(application: Application) : AndroidViewModel(application) {
//
//    private var noteItems: LiveData<List<NoteModel>>? = null
//    //    private MutableLiveData<List<NoteModel>> noteItems = new MutableLiveData<>();
//    protected var database: AppDatabase
//    private var showInTrash = false
//
//    init {
//
//        database = AppDatabase.getDatabase(this.getApplication())
//        noteItems = database.noteModel.allItems
//
//        //        noteItems.setValue(database.getNoteModel().getAllItems().getValue());
//    }
//
//    fun getNoteItems(): LiveData<List<NoteModel>>? {
//
//        if (!showInTrash) {
//            noteItems = database.noteModel.allItems
//        } else {
//            noteItems = database.noteModel.allItemsInTrahs
//        }
//
//        return noteItems
//    }
//
//    fun quickSearch(text: String, inTrash: Boolean): LiveData<List<NoteModel>>? {
//        var text = text
//        text = TextUtils.prepareToLikeQuery(text)
//
//        val inTrashStr = if (inTrash) "1" else "0"
//        val searchTask = NoteUtils.SearchAsyncTask(database)
//
//        try {
//            noteItems = searchTask.execute(text, inTrashStr).get()
//        } catch (e: InterruptedException) {
//            Log.d("SSSSS", "INTERRUPT EXCEPTION")
//        } catch (e: ExecutionException) {
//            Log.d("SSSSS", "EXECUTE EXCEPTION")
//        }
//
//        return noteItems
//    }
//
//    fun deleteItem(item: NoteModel) {
//        NoteUtils.NoteDeleteAsyncTask(database).execute(item)
//    }
//
//
//    fun setShowTrash(trashVisible: Boolean) {
//        showInTrash = trashVisible
//    }
//
//    fun moveToTrash(item: NoteModel) {
//        item.isInTrash = true
//        NoteUtils.NoteUpdateAsyncTask(database).execute(item)
//    }
//
//    fun moveFromTrash(item: NoteModel) {
//        item.isInTrash = false
//        NoteUtils.NoteUpdateAsyncTask(database).execute(item)
//    }
//}
