package ru.mirea.krasikova.data.storage;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.krasikova.data.model.PlantEntity;

public class FakePlantStorage implements PlantStorage {

    private final List<PlantEntity> fakePlants = new ArrayList<>();

    public FakePlantStorage() {
        PlantEntity chamomile = new PlantEntity();
        chamomile.id = 1;
        chamomile.name = "Ромашка";
        chamomile.description = "Лекарственное растение с белыми лепестками";
        chamomile.imageUrl = "https://avatars.mds.yandex.net/i?id=ebce99449345b0208f5039839461b58a_l-4571642-images-thumbs&n=13";
        chamomile.dateAdded = "2025-11-06";

        PlantEntity rose = new PlantEntity();
        rose.id = 2;
        rose.name = "Роза";
        rose.description = "Кустарник с крупными цветками";
        rose.imageUrl = "https://i.pinimg.com/originals/5f/46/93/5f4693c066d085ceeabddf2f2be2d7f8.jpg";
        rose.dateAdded = "2025-11-06";

        PlantEntity sunflower = new PlantEntity();
        sunflower.id = 3;
        sunflower.name = "Подсолнух";
        sunflower.description = "Жёлтый солнечный цветок";
        sunflower.imageUrl = "https://avatars.dzeninfra.ru/get-zen_doc/3680683/pub_5f28e95c47b17947de681150_5f28ea1c0f67d2688ea85b69/scale_1200";
        sunflower.dateAdded = "2025-11-06";

        fakePlants.add(chamomile);
        fakePlants.add(rose);
        fakePlants.add(sunflower);
    }

    @Override
    public List<PlantEntity> getAll() {
        return fakePlants;
    }

    @Override
    public PlantEntity getById(int id) {
        for (PlantEntity e : fakePlants) {
            if (e.id == id) return e;
        }
        return null;
    }

    @Override
    public void insert(PlantEntity plant) {
        plant.id = fakePlants.size() + 1;
        fakePlants.add(plant);
    }
}
