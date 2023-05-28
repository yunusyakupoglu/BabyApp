package com.example.babyapp.repositories.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.babyapp.repositories.db.entities.Post;

import java.util.List;


@Dao
public interface PostDao {
    @Insert
    void insert(Post post);
    @Update
    void update(Post post);
    @Delete
    void delete(Post post);

    @Query("SELECT * FROM Post")
    List<Post> loadAllPosts();

    @Query("SELECT * FROM Post WHERE id=:id")
    Post loadPostById(long id);

    @Query("SELECT * FROM Post WHERE UserId=:uid")
    List<Post> loadPostsByUId(String uid);

}
