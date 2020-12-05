package com.omada.junction.data.framework;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.omada.junction.data.framework.localdatabase.EventDao;

@Database(entities = {}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    private static final String DB_NAME = "repoDatabase.db";
    private static volatile LocalDatabase instance;

    static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private LocalDatabase() {}

    private static LocalDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                LocalDatabase.class,
                DB_NAME).build();
    }

    public abstract EventDao eventDao();

}