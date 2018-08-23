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


//@Database(entities = arrayOf(NoteModel::class), version = 2)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract val noteModel: NoteDao
//
//    companion object {
//
//        private var INSTANCE: AppDatabase? = null
//        val DB_NAME = "qnotez.db3"
//
//        fun getDatabase(context: Context): AppDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder<AppDatabase>(
//                        context.applicationContext, AppDatabase::class.java!!, DB_NAME
//                ).addMigrations(AppDatabase.MIGRATION_1_2).build()
//            }
//
//            return INSTANCE
//        }
//
//        fun destroyInstance() {
//            INSTANCE = null
//        }
//
//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE " + NoteModel.TABLE_NAME + " ADD COLUMN " + NoteModel.COL_TRASH + " INTEGER DEFAULT 0 NOT NULL")
//
//            }
//        }
//    }
//}
