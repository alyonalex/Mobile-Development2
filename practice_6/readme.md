Отчёт по 5 практической работе
----
В рамках данной практической работы была рассмотрена библиотека Retrofit, используемая для работы с сетевыми данными, а также библиотеки Picasso и Glide, используемые для обработки изображений. 

RetrofitApp
---
Для работы был создан новый модуль. В манифест файл добавлено разрешение на использование интернета.
```xml
<uses-permission android:name="android.permission.INTERNET"
```
Далее была создана модель POJO 
```JAVA
public class Todo {

    @SerializedName("userId")
    @Expose
    private Integer userId;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("completed")
    @Expose
    private Boolean completed;

    public Todo() {
    }

    public Todo(Boolean completed) {
        this.completed = completed;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
```
Создан интерфейс ApiService, в котором описан GET-запрос к серверу для получения данных.
```java
public interface ApiService {
    @GET("todos")
    Call<List<Todo>> getTodos();

    @PATCH("todos/{id}")
    Call<Todo> updateTodo(
            @Path("id") int todoId,
            @Body Todo todo
    );
}
```
Также создан вспомогательный компонент - TodoAdapter, который управляет элементами списка и отвечает за преобразование данных в элементы UI. Для реализации второй части задания в данный класс добавлена инициализация строкового массива с ссылками на изображения и обработка данных изображений при помощи Picasso.
```java
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> todoList;
    private ApiService apiService;

    private static final String[] IMAGE_URLS = {
            "https://images-na.ssl-images-amazon.com/images/I/71zTLlqhDEL._AC_SL1200_.jpg",
            "https://avatars.mds.yandex.net/i?id=d3d10a35f0b68200db004ff516732f84cafb3322-8211098-images-thumbs&ref=rim&n=33&w=263&h=250",
            "https://i.pinimg.com/originals/1a/a8/bd/1aa8bdcced83056066833fe6e2934514.png",
            "https://thumbs.dreamstime.com/b/sticker-note-pin-13549913.jpg"
    };

    public TodoAdapter(List<Todo> todoList, ApiService apiService) {
        this.todoList = todoList;
        this.apiService = apiService;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.textViewTitle.setText(todo.getTitle());

        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(Boolean.TRUE.equals(todo.getCompleted()));

        int base = (todo.getId() != null ? todo.getId() : position);
        String imageUrl = IMAGE_URLS[ Math.abs(base) % IMAGE_URLS.length ];

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(64, 64)
                .centerCrop()
                .into(holder.imageTodo);

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            Todo updatedTodo = new Todo(isChecked);

            Call<Todo> call = apiService.updateTodo(todo.getId(), updatedTodo);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("TodoAdapter", "Todo " + todo.getId() + " updated successfully!");
                        todoList.get(adapterPosition).setCompleted(response.body().getCompleted());
                        notifyItemChanged(adapterPosition);
                    } else {
                        Log.e("TodoAdapter", "Failed to update todo. Code: " + response.code());
                        buttonView.setChecked(!isChecked);
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    Log.e("TodoAdapter", "Network error on update: " + t.getMessage());
                    buttonView.setChecked(!isChecked);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTodo;
        TextView textViewTitle;
        CheckBox checkBoxCompleted;

        public TodoViewHolder(View itemView) {
            super(itemView);
            imageTodo = itemView.findViewById(R.id.imageTodo);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        }
    }
}
```
В MainActivity был создан экземпляр Retrofit. Настроено асинхронное выполнение запроса с использованием метода enqueue() и обработка ответа от сервера. Инициализирован адаптер для управления элементами списка.
```java
public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private static String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Todo>> call = apiService.getTodos();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Todo> todos = response.body();
                    todoAdapter = new TodoAdapter(todos, apiService);
                    recyclerView.setAdapter(todoAdapter);
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

**Приложение после запуска без добавления картинок:**

<img width="480" height="822" alt="Снимок экрана 2025-11-09 123949" src="https://github.com/user-attachments/assets/485d8313-d746-430a-bf22-a262d4170fdf" />

**Приложение после запуска после добавления картинок:**

<img width="500" height="821" alt="Снимок экрана 2025-11-09 125745" src="https://github.com/user-attachments/assets/91bf8267-8775-45c9-9344-44e120671886" />


GreenGuide
---
В проект добавлена реализация сценария получения данных о погоде с использование Retrofit интерфейса, также реализованы сценарии работы с базой данных для получения, просмотра и добавления новых сущностей. Настроена работа Glide для обработки URL-адресов и получения изображений.

Для корректной работы Retrofit, базы данных и glide в gradle файлы проекта были добавлены необходимые зависимости.

**data/build.gradle.kts**
```java
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}
dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //SQLite
    implementation("androidx.sqlite:sqlite:2.4.0")
    implementation("androidx.sqlite:sqlite-framework:2.4.0")
}
```

**app/build.gradle.kts**
```java
dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
}
```
Добавлен новый интерфейс для получения ip адреса пользователя
```java
public interface IpInfoService {
    @GET("json")
    Call<IpInfoResponse> getIpInfo();
}
```
Retrofit интерфейс OpenMeteoService:
```java
public interface OpenMeteoService {
    @GET("v1/forecast")
    Call<WeatherResponse> getCurrentWeather(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current_weather") boolean currentWeather
    );
}
```
На уровне domain настроены модели для ответа сервера.
**IpInfoResponse**
```java
public class IpInfoResponse {
    public String city;
    public String country;
    public String loc; // "lat,lon"
}
```
**WeatherResponse**
```java
public class WeatherResponse {
    public CurrentWeather current_weather;

