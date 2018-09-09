package org.doomer.qnotez.utils

import android.util.Log

import org.doomer.qnotez.db.NoteModel
import java.util.*

import kotlinx.serialization.json.JSON
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


const val BACLUP_VERSION = 3

@Serializable
data class NoteItemBackup(var id: Int = 0,
                          var title: String?,
                          var text: String?,
                          @Serializable(with = DateSerializer::class) var created: Date? = null,
                          @Serializable(with = DateSerializer::class) var updated: Date? = null,
                          var isInTrash: Boolean
)


// List<NoteItemBackup>
@Serializable
data class NoteBackup(@SerialName("version") var backupVersion : Int= BACLUP_VERSION,
                      @Serializable(with = DateSerializer::class) var backupDate : Date = Date(),
                      var notes : List<NoteItemBackup>
)


class BackupTool {
    var info : BackupInfo = BackupInfo()

    fun isEmpty() : Boolean = info.itemsCount == 0

    fun prepareData(data : List<NoteModel>?) : String {
        val items : MutableList<NoteItemBackup> = mutableListOf()

        data?.let {
            for (note in data) {
                val item = noteToBackup(note)
                items.add(item)
            }
        }

        val backup : NoteBackup = NoteBackup(notes = items)

        info.operation = BackupOperation.Save
        info.itemsCount = backup.notes.count()
        info.backupDate = backup.backupDate
        info.version = backup.backupVersion

        return JSON.indented.stringify(backup)
    }

    private fun noteToBackup(note : NoteModel) : NoteItemBackup {
        val item : NoteItemBackup = NoteItemBackup(
                id = note.id, title = note.title, isInTrash = note.isInTrash,
                text = note.text, created = note.created
        )

        note.updated?.let {
            item.updated = it
        }

        return item
    }

    fun parseData(jsonData : String) : List<NoteModel>? {
        val backupData : NoteBackup = JSON.indented.parse(jsonData)

        val items : MutableList<NoteModel> = mutableListOf()
        val notes : List<NoteItemBackup> = backupData.notes

        info.operation = BackupOperation.Load
        info.itemsCount = notes.count()
        info.backupDate = backupData.backupDate
        info.version = backupData.backupVersion

        notes?.let {
            for (item in notes) {
                val note = noteFromBackup(item)
                items.add(note)
            }
        }

        return items
    }

    private fun noteFromBackup(item : NoteItemBackup) :NoteModel {
        val note = NoteModel(
                id = item.id, title = item.title, text = item.text,
                created = item.created, updated = item.updated,
                isInTrash = item.isInTrash
        )

        return note
    }
}

enum class BackupOperation(code : Int) {
    Load(10),
    Save(20)
}

data class BackupInfo(var itemsCount : Int = 0,
                      var backupDate :Date = Date(),
                      var version : Int = BACLUP_VERSION,
                      var operation : BackupOperation = BackupOperation.Load
                      )

