package com.example.babyapp.repositories.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
            // Veri işlemleri
            database.execSQL("CREATE TABLE IF NOT EXISTS room_table_modification_log (id INTEGER PRIMARY KEY AUTOINCREMENT, invalidated INTEGER NOT NULL)");

            // Eksik olan tablo oluşturuldu, diğer işlemler buraya eklenebilir

            // Örnek: Tabloya veri ekleme
            ContentValues values = new ContentValues();
            values.put("invalidated", 1);
            database.insert("room_table_modification_log", SQLiteDatabase.CONFLICT_REPLACE, values);

            // Örnek: Tablodan veri silme
            database.delete("room_table_modification_log", "invalidated = ?", new String[]{"0"});

            // Örnek: Tablodaki verileri güncelleme
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("invalidated", 0);
            database.update("room_table_modification_log", SQLiteDatabase.CONFLICT_REPLACE, updatedValues, "invalidated = ?", new String[]{"1"});
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
