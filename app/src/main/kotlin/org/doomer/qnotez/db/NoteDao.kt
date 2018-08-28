package org.doomer.qnotez.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Query
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.Update

import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao
@TypeConverters(DateConverter::class)
interface NoteDao {

    @get:Query("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_TRASH + "=0 ORDER BY updated DESC")
    val allItems: LiveData<List<NoteModel>>

    @get:Query("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_TRASH + "=1 ORDER BY updated DESC")
    val allItemsInTrahs: LiveData<List<NoteModel>>

    @Query("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_TRASH +
            "=:inTrash AND title LIKE :text ORDER BY updated DESC")
    fun searchByTitle(text: String, inTrash: String): LiveData<List<NoteModel>>

    @Query("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_TRASH +
            "=:inTrash AND text LIKE :text ORDER BY updated DESC")
    fun searchByText(text: String, inTrash: String): LiveData<List<NoteModel>>

    @Query("SELECT * FROM " + TABLE_NOTES + " WHERE id = :id")
    fun getItem(id: String): NoteModel

    @Insert(onConflict = REPLACE)
    fun addItem(item: NoteModel)

    @Update
    fun updateItem(item: NoteModel)

    @Delete
    fun deleteItem(item: NoteModel)

    @get:Query("SELECT * FROM " + TABLE_NOTES + " ORDER BY id ASC")
    val backupItems : List<NoteModel>
}
