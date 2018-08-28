package org.doomer.qnotez.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_backup.*

import org.doomer.qnotez.R
import org.doomer.qnotez.db.NoteDao

import android.provider.DocumentsContract
import org.doomer.qnotez.utils.*


class BackupFragment : Fragment() {

    companion object {
        val FRAGMENT_TAG = "BackupFragment"

        // simply dirty hack for prevent erase log when user has rotated the screen
        private var log : String = ""
    }

    private val READ_REQUEST_CODE = 42
    private val CREATE_REQUEST_CODE = 43
    private val WRITE_REQUEST_CODE = 44

    @Inject
    lateinit var notes : NoteDao

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item : MenuItem = menu!!.findItem(R.id.action_qs)
        item.setVisible(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_backup, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity!!.title = getString(R.string.txt_backup_title)

        btn_backup_load.setOnClickListener { openBackupFile() }
        btn_backup_save.setOnClickListener { saveBackupFile() }

        text_backup_log.text = log
    }

    private fun openBackupFile() {
        val inOpen = Intent(Intent.ACTION_OPEN_DOCUMENT)
        inOpen.addCategory(Intent.CATEGORY_OPENABLE)
        inOpen.type = "*/*"

        startActivityForResult(inOpen, READ_REQUEST_CODE)
    }

    private fun saveBackupFile() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val inSave = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

            startActivityForResult(inSave, CREATE_REQUEST_CODE)
        } else { // Android 4.4
            val inSave = Intent(Intent.ACTION_CREATE_DOCUMENT)
            inSave.addCategory(Intent.CATEGORY_OPENABLE)
            inSave.type = "text/plain"
            inSave.putExtra(Intent.EXTRA_TITLE, "qnotez-backup.json")

            startActivityForResult(inSave, CREATE_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            data?.let {
                readFromFile(data.data)
            }

        }

        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val url = data.data
                val savePath = Uri.parse( url.toString() + "/qnotez-backup.json")

                writeToFile(url)
            }
        }
    }

    private fun readFromFile(url : Uri)  {
        val jsonData = IOUtils.readText(
                activity!!.contentResolver, url
        )
        val bt : BackupTool = BackupTool()
        val parsedNotes = bt.parseData(jsonData)

        parsedNotes?.let {
            for (note in it) {
                notes.addItem(note)
            }
        }

        displayBackupInfo(bt.info)
    }

    private fun writeToFile(saveDir : Uri) {

        val contentResolver = activity!!.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val docUri = DocumentsContract.buildDocumentUriUsingTree(saveDir,
                    DocumentsContract.getTreeDocumentId(saveDir))

            val fileUri = DocumentsContract
                    .createDocument(contentResolver, docUri, "text/json",
                            TextUtils.filenameForBackup() )

            val bt : BackupTool = BackupTool()
            val jsonStr = bt.prepareData(notes.backupItems)

            IOUtils.writeText(contentResolver, fileUri, jsonStr)
            displayBackupInfo(bt.info)
        } else { // android 4.4

            val bt : BackupTool = BackupTool()
            val jsonStr = bt.prepareData(notes.backupItems)

            IOUtils.writeText(contentResolver, saveDir, jsonStr)
            displayBackupInfo(bt.info)
        }


    }

    private fun displayBackupInfo(info: BackupInfo) {
        var str : String = ""

        when (info.operation) {
            BackupOperation.Load -> str = getString(R.string.txt_backup_load)
            else -> str = getString(R.string.txt_backup_save)
        }

        str += "\n"
        str += getString(R.string.txt_notes_count) + ": " + info.itemsCount.toString()
        str += "\n"
        str += getString(R.string.txt_backup_date) + ": " + info.backupDate.toString()

        log = str
        text_backup_log.text = str
    }
}