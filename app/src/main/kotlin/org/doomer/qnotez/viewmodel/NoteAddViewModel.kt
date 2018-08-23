package org.doomer.qnotez.viewmodel

import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import org.doomer.qnotez.db.AppDatabase
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.NoteUtils


class NoteAddViewModel @Inject constructor(private val db : AppDatabase) : ViewModel() {

    fun addNote(item: NoteModel) {
        NoteUtils.NoteAddAsyncTask(db).execute(item)
    }
}


//class NoteAddViewModel(application: Application) : AndroidViewModel(application) {
//    protected var database: AppDatabase
//
//    init {
//
//        database = AppDatabase.getDatabase(this.getApplication())
//    }
//
//    fun addNote(item: NoteModel) {
//        NoteUtils.NoteAddAsyncTask(database).execute(item)
//    }
//}
