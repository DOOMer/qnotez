package org.doomer.qnotez.ui.fragments

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.*

import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_backup.*

import org.doomer.qnotez.R
import org.doomer.qnotez.utils.*
import org.doomer.qnotez.viewmodel.NoteListViewModel
import org.doomer.qnotez.viewmodel.ViewModelFactory


class BackupFragment : Fragment() {

    companion object {
        val FRAGMENT_TAG = "BackupFragment"

        // simply dirty hack for prevent erase log when user has rotated the screen
        private var log : String = ""

        val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val READ_REQUEST_CODE = 42
    private val CREATE_REQUEST_CODE = 43
    private val WRITE_REQUEST_CODE = 44
    private val PERMISSION_STORAGE_CODE = 111

//    @Inject
//    lateinit var notes : NoteDao

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var viewModel: NoteListViewModel? = null

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn_backup_permissions.setOnClickListener {
                if (isDisplayPermissionsRequest()) {
                    requestStoragePermissions()
                } else {
                    openAppSettings()
                }
            }

            checkStoragePermissions()
        } else {
            enableBackup(true)
        }

        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(NoteListViewModel::class.java)
    }

    private fun openBackupFile() {
        val inOpen = Intent(Intent.ACTION_OPEN_DOCUMENT)
        inOpen.addCategory(Intent.CATEGORY_OPENABLE)
        inOpen.type = "*/*"

        startActivityForResult(inOpen, READ_REQUEST_CODE)
    }

    private fun saveBackupFile() {

        if (viewModel!!.notesForBackup().isNotEmpty()) {
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
        } else {
            activity!!.showMessageSnack(layout_backup, getString(R.string.msg_empty_backup_save_empty))
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

        if (!bt.isEmpty()) {
            parsedNotes?.let {
                for (note in it) {
                    viewModel!!.addItem(note)
                }
            }
            activity!!.showMessageToast(getString(R.string.msg_empty_backup_load_ok))
            displayBackupInfo(bt.info)
        } else {
            activity!!.showMessageSnack(layout_backup, getString(R.string.msg_empty_backup_load_empty))
        }
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
            val jsonStr = bt.prepareData(viewModel!!.notesForBackup())

            IOUtils.writeText(contentResolver, fileUri, jsonStr)
            activity!!.showMessageToast(getString(R.string.msg_empty_backup_save_ok))
            displayBackupInfo(bt.info)
        } else { // android 4.4

            val bt : BackupTool = BackupTool()
            val jsonStr = bt.prepareData(viewModel!!.notesForBackup())

            IOUtils.writeText(contentResolver, saveDir, jsonStr)
            activity!!.showMessageToast(getString(R.string.msg_empty_backup_save_ok))
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

    private fun checkStoragePermissions() {
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            enableBackup(false)
            requestStoragePermissions()

        } else {
            // enable buttons
            enableBackup(true)
        }
    }

    private fun enableBackup(enabled : Boolean) {
        if (enabled) {
            btn_backup_save.visibility = View.VISIBLE
            btn_backup_load.visibility = View.VISIBLE
            btn_backup_permissions.visibility = View.GONE
            log = ""
        } else {
            btn_backup_save.visibility = View.INVISIBLE
            btn_backup_load.visibility = View.INVISIBLE
            btn_backup_permissions.visibility = View.VISIBLE
            log = getString(R.string.txt_storage_perms_deny)
        }
        text_backup_log.text = log
    }

    fun openAppSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity!!.packageName))
        startActivityForResult(appSettingsIntent, PERMISSION_STORAGE_CODE)
    }

    private fun isDisplayPermissionsRequest() =
            shouldShowPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    shouldShowPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestStoragePermissions() {
        if (isDisplayPermissionsRequest()) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            requestPermissions(PERMISSIONS_STORAGE, PERMISSION_STORAGE_CODE)
            enableBackup(false)

        } else {
            // Storage permissions have not been granted yet. Request them directly.
            requestPermissions(PERMISSIONS_STORAGE, PERMISSION_STORAGE_CODE)
            enableBackup(false)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {

        if (requestCode == PERMISSION_STORAGE_CODE ) {
            if (grantResults.containsOnly(PackageManager.PERMISSION_GRANTED)) {
                enableBackup(true)
            } else {
                enableBackup(false)
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }
}


