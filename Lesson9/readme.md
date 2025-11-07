Отчёт по 3 практической работе
----
В рамках данной практической работы были рассмотрены архитектурные шаблоны MVC, MVP, MVVM, MVI, и VIPER и их различия. На примере MVVM была реализована интеграция архитектурного шаблона в проект.

Приложение для сохранения и отображения любимого фильма
---
Был добавлен новый класс MainViewModel, предназначенный для хранения и управления данными, связанными со слоем представления, а также для управления взаимодействием с бизнес-логикой.
```
public class MainViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel created");
        this.movieRepository = movieRepository;
    }

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setText(MovieDomain movie) {
        boolean result = new SaveMovieToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(String.valueOf(result));
    }

    public void getText() {
        MovieDomain movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel cleared");
        super.onCleared();
    }
}
```
Далее была реализована фабрика ViewModelFactory, котоая принимает Context и использует его для создания зависимостей.
```
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context appContext;

    public ViewModelFactory(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage storage = new SharedPrefMovieStorage(appContext);
        MovieRepository repo = new MovieRepositoryImpl(storage);
        return (T) new MainViewModel(repo);
    }
}
```
Далее были внесены изменения в основной файл MainActivity.
1. Инициализация MainViewModel была осуществлена с помощью ViewModelProvider, который необходим для того, чтобы MainViewModel не пересоздавалась при изменениях конфигурации.
2. ViewModelProvider в свою очередь был инициализирован с использованием фабрики. Данный подход позволил полностью изолировать ViewModel от Context и создания зависимостей.
3. Создана подписка на LiveData с помощью метода .observe(), который хранит объект данных и позволяет наблюдателям получать обновления при каждом изменении данных.

```
   public class MainActivity extends AppCompatActivity {

    private MainViewModel vm;

    private EditText editText;
    private TextView textView;
    private Button saveButton;
    private Button getButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MainActivity.class.getSimpleName(), "MainActivity created");

        vm = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        initViews();
        bindObservers();
        bindClicks();
    }

    private void initViews() {
        editText   = findViewById(R.id.editTextMovie);
        textView   = findViewById(R.id.textViewMovie);
        saveButton = findViewById(R.id.buttonSaveMovie);
        getButton  = findViewById(R.id.buttonGetMovie);
    }

    private void bindObservers() {
        vm.getFavoriteMovie().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
    }

    private void bindClicks() {
        saveButton.setOnClickListener(v -> {
            String name = editText.getText().toString().trim();
            if (!name.isEmpty()) {
                vm.setText(new MovieDomain(1, name));
                editText.setText("");
            }
        });

        getButton.setOnClickListener(v -> vm.getText());
    }
}
```

**После запуска проекта был введён и сохранён любимый фильм:**

<img width="560" height="784" alt="Снимок экрана 2025-11-06 142333" src="https://github.com/user-attachments/assets/9698a61a-4663-4e70-8eed-d16a4163cfc7" />

**Содержимое Logcat после запуска:**

<img width="1452" height="332" alt="Снимок экрана 2025-11-06 142209" src="https://github.com/user-attachments/assets/6be677ba-0f1f-40dc-b03a-4e1129526553" />


**После поворота экрана сохранённый любимый фильм остался:**

<img width="1126" height="581" alt="Снимок экрана 2025-11-06 142545" src="https://github.com/user-attachments/assets/36d84a4c-e535-48a8-9c1e-9f8b518035bb" />


**Содержимое Logcat после поворота:**

<img width="1421" height="271" alt="Снимок экрана 2025-11-06 142458" src="https://github.com/user-attachments/assets/21af658a-3672-49bf-b6e9-9e10dc1502a1" />

Из логов видно, что MainViewModel была создана один раз, а MainActivity создавалась дважды при изменениях конфигурации (при повороте экрана), 
при этом данные остались сохранены в прежнем виде. Это говорит о том, что MainViewModel имеет устойчивый жизненный цикл. Она повторно не
обращается к UseCases и не загружает какие-либо данные. После изменения MainActivity происходит считывание последнего состояния из MainViewModel.

GreenGuide
---
В проект было внесено несколько изменений:
1. Взаимодействие слоя представления со слоем domain теперь происходит не напрямую через Activity, а через ViewModel.
2. Обновление состояния интерфейса осуществляется через LiveData.
3. MediatorLiveData объединяет данные и вызывает update() при их изменении.

Для работы с данными из БД была создана новая PlantListViewModel, которая хранит список растений, обёрнутый в  MutableLiveData и вызывает usecase для загрузки данных.

