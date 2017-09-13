package org.doomer.qnotez.db;

import android.arch.lifecycle.LiveData;
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

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME)
    LiveData<List<NoteModel>> getAllItems();

    @Query("SELECT * FROM " + NoteModel.TABLE_NAME + " WHERE id = :id")
    LiveData<NoteModel> getItem(String id);

    @Insert(onConflict = REPLACE)
    void addItem(NoteModel item);

    @Update
    void updateItem(NoteModel item);

    @Delete
    void deleteItem(NoteModel item);
}
