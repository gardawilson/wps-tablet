package com.example.myapplication.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PendingPrintUpdateDao {

    @Insert
    long insert(PendingPrintUpdate item);

    @Delete
    void delete(PendingPrintUpdate item);

    @Update
    void update(PendingPrintUpdate item);

    @Query("SELECT * FROM pending_print_updates ORDER BY created_at ASC")
    List<PendingPrintUpdate> getAll();

    @Query("SELECT COUNT(*) FROM pending_print_updates")
    int count();

    @Query("DELETE FROM pending_print_updates WHERE id = :id")
    void deleteById(long id);
}
