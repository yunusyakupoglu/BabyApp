package com.example.babyapp.repositories.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.babyapp.repositories.db.entities.Text;

import java.util.List;

@Dao
public interface TextDao {
    @Insert
    void insert(Text text);
    @Update
    void update(Text text);
    @Delete
    void delete(Text text);

    @Query("SELECT * FROM Text")
    List<Text> loadAllTexts();

    @Query("SELECT * FROM Text WHERE id=:id")
    Text loadTextById(long id);

    @Query("SELECT * FROM TEXT where type=:TEXT_TYPE and postId=:postId")
    List<Text> loadTextsByTextTypeAndPostId(int TEXT_TYPE,long postId);
}
