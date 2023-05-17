package com.example.babyapp.repositories.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.daos.ReplyDao;
import com.example.babyapp.repositories.db.daos.TextDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Reply;
import com.example.babyapp.repositories.db.entities.Text;

@Database(entities = {Post.class, Text.class, Reply.class}, version = 1, exportSchema = false)
public abstract class BabyAppDatabase extends RoomDatabase{

    private static BabyAppDatabase babyAppDatabase;

    public abstract PostDao postDao();

    public abstract TextDao textDao();

    public abstract ReplyDao replyDao();

    private static final String databaseName = "BabyAppDatabase.db";

    private static final Migration MIGRATION = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Veri islemleri
        }
    };

    public static synchronized BabyAppDatabase getDatabase(Context context){
        if (babyAppDatabase == null){
            babyAppDatabase =
                    Room.databaseBuilder(context.getApplicationContext(),
                                    BabyAppDatabase.class,databaseName)
                            .addMigrations(MIGRATION)
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
        }
        return babyAppDatabase;
    }

    public static void destroyInstance(){
        babyAppDatabase = null;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            db.disableWriteAheadLogging();
        }
    };

}
