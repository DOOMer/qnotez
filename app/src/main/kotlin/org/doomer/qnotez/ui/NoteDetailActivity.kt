package org.doomer.qnotez.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View

import com.afollestad.materialdialogs.MaterialDialog

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_note_add.*
import kotlinx.android.synthetic.main.content_note_add.*

import org.doomer.qnotez.R
import org.doomer.qnotez.consts.NoteActions
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.*
import org.doomer.qnotez.viewmodel.NoteDetailViewModel
import org.doomer.qnotez.viewmodel.ViewModelFactory

import java.text.SimpleDateFormat
import javax.inject.Inject

class NoteDetailActivity : AppCompatActivity(), LifecycleRegistryOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val registry = LifecycleRegistry(this)

    private var viewModel: NoteDetailViewModel? = null
    private var backAction = -1
    private var readOnlyNote = false
    private var isSaved = true

    private val editWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            checkCHanged()
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        ThemeChanger.setFromSettings(this)
        backAction = ActivityUtils.noteEditBackBehavior(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab!!.setOnClickListener { view ->
            if (edit_text.text.toString().length == 0) {
                Snackbar.make(view, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                        .show()
            } else {
                saveNote()
                showMessageToast(getString(R.string.msg_note_saved))
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val `in` = intent
        val noteId = `in`.getIntExtra(KEY_NOTE_ID, 0)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteDetailViewModel::class.java!!)

        readOnlyNote = `in`.getBooleanExtra(NoteActions.NOTE_READ_ONLY, false)

        if (readOnlyNote) {
            fab!!.visibility = View.INVISIBLE
            edit_title.isFocusable = false
            edit_text.isFocusable = false
            title = getString(R.string.title_activity_note_detail_ro)
        }

        subscribe()

        viewModel!!.getData(noteId)

        edit_text.addTextChangedListener(editWatcher)
        edit_text.addTextChangedListener(editWatcher)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

    fun subscribe() {
        val noteObserver = Observer<NoteModel> { noteModel ->
            if (noteModel != null) {

                var title = noteModel.title

                if (readOnlyNote) {
                    if (title!!.isEmpty()) {
                        title = getString(R.string.iten_no_title)
                    }
                }

                edit_title.setText(title)
                edit_text.setText(noteModel.text)

                val sdf = SimpleDateFormat("dd MMM yyyy k:mm")
                val createdString = sdf.format(noteModel.created)
                val updatedString = sdf.format(noteModel.updated)

                var dateString = getString(R.string.txt_created) + " " + createdString

                if (noteModel.created != noteModel.updated) {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        dateString += "\n" + getString(R.string.txt_updated) + " " + updatedString
                    } else {
                        dateString += ", " + getString(R.string.txt_updated).toLowerCase() + " " + updatedString
                    }
                }

                text_updated.text = dateString
            }
        }

        viewModel!!.getNoteItem().observe(this, noteObserver)
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
        if (!readOnlyNote) {
            when (backAction) {
                ActivityUtils.BACK_SAVE_AUTO -> if (edit_text.text.toString().isEmpty()) {
                    showMessageSnack(edit_text, getString(R.string.warning_emty_text))
                } else {
                    if (!isSaved) {
                        saveNote()
                    }
                    finish()
                }
                ActivityUtils.BACK_SAVE_CONFIRM -> if (!isSaved) {
                    val strIdTitle = R.string.msg_warning
                    val strIdContent = R.string.msg_note_save_text

                    val saveConfirm = Dialogs.createConfirmDialog(this,
                        strIdTitle, strIdContent,
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            if (edit_text.text.toString().isEmpty()) {
                                showMessageSnack(edit_text, getString(R.string.warning_emty_text))
                            } else {
                                saveNote()
                                finish()
                            }
                        }, // positive
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            finish()
                        } // negatve
                    )
                    saveConfirm.show()
                } else {
                    finish()
                }
                ActivityUtils.BACK_SAVE_NO -> finish()
            }
        } else {
            finish()
        }
    }

    private fun saveNote() {
        val editNote = viewModel!!.getNoteItem().value
        editNote!!.title = edit_title.text.toString()
        editNote.text = edit_text.text.toString()

        viewModel!!.updateItem(editNote)
        isSaved = true
    }

    fun checkCHanged() {
        val newTitile = edit_title.text.toString()
        val newText = edit_text.text.toString()

        val checkNote = viewModel!!.getNoteItem().value
        isSaved = (checkNote!!.title == newTitile && checkNote.text == newText)
    }

    companion object {

        val KEY_NOTE_ID = "qnotez_key_note_id"
    }
}
