package com.example.babyapp.repositories.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.babyapp.repositories.db.entities.Reply;

import java.util.List;

@Dao
public interface ReplyDao {
    @Insert
    void insert(Reply reply);
    @Update
    void update(Reply reply);
    @Delete
    void delete(Reply reply);

    @Query("SELECT * FROM Reply")
    List<Reply> loadAllReplies();

    @Query("SELECT * FROM Reply WHERE id=:id")
    Reply loadReplyById(long id);

    @Query("SELECT * FROM Reply where textId=:textId")
    List<Reply> loadRepliesByTextId(long textId);

    @Query("SELECT COUNT(*) FROM Reply WHERE textId=:textId")
    int countRepliesByTextId(long textId);
}
