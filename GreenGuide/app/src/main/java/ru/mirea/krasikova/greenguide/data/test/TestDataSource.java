package ru.mirea.krasikova.greenguide.data.test;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.krasikova.greenguide.domain.model.Plant;
import ru.mirea.krasikova.greenguide.domain.repository.WeatherInfo;

public class TestDataSource {
    public static List<Plant> plants = new ArrayList<Plant>() {{
        add(new Plant(1, "Подсолнечник", "Народное название — подсолнух. Является одной из важнейших масличных культур.", "https://www.greenguide.space/wp-content/uploads/2025/04/How-Much-Water-Do-Sunflowers-Need-780x470.png", "2025-09-25"));
        add(new Plant(2, "Гербера", "Герберы — светолюбивые и теплолюбивые многолетние растения, которые используют в декоре.", "https://www.greenguide.space/wp-content/uploads/2025/04/how-to-grow-and-care-for-gerbera-daisies-780x470.png", "2025-09-25"));
    }};

    public static WeatherInfo weather = new WeatherInfo(18.5, "Солнечно");
}
