package ru.mirea.krasikova.data.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mirea.krasikova.data.model.PlantEntity;

@Database(entities = {PlantEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlantStorage plantStorage();
}
