package ru.mirea.krasikova.greenguide.domain.model;

public class Plant {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String dateAdded;

    public Plant(int id, String name, String description, String imageUrl, String dateAdded) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.dateAdded = dateAdded;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getDateAdded() {
        return dateAdded;
    }
}
