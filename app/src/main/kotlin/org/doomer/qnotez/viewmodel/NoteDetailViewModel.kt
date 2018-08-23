package org.doomer.qnotez.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import android.util.Log

import org.doomer.qnotez.db.AppDatabase
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.NoteUtils

import java.util.Date
import java.util.concurrent.ExecutionException
import javax.inject.Inject


class NoteDetailViewModel @Inject constructor(private val db : AppDatabase) : ViewModel() {

    private val noteItem = MutableLiveData<NoteModel>()

    fun getData(id: Int) {
        val idStr = id.toString()
        Log.d("NNNN", "GET DATA $idStr")
        val giTask = GetItemAsyncTask(db)

        try {
            noteItem.value = giTask.execute(idStr).get()
        } catch (e: InterruptedException) {
            Log.d("SSSSS", "INTERRUPT EXCEPTION")
        } catch (e: ExecutionException) {
            Log.d("SSSSS", "EXECUTE EXCEPTION")
        }

    }

    fun updateItem(note: NoteModel) {
        note.updated = Date()
        NoteUtils.NoteUpdateAsyncTask(db).execute(note)
        noteItem.value = note
    }

    fun getNoteItem(): LiveData<NoteModel> {
        return noteItem
    }



    private class GetItemAsyncTask internal constructor(private val db: AppDatabase) : AsyncTask<String, Void, NoteModel>() {

        override fun doInBackground(vararg strings: String): NoteModel {
            return db.noteModel().getItem(strings[0])
        }
    }
}


//class NoteDetailViewModel(application: Application) : AndroidViewModel(application) {
//    protected var database: AppDatabase
//    private val noteItem = MutableLiveData<NoteModel>()
//
//    init {
//        database = AppDatabase.getDatabase(this.getApplication())
//    }
//
//    fun getData(id: Int) {
//        val idStr = id.toString()
//        Log.d("NNNN", "GET DATA $idStr")
//        val giTask = GetItemAsyncTask(database)
//
//        try {
//            noteItem.value = giTask.execute(idStr).get()
//        } catch (e: InterruptedException) {
//            Log.d("SSSSS", "INTERRUPT EXCEPTION")
//        } catch (e: ExecutionException) {
//            Log.d("SSSSS", "EXECUTE EXCEPTION")
//        }
//
//    }
//
//    fun updateItem(note: NoteModel) {
//        note.updated = Date()
//        NoteUtils.NoteUpdateAsyncTask(database).execute(note)
//        noteItem.value = note
//    }
//
//    fun getNoteItem(): LiveData<NoteModel> {
//        return noteItem
//    }
//
//    private class GetItemAsyncTask internal constructor(private val db: AppDatabase) : AsyncTask<String, Void, NoteModel>() {
//
//        override fun doInBackground(vararg strings: String): NoteModel {
//            return db.noteModel.getItem(strings[0])
//        }
//    }
//}
