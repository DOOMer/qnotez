package org.doomer.qnotez.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.content_note_add.*

import org.doomer.qnotez.R
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.Dialogs
import org.doomer.qnotez.viewmodel.NoteAddViewModel

import org.doomer.qnotez.utils.ActivityUtils
import org.doomer.qnotez.utils.ThemeChanger

import com.afollestad.materialdialogs.MaterialDialog
import org.doomer.qnotez.utils.showMessageSnack
import org.doomer.qnotez.viewmodel.ViewModelFactory

import javax.inject.Inject

class NoteAddActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var addViewModel: NoteAddViewModel? = null

    private var backAction = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        ThemeChanger.setFromSettings(this)
        backAction = ActivityUtils.noteEditBackBehavior(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)

        title = getString(R.string.title_activity_note_add)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        text_updated.text = ""

        val inSended = intent
        val actiion = inSended.action
        val type = inSended.type

        if (Intent.ACTION_SEND == actiion && type != null) {
            if ("text/plain" == type) {
                dataFromIntent(inSended)
            }
        }

        addViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteAddViewModel::class.java!!)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            if (edit_text.text.toString().length == 0) {
                showMessageSnack(edit_text, getString(R.string.warning_emty_text))
            } else {
                saveNote()
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun dataFromIntent(`in`: Intent) {
        val sharedTitle = `in`.getStringExtra(Intent.EXTRA_SUBJECT)
        val sharedText = `in`.getStringExtra(Intent.EXTRA_TEXT)

        if (sharedTitle != null) {
            edit_title.setText(sharedTitle)
        }

        if (sharedText != null) {
            edit_text.setText(sharedText)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                checkForSave()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        checkForSave()
    }

    private fun checkForSave() {
        when (backAction) {
            ActivityUtils.BACK_SAVE_AUTO -> if (edit_text.text.toString().isEmpty()) {
                showMessageSnack(edit_text, getString(R.string.warning_emty_text))
            } else {
                saveNote()
            }
            ActivityUtils.BACK_SAVE_CONFIRM -> if (edit_title.text.toString().isEmpty()
                    && edit_text.text.toString().isEmpty()) {
                finish()
            } else {
                val strIdTitle = R.string.msg_warning
                val strIdContent = R.string.msg_note_save_text

                val saveConfirm = Dialogs.createConfirmDialog(this,
                     strIdTitle, strIdContent,
                     MaterialDialog.SingleButtonCallback { dialog, which ->
                         if (edit_text.text.toString().isEmpty()) {
//                             emptyTextWarning()
                             showMessageSnack(edit_text, getString(R.string.warning_emty_text))
                         } else {
                             saveNote()
                         }
                    }, // positive
                    MaterialDialog.SingleButtonCallback { dialog, which ->
                        finish()
                    } // negatve
                )
                saveConfirm.show()
            }
            ActivityUtils.BACK_SAVE_NO -> finish()
        }
    }

    private fun saveNote() {
        val newNote = NoteModel(title = edit_title.text.toString(),
                text = edit_text.text.toString())
        addViewModel!!.addNote(newNote)
        finish()
    }
}
