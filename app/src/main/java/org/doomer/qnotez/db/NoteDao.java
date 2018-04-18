package org.doomer.qnotez.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(DateConverter.class)
public interface NoteDao {

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE " + NoteModel.COL_TRASH + "=0 ORDER BY updated DESC")
    LiveData<List<NoteModel>> getAllItems();

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE " + NoteModel.COL_TRASH + "=1 ORDER BY updated DESC")
    LiveData<List<NoteModel>> getAllItemsInTrahs();

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE title LIKE :text ORDER BY updated DESC")
    LiveData<List<NoteModel>> searchByTitle(String text);

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE text LIKE :text ORDER BY updated DESC")
    LiveData<List<NoteModel>> searchByText(String text);

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE id = :id")
    NoteModel getItem(String id);

    @Insert(onConflict = REPLACE)
    void addItem(NoteModel item);

    @Update
    void updateItem(NoteModel item);

    @Delete
    void deleteItem(NoteModel item);
}
