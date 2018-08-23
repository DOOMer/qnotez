package org.doomer.qnotez.utils

import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.preference.PreferenceManager

import org.doomer.qnotez.di.App
import org.doomer.qnotez.R
import org.doomer.qnotez.db.AppDatabase
import org.doomer.qnotez.db.NoteModel

object NoteUtils {

    var QS_TEXT = App.appContext!!.getString(R.string.settings_qs_by_text)
    var QS_TITLE = App.appContext!!.getString(R.string.settings_qS_by_title)

    val quickSearchMode: String
        get() {
            val ctx = App.appContext

            val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
            val key = ctx!!.getString(R.string.settings_key_quick_search)

            return sp.getString(key, ctx.getString(R.string.settings_qs_by_text))
        }

    fun shareNote(note: NoteModel, context: Context) {
        val title = note.title

        val contentText = title + "\n\n" + note.text

        val inShare = Intent()
        inShare.action = Intent.ACTION_SEND
        inShare.type = "text/plain"

        inShare.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        inShare.putExtra(Intent.EXTRA_SUBJECT, title)
        inShare.putExtra(Intent.EXTRA_TEXT, contentText)

        //        Context appContext = App.getAppContext();
        context.startActivity(Intent.createChooser(inShare, context.getString(R.string.action_item_share_chooser)))
    }

    class NoteAddAsyncTask(private val db: AppDatabase) : AsyncTask<NoteModel, Void, Void>() {

        override fun doInBackground(vararg noteModels: NoteModel): Void? {
            db.noteModel().addItem(noteModels[0])
            return null
        }
    }

    class NoteUpdateAsyncTask(private val db: AppDatabase) : AsyncTask<NoteModel, Void, Void>() {

        override fun doInBackground(vararg noteModels: NoteModel): Void? {
            db.noteModel().updateItem(noteModels[0])
            return null
        }
    }

    class NoteDeleteAsyncTask(private val db: AppDatabase) : AsyncTask<NoteModel, Void, Void>() {

        override fun doInBackground(vararg noteModels: NoteModel): Void? {
            db.noteModel().deleteItem(noteModels[0])
            return null
        }
    }

    class SearchAsyncTask(private val db: AppDatabase) : AsyncTask<String, Void, LiveData<List<NoteModel>>>() {

        override fun doInBackground(vararg strings: String): LiveData<List<NoteModel>> {
            val searchedItems: LiveData<List<NoteModel>>
            if (NoteUtils.quickSearchMode == NoteUtils.QS_TITLE) {
                searchedItems = db.noteModel().searchByTitle(strings[0], strings[1])
            } else {
                searchedItems = db.noteModel().searchByText(strings[0], strings[1])
            }

            return searchedItems
        }
    }
}
