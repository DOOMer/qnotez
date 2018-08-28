package org.doomer.qnotez.db

import java.util.Date

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

@Entity(tableName = TABLE_NOTES)
data class NoteModel(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                     var title: String?,
                     var text: String?,
                     @field:TypeConverters(DateConverter::class) var created: Date? = Date(),
                     @field:TypeConverters(DateConverter::class) var updated: Date? = null,
                     @ColumnInfo(name = COL_TRASH) var isInTrash: Boolean = false
) {
    init {
        this.updated = this.created
    }
}
