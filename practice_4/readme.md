Отчёт по 4 практической работе
----
В рамках данной практической работы были изучены различные виды списков, их преимущества и недостатки. Реализовано отображение замоканных данных через RecycleView в проекте.

ScrollView
---
Для работы был создан новый модуль. Внутри него создан файл разметки item.xml для одного элемента списка, содержащий название и картинку и файл разметки, использующий элемент ScrollView для отображения всех элементов списка.
В MainActivity с помощью LayoutInflater создаётся представление элемента списка, устанавливается его значения и добавляется в контейнер.

```
public class MainActivity extends AppCompatActivity {

    private LinearLayout wrapper;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrapper = findViewById(R.id.wrapper);
        scrollView = findViewById(R.id.scrollView);

        BigInteger value = BigInteger.ONE;
        for (int i = 1; i <= 100; i++) {
            value = value.shiftLeft(1);

            View view = getLayoutInflater().inflate(R.layout.item, null, false);

            TextView text = view.findViewById(R.id.textView);
            text.setText(String.format("Элемент %d: %s", i, value.toString()));

            ImageView img = view.findViewById(R.id.imageView);
            img.setImageResource(android.R.drawable.ic_dialog_info);

            wrapper.addView(view);
        }
    }
}
```

**После запуска приложения на экране появляется список:**

<img width="468" height="818" alt="Снимок экрана 2025-11-07 223240" src="https://github.com/user-attachments/assets/5e1e52ea-f9fc-49a8-b62a-0e9273216871" />

ListView
---
Для работы был создан новый модуль. Внутри него создан файл разметки использующий элемент FrameLayout для отображения всех элементов списка.
В MainActivity создаётся и инициализируется новый массив строковых значений (названия стран), в методе onCreate создаётся простейший ArrayAdapter, который
связывает массив данных с набором элементов TextView.

```
public class MainActivity extends AppCompatActivity {
    private ListView listViewCountries;

    private String[] countries = new String[] {
            "Австралия", "Австрия", "Аргентина", "Бангладеш", "Белорусь", "Венгрия", "Великобритания",
            "Германия", "Гонконг", "Грузия", "Дания", "Египет", "Израиль", "Испания",
            "Италия", "Канада", "Марокко", "Норвегия", "ОАЭ", "Португалия", "Российская Федерация",
            "Сингапур", "Турция", "Уругвай", "Франция", "Хорватия", "Чехия", "Эквадор", "Япония"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewCountries = findViewById(R.id.listViewCountries);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                countries
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text2.setText(getItem(position).toString());
                text1.setText(String.valueOf(position+1));
                return view;
            }
        };

        listViewCountries.setAdapter(adapter);
    }
}
```

**После запуска приложения на экране отображается список с названием стран:**

<img width="451" height="820" alt="Снимок экрана 2025-11-07 223352" src="https://github.com/user-attachments/assets/9b9c8814-63c8-4ff2-84b0-5112f335528a" />

RecyclerView
---
Для работы был создан новый модуль. Внутри него создан файл разметки item_event.xml для одного элемента списка, содержащий название, краткое описание и картинку и файл разметки, использующий элемент RecyclerView для отображения всех элементов списка.
Создан класс HistoricalEvent.
```
public class HistoricalEvent {
    private String title;
    private String description;
    private int imageResId;

    public HistoricalEvent(String title, String description, int imageResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
}
```
Реализован EventAdapter:
```
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<HistoricalEvent> events;

    public EventAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HistoricalEvent event = events.get(position);
        holder.textTitle.setText(event.getTitle());
        holder.textDescription.setText(event.getDescription());
        holder.imageEvent.setImageResource(event.getImageResId());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imageEvent;
        TextView textTitle, textDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEvent = itemView.findViewById(R.id.imageEvent);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
```
В MainActivity создаётся массив исторических событий, создаётся экземляр EventAdapter, при помощи которого в recyclerView передаются данные для отображения в списке.
```
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HistoricalEvent> events = new ArrayList<>();
        events.add(new HistoricalEvent("Падение Римской империи",
                "476 год — падение Западной Римской империи, конец античности.",
                R.drawable.rome));
        events.add(new HistoricalEvent("Открытие Америки",
                "1492 год — Христофор Колумб достиг берегов Нового Света.",
                R.drawable.columbus));
        events.add(new HistoricalEvent("Первая мировая война",
                "1914–1918 годы — крупнейший конфликт начала XX века.",
                R.drawable.ww1));
        events.add(new HistoricalEvent("Высадка на Луну",
                "1969 год — Нил Армстронг сделал первый шаг на Луне.",
                R.drawable.moon));

        adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
    }
}
```
**После запуска приложения на экране отображается список исторических событий:**

<img width="467" height="814" alt="Снимок экрана 2025-11-07 223426" src="https://github.com/user-attachments/assets/7e474bc1-408c-453c-bd08-b59c14668f70" />

GreenGuide
---
В проекте настроена передача данных из репозитория через LiveData и получение их в RecycleView.
Был создан новый файл разметки для одного элемента списка item_plant.xml, содержащий название растений и его картинку и файл разметки, который использует элемент RecycleView для отображения всех элементов списка.

На уровне data создан новый класс FakePlantStorage с фейковым набором данных.
```
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
```
FakePlantStorage передаётся в репозиторий PlantRepositoryImpl, который в свою очередь создаётся при помощи фабрики в PlantListActivity.

Для управления элементами списка был создан PlantAdapter, который хранит внутренний список растений. Он обновляет этот список и вызывает notifyDataSetChanged(); RecyclerView перерисовывает элементы.
```
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final List<Plant> plants = new ArrayList<>();

    public void submitList(List<Plant> newPlants) {
        plants.clear();
        if (newPlants != null) {
            plants.addAll(newPlants);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        private final ImageView plantImage;
        private final TextView plantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
        }

        public void bind(Plant plant) {
            plantName.setText(plant.getName());

            // Загрузка изображения по ссылке
            Glide.with(itemView.getContext())
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(plantImage);
        }
    }
}
```
В PlantListActivity создаётся экземпляр PlantAdapter, который устанавливает новые значения в RecyclerView.
```
        RecyclerView recyclerView = findViewById(R.id.plantsRecycler);
        adapter = new PlantAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
```

**После запуска приложения пользователь может просматривать список растений:**

<img width="549" height="820" alt="Снимок экрана 2025-11-07 192657" src="https://github.com/user-attachments/assets/0d3f54be-5f6b-444b-be03-5bd86f53e626" />