    public static class CurrentWeather {
        public double temperature;
        public double windspeed;
    }
}
```

В WeatherRepository добавлен интерфейс обратного вызова, который описывает, что репозиторий должен вернуть при работе асинхронно :
```java
public interface WeatherRepository {
    void getWeatherByIp(RepositoryCallback<WeatherInfo> callback);

    interface RepositoryCallback<T> {
        void onSuccess(T weatherInfo);
        void onError(String errorMessage);
    }
}
```
В реализацию данного интерфейса внесены изменения с учётом получения данных погоды по IP-адресу пользователя через два сетевых запроса с помощью Retrofit.:
1. ipInfoService.getIpInfo() — делает запрос к API, чтобы узнать IP, город, страну и координаты (широту/долготу).Когда ответ получен — парсит координаты (lat, lon).
2. openMeteoService.getCurrentWeather(lat, lon, true) — делает второй запрос к погодному API по этим координатам. Когда погода получена — создаётся объект WeatherInfo (город, страна, температура, скорость ветра).
3. Результат передаётся в callback.onSuccess().
4. Если что-то пошло не так (ошибка сети, парсинга, или пустой ответ) — вызывается callback.onError().

```java
public class WeatherRepositoryImpl implements WeatherRepository {
    private final IpInfoService ipInfoService;
    private final OpenMeteoService openMeteoService;

    public WeatherRepositoryImpl(IpInfoService ipInfoService, OpenMeteoService openMeteoService) {
        this.ipInfoService = ipInfoService;
        this.openMeteoService = openMeteoService;
    }

