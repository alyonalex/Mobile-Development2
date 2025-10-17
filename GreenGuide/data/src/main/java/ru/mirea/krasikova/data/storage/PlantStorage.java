package ru.mirea.krasikova.data.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import ru.mirea.krasikova.data.model.PlantEntity;

@Dao
public interface PlantStorage {
    @Query("SELECT * FROM plants")
    List<PlantEntity> getAll();

    @Query("SELECT * FROM plants WHERE id = :id")
    PlantEntity getById(int id);

    @Insert
    void insert(PlantEntity plant);
}
