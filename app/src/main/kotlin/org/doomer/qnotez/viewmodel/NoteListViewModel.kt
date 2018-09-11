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
            noteItems = db.noteModel().allItemsInTrash
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
            Log.d("QNotez::QSearch", "INTERRUPT EXCEPTION")
        } catch (e: ExecutionException) {
            Log.d("QNotez::QSearch", "EXECUTE EXCEPTION")
        }

        return noteItems
    }

    fun addItem(item: NoteModel) {
        NoteUtils.NoteAddAsyncTask(db).execute(item)
    }

    fun deleteItem(item: NoteModel) {
        NoteUtils.NoteDeleteAsyncTask(db).execute(item)
    }


    fun cleanTrash() {
        NoteUtils.CleanTrashAsyncTask(db).execute("")
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

    fun notesForBackup() : List<NoteModel> {
        val searchTask = NoteUtils.BackupAsyncTask(db)

        var backupItems : List<NoteModel> = mutableListOf()
        try {
             backupItems = searchTask.execute("").get()
        } catch (e: InterruptedException) {
            Log.d("QNotez::QBackup", "INTERRUPT EXCEPTION")
        } catch (e: ExecutionException) {
            Log.d("QNotez::QBackup", "EXECUTE EXCEPTION")
        }

        return backupItems
    }

    fun countInTrash() : Int {

        val countTask = NoteUtils.CountTrashAsyncTask(db)

        var count : Int = 0
        try {
            count = countTask.execute("").get()
        } catch (e: InterruptedException) {
            Log.d("QNotez::QBackup", "INTERRUPT EXCEPTION")
        } catch (e: ExecutionException) {
            Log.d("QNotez::QBackup", "EXECUTE EXCEPTION")
        }

        return count
    }
}