```
public class PlantListViewModel extends ViewModel {
    private final GetPlantsUseCase getPlantsUseCase;

    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();

    public PlantListViewModel(GetPlantsUseCase getPlantsUseCase) {
        this.getPlantsUseCase = getPlantsUseCase;
        loadPlants();
    }

    public LiveData<List<Plant>> getPlantsLiveData() {
        return plantsLiveData;
    }

    private void loadPlants() {
        plantsLiveData.setValue(getPlantsUseCase.execute());
    }
}
```
Далее была реализована фабрика ViewModelFactoryPlantListViewModelFactory, котоая принимает Context и использует его для создания зависимостей. Фабрика создаёт PlantRepositoryImpl и передаёт его в GetPlantsUseCase, который передаётся в PlantListViewModel.

```
public class PlantListViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        PlantRepository repo = new PlantRepositoryImpl();
        GetPlantsUseCase getPlantsUseCase = new GetPlantsUseCase(repo);
        return (T) new PlantListViewModel(getPlantsUseCase);
    }
}
```

В PlantListActivity через фабрику создаётся PlantListViewModel. PlantListActivity подписывается на LiveData с помощью метода .observe().
```
public class PlantListActivity extends AppCompatActivity {

    private PlantListViewModel viewModel;
    private PlantAdapter adapter;
    private Button btnBackToMainMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        SharedPrefsUserStorage userStorage = new SharedPrefsUserStorage(this);
        String userType = userStorage.getUserType();

        // RecyclerView и Adapter
        RecyclerView recyclerView = findViewById(R.id.plantsRecycler);
        adapter = new PlantAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ViewModel через Factory
        PlantListViewModelFactory factory = new PlantListViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(PlantListViewModel.class);

        // Подписка на LiveData для отображения списка растений
        viewModel.getPlantsLiveData().observe(this, plants -> {
            adapter.submitList(plants);
        });

        Button backButton = findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
```

Для работы с сетевыми (пока что замоканными) данными о погоде была создана WeatherViewModel, которая вызывает GetWeatherUseCase, который обращается к WeatherRepositoryImpl. Внутри используется MediatorLiveData, необходимый для того, чтобы прослушивать два источника координат latLiveData и lonLiveData и вызова updateWeather() когда есть оба значения.

```
public class WeatherViewModel extends ViewModel {

    private final GetWeatherUseCase getWeatherUseCase;

    private final MutableLiveData<Double> latLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> lonLiveData = new MutableLiveData<>();
    private final MediatorLiveData<WeatherInfo> weatherLiveData = new MediatorLiveData<>();

    public WeatherViewModel(GetWeatherUseCase getWeatherUseCase) {
        this.getWeatherUseCase = getWeatherUseCase;

        // MediatorLiveData отслеживает изменения координат
        weatherLiveData.addSource(latLiveData, lat -> updateWeather());
        weatherLiveData.addSource(lonLiveData, lon -> updateWeather());
    }

    public LiveData<WeatherInfo> getWeatherLiveData() {
        return weatherLiveData;
    }

    public void setCoordinates(double lat, double lon) {
        latLiveData.setValue(lat);
        lonLiveData.setValue(lon);
    }

    private void updateWeather() {
        Double lat = latLiveData.getValue();
        Double lon = lonLiveData.getValue();
        if (lat != null && lon != null) {
            WeatherInfo info = getWeatherUseCase.execute(lat, lon);
            weatherLiveData.setValue(info);
        }
    }
}
```

Далее была создана фабрика WeatherViewModelFactory, которая создаёт WeatherViewModel вручную, инстанцируя WeatherRepositoryImpl.

```
public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        GetWeatherUseCase useCase = new GetWeatherUseCase(new WeatherRepositoryImpl());
        return (T) new WeatherViewModel(useCase);
    }
}
```

В WeatherActivity создаётся ViewModel через фабрику, вручную выставляется пример координат и осуществляется подписка на weatherLiveData. При обновлении LiveData устанавливает тексты в поля TextView.
```
public class WeatherActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;
    private TextView cityText;
    private TextView countryText;
    private TextView temperatureText;
    private TextView windSpeedText;
    private Button btnBackToMainMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityText = findViewById(R.id.cityText);
        countryText = findViewById(R.id.countryText);
        temperatureText = findViewById(R.id.temperatureText);
        windSpeedText = findViewById(R.id.windSpeedText);
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu);

        WeatherViewModelFactory factory = new WeatherViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        // Пример координат
        viewModel.setCoordinates(55.751244, 37.618423); // Москва

        viewModel.getWeatherLiveData().observe(this, weatherInfo -> {
            cityText.setText("Город: " + weatherInfo.getCity());
            countryText.setText("Страна: " + weatherInfo.getCountry());
            temperatureText.setText("Температура: " + weatherInfo.getTemperature() + "°C");
            windSpeedText.setText("Скорость ветра: " + weatherInfo.getWindSpeed() + " м/с");
        });

        btnBackToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
```

**Полученные данные погоды:**

<img width="452" height="817" alt="Снимок экрана 2025-11-07 192357" src="https://github.com/user-attachments/assets/7fa713f8-a596-43ee-a0b5-8ca595f93691" />
