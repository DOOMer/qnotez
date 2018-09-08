package org.doomer.qnotez.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration

val DB_NAME = "qnotez.db3"

@Database(entities = arrayOf(NoteModel::class), version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteModel() : NoteDao

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {

        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COL_TRASH + " INTEGER DEFAULT 0 NOT NULL")

        }
    }
}

