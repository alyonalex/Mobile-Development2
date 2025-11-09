package ru.mirea.krasikova.data.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.krasikova.data.model.PlantEntity;

@Database(entities = {PlantEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlantStorage plantStorage();

    private static AppDatabase INSTANCE;

    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}


