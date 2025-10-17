package ru.mirea.krasikova.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "plants")
public class PlantEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public String imageUrl;
    public String dateAdded;
}