    @Override
    public void getWeatherByIp(RepositoryCallback<WeatherInfo> callback) {
        ipInfoService.getIpInfo().enqueue(new Callback<IpInfoResponse>() {
            @Override
            public void onResponse(Call<IpInfoResponse> call, Response<IpInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    IpInfoResponse ipInfo = response.body();

                    try {
                        String[] locParts = ipInfo.loc.split(",");
                        double lat = Double.parseDouble(locParts[0]);
                        double lon = Double.parseDouble(locParts[1]);

                        openMeteoService.getCurrentWeather(lat, lon, true)
                                .enqueue(new Callback<WeatherResponse>() {
                                    @Override
                                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> resp) {
                                        if (resp.isSuccessful() && resp.body() != null) {
                                            WeatherResponse.CurrentWeather w = resp.body().current_weather;
                                            WeatherInfo weatherInfo = new WeatherInfo(
                                                    ipInfo.city,
                                                    ipInfo.country,
                                                    w.temperature,
                                                    w.windspeed
                                            );
                                            callback.onSuccess(weatherInfo);
                                        } else {
                                            callback.onError("Ошибка при получении погоды");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                                        callback.onError("Сетевая ошибка: " + t.getMessage());
                                    }
                                });
                    } catch (Exception e) {
                        callback.onError("Ошибка при обработке координат: " + e.getMessage());
                    }
                } else {
                    callback.onError("Ошибка при получении IP-информации");
                }
            }

            @Override
            public void onFailure(Call<IpInfoResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }
}
```
Во ViewModel добавлена реализация метода loadWeatherByIp(), который вызывает getWeatherUseCase.execute(). Результат возвращается через callback.
```java
public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherInfo> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final GetWeatherUseCase getWeatherUseCase;

    public WeatherViewModel(GetWeatherUseCase getWeatherUseCase) {
        this.getWeatherUseCase = getWeatherUseCase;
    }

    public LiveData<WeatherInfo> getWeatherLiveData() {
        return weatherLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadWeatherByIp() {
        getWeatherUseCase.execute(new GetWeatherUseCase.Callback() {
            @Override
            public void onSuccess(WeatherInfo weatherInfo) {
                weatherLiveData.postValue(weatherInfo);
                errorLiveData.postValue(null);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.postValue(errorMessage);
            }
        });
    }
}
```
Фабрика теперь создаёт два клиента Retrofit: для получения координат по IP и для получения погоды по координатам.
```java
public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Retrofit ipRetrofit = new Retrofit.Builder()
                .baseUrl("https://ipinfo.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpInfoService ipService = ipRetrofit.create(IpInfoService.class);

        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenMeteoService weatherService = weatherRetrofit.create(OpenMeteoService.class);

        WeatherRepository repository = new WeatherRepositoryImpl(ipService, weatherService);

        GetWeatherUseCase useCase = new GetWeatherUseCase(repository);

        return (T) new WeatherViewModel(useCase);
    }
}
```
WeatherActivity создаёт необходимые экземпляры, отображает данные о погоде, подписывается на LiveData, хранит данные и ошибки.
```java
public class WeatherActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;
    private TextView cityText;
    private TextView countryText;
    private TextView temperatureText;
    private TextView windSpeedText;
    private TextView errorText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityText = findViewById(R.id.cityText);
        countryText = findViewById(R.id.countryText);
        temperatureText = findViewById(R.id.temperatureText);
        windSpeedText = findViewById(R.id.windSpeedText);
        errorText = findViewById(R.id.errorText);


        WeatherViewModelFactory factory = new WeatherViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);


        viewModel.getWeatherLiveData().observe(this, weatherInfo -> {
            if (weatherInfo != null) {
                cityText.setText("Город: " + weatherInfo.getCity());
                countryText.setText("Страна: " + weatherInfo.getCountry());
                temperatureText.setText(String.format("Температура: %.1f°C", weatherInfo.getTemperature()));
                windSpeedText.setText(String.format("Ветер: %.1f м/с", weatherInfo.getWindSpeed()));
            }
        });


        viewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                errorText.setText("Ошибка: " + errorMessage);
            } else {
                errorText.setText("");
            }
        });


        viewModel.loadWeatherByIp();

        Button backButton = findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
```
**После перехода на страницу погоды данные автоматически подгружаются и отображаются пользователю:**

<img width="562" height="846" alt="Снимок экрана 2025-11-09 152630" src="https://github.com/user-attachments/assets/48727a87-5aca-4c7d-ae9b-3f645834c70b" />


Для обработки изображений в PlantAdapter был добавлен метод с использованием Glide.
```java
public void bind(Plant plant) {
            plantName.setText(plant.getName());
            Glide.with(itemView.getContext())
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(plantImage);
        }
```

Настроена работа всех основных функций приложения. Теперь пользователь может:
1. Просматривать список растений
2. Просматривать информацию по конкретному растению
3. Добавлять новые растения (только авторизованный)
4. Просматривать данные о погоде

**После первичного запуска в БД нет данных, поэтому список пуст:**

<img width="603" height="850" alt="Снимок экрана 2025-11-09 212634" src="https://github.com/user-attachments/assets/1f45de68-14c5-4eac-98f1-413f330e5154" />

**Добавление нового растения:**

<img width="477" height="854" alt="Снимок экрана 2025-11-09 215228" src="https://github.com/user-attachments/assets/e595198f-024c-4a1a-9d6e-638bea7e02b2" />

**Добавленное растение появилось в списке:**

<img width="502" height="849" alt="Снимок экрана 2025-11-09 215238" src="https://github.com/user-attachments/assets/eb1993b6-683a-49de-b2d8-d65109a2e3ea" />

**Просмотр информации по конкретному растению:**

<img width="478" height="849" alt="Снимок экрана 2025-11-09 215253" src="https://github.com/user-attachments/assets/cd79cc28-da0d-49d0-99e5-7c85db14ba56" />


**Просмотр списка растений от лица гостя (без возможности добавлять новые):**

<img width="493" height="850" alt="Снимок экрана 2025-11-09 215951" src="https://github.com/user-attachments/assets/0fade782-a6d1-48f0-9d44-030b00591ab6" />

