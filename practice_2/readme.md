Отчёт по 2 практической работе
----
В рамках данной практической работы были изучены основные различия моделей данных на разных слоях программного обеспечения. Добавлены изменения в существующие проекты для достижения принципов "чистой архитектуры".

Приложение для сохранения и отображения любимого фильма
---
В директории data был создан дополнительный пакет Storage, внутри которого был создан интерфейс MovieStorage с двумя методами: для получения данных «get» и для сохранения данных «save».
```
public interface MovieStorage {
 public Movie get();
 public boolean save(Movie movie);
}
```
Далее была создана реализация данного интерфейса также на уровне data - SharedPrefMovieStorage:
```
public class SharedPrefMovieStorage implements MovieStorage {
    private static final String SHARED_PREFS_NAME = "shared_prefs_name";
    private static final String KEY = "movie_name";
    private static final String DATE_KEY = "movie_date";
    private static final String ID_KEY = "movie_id";
    private SharedPreferences sharedPreferences;
    private Context context;
    public SharedPrefMovieStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
    @Override
    public Movie get() {
        String movieName = sharedPreferences.getString(KEY, "unknown");
        String movieDate = sharedPreferences.getString(DATE_KEY, String.valueOf(LocalDate.now()));
        int movieId = sharedPreferences.getInt(ID_KEY, -1);
        return new Movie(movieId, movieName, movieDate);
    }
    @Override
    public boolean save(Movie movie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, movie.getName());
        editor.putString(DATE_KEY, String.valueOf(LocalDate.now()));
        editor.putInt(ID_KEY, 1);
        return editor.commit();
    }
}
```
В MainActivity был создан экземпляр класса MovieStorage, экземпляр передан в репозиторий:
```
SharedPrefMovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
```
Далее на уровне data была создана модель Movie, содержащая те же поля, что модель на уровне domain и одно доп поле для сохранения даты:
```
public class Movie {
    private int id;
    private String name;
    private String localDate;
    public Movie(int id, String name, String localDate) {
        this.id = id;
        this.name = name;
        this.localDate = localDate;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getLocalDate() {
        return localDate;
    }
}
```
Для дальнейшего удобства модель на уровне domain была переименована в MovieDomain.
Дополнительно для повышения качества кода в реализации MovieRepository на уровне data были созданы методы маппер, которые формируют нужные модели для слоев:
```
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage movieStorage;
    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean saveMovie(MovieDomain movie){
        Movie storageMovie = mapToStorage(movie);
        return movieStorage.save(storageMovie);
    }
    @Override
    public MovieDomain getMovie(){
        Movie movie = movieStorage.get();
        return mapToDomain(movie);
    }
    private Movie mapToStorage(MovieDomain movie){
        String name = movie.getName();
        return new Movie(2, name, LocalDate.now().toString());
    }
    private MovieDomain mapToDomain(Movie movie){
        return new MovieDomain(movie.getId(), movie.getName());
    }
}
```
В конечном счёте проект был разделён на несколько модулей: app, domain, data

<img width="504" height="142" alt="image" src="https://github.com/user-attachments/assets/38d172b1-ea02-4e17-ba85-a6e253304611" />

Для проверки корректности работы приложения после внесённых изменений был проведён запуск приложения.

<img width="465" height="823" alt="Снимок экрана 2025-10-17 124816" src="https://github.com/user-attachments/assets/16a5463b-e7cd-485d-aa47-a1ef3e881f87" />


GreenGuide
---
В рамках данной практической работы в проект были внесены следующие изменения:
1. Разделение на модули app, domain, data
2. Подключение к проекту firebase для реализации логики авторизации в приложении
3. Реализация трёх способов обработки данных: SharedPreferences с информацией о клиенте, Room и класс для работы с сетью с замоканными данными

Был разработан прототип приложения:

<img width="2558" height="852" alt="Прототип приложения" src="https://github.com/user-attachments/assets/ee451f96-49a6-4dec-90e1-6e22c38cee1c" />


Для соблюдения принципов чистой архитектуры на уровне data был создан пакет storage, в который был добавлен класс SharedPrefsUserStorage, реализующий получение и сохранение данных о пользователе, а именно статус пользователя (гость/авторизованный):
```
public class SharedPrefsUserStorage {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_TYPE = "user_type";

    private final SharedPreferences prefs;

    public SharedPrefsUserStorage(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserType(String userType) {
        prefs.edit().putString(KEY_USER_TYPE, userType).apply();
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "guest");
    }
}
```
На уровне domain ранее был создан интерфейс AuthRepository, в него внесены некоторые изменения для дальнейшей работы с fb:
```
public interface AuthRepository {
    boolean login(String email, String password);
    boolean register(String email, String password);
    boolean isAuthorized();
    void logout();

    void saveUserType(String userType);
    String getUserType();
}
```
Реализация данного интерфейса прописана на уровне data. В реализации добавлены методы для регистрации и логина с использованием firebase, проверки статуса пользователя, сохранения и получения статуса пользователя:
```
public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final SharedPrefsUserStorage prefs;

    public AuthRepositoryImpl(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        prefs = new SharedPrefsUserStorage(context);
    }

    @Override
    public boolean login(String email, String password) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            prefs.saveUserType("authorized");
                            Log.d("Auth", "Вход выполнен успешно");
                        } else {
                            Log.e("Auth", "Ошибка входа: " + task.getException());
                        }
                    });
            return true;
        } catch (Exception e) {
            Log.e("AuthRepositoryImpl", "Ошибка login(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean register(String email, String password) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            prefs.saveUserType("authorized");
                            Log.d("Auth", "Регистрация прошла успешно");
                        } else {
                            Log.e("Auth", "Ошибка регистрации: " + task.getException());
                        }
                    });
            return true;
        } catch (Exception e) {
            Log.e("AuthRepositoryImpl", "Ошибка register(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAuthorized() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null;
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
        prefs.saveUserType("guest");
    }

    @Override
    public void saveUserType(String userType) {
        prefs.saveUserType(userType);
    }

    @Override
    public String getUserType() {
        return prefs.getUserType();
    }
}
```
На уровне app была создана новая активность - LoginActivity, настроенная в качестве точки входа после запуска приложения. В данном классе прописана логика заполнения и отображения данных для входа в приложение, а также обработчики событий на кнопках:
```
public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton, guestButton, registerButton;
    private FirebaseAuth auth;

    private AuthRepository repository;
    private LoginUseCase loginUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        guestButton = findViewById(R.id.guestButton);
        registerButton = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(this);
        loginUseCase = new LoginUseCase(repository);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String pass = passwordInput.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            repository.saveUserType("authorized");
                            Toast.makeText(this, "Вход успешен", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Ошибка входа: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        guestButton.setOnClickListener(v -> {
            repository.saveUserType("guest");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
```
При клике на кнопку "Зарегистрироваться" происходит старт RegisterActivity и пользователю загружается экран регистрации. При успешной регистрации происходит автоматический вход в систему:
```
public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button registerButton, backToLoginButton;
    private FirebaseAuth auth;

    private AuthRepository repository;
    private RegisterUseCase registerUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(this);
        registerUseCase = new RegisterUseCase(repository);

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String pass = passwordInput.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            repository.saveUserType("authorized");
                            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Ошибка: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        backToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
```

**Приложение после запуска:**

<img width="454" height="821" alt="Снимок экрана 2025-10-17 170003" src="https://github.com/user-attachments/assets/13012143-3050-4397-8f80-f47131b70f5f" />

**Вход в приложение под статусом гостя:**

<img width="454" height="820" alt="Снимок экрана 2025-10-17 170014" src="https://github.com/user-attachments/assets/165d1a01-3394-4b98-a99d-ec0063597115" />

**Экран регистрации:**

<img width="448" height="814" alt="Снимок экрана 2025-10-17 170109" src="https://github.com/user-attachments/assets/42891504-74ae-4c52-8bf2-ee3d2abf2300" />

**Вход в приложение под статусом авторизованного пользователя:**

<img width="454" height="807" alt="Снимок экрана 2025-10-17 170121" src="https://github.com/user-attachments/assets/14fbf26f-cf72-4b33-8cea-b75d77b8676b" />

На уровне data в директории storage был создан интерфейс PlantStorage:
```
public interface PlantStorage {
    @Query("SELECT * FROM plants")
    List<PlantEntity> getAll();

    @Query("SELECT * FROM plants WHERE id = :id")
    PlantEntity getById(int id);

    @Insert
    void insert(PlantEntity plant);
}
```
Также был создан класс AppDatabase:
```
package ru.mirea.krasikova.data.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mirea.krasikova.data.model.PlantEntity;

@Database(entities = {PlantEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlantStorage plantStorage();
}
```
На уровне data создана модель PlantEntity с такими же полями, как у модели Plant на уровне domain:
```
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
```
В реализацию репозитория PlantRepository были внесены изменения с учётом использования новой модели данных:
```
public class PlantRepositoryImpl implements PlantRepository {
    private final PlantStorage plantStorage;

    public PlantRepositoryImpl(PlantStorage plantStorage) {
        this.plantStorage = plantStorage;
    }

    @Override
    public List<Plant> getPlants() {
        List<PlantEntity> entities = plantStorage.getAll();
        List<Plant> plants = new ArrayList<>();
        for (PlantEntity e : entities) {
            plants.add(new Plant(e.id, e.name, e.description, e.imageUrl, e.dateAdded));
        }
        return plants;
    }

    @Override
    public Plant getPlantById(int id) {
        PlantEntity e = plantStorage.getById(id);
        if (e == null) return null;
        return new Plant(e.id, e.name, e.description, e.imageUrl, e.dateAdded);
    }

    @Override
    public boolean addPlant(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.name = plant.getName();
        entity.description = plant.getDescription();
        entity.imageUrl = plant.getImageUrl();
        entity.dateAdded = plant.getDateAdded();
        plantStorage.insert(entity);
        return true;
    }
}
```
Также на уровне data был создан новый интерфейс OpenMeteoApiService для работы с сетевыми данными:
```
public interface OpenMeteoApiService {
    @GET("v1/forecast?current_weather=true")
    Call<String> getCurrentWeather(
            @Query("latitude") double lat,
            @Query("longitude") double lon
    );
}
```
На уровне data прописана реализация интерфейса WeatherRepository с учётом использования нового интерфейса OpenMeteoApiService:
```
public class WeatherRepositoryImpl implements WeatherRepository {
    private final OpenMeteoApiService api;

    public WeatherRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        api = retrofit.create(OpenMeteoApiService.class);
    }

    @Override
    public WeatherInfo getCurrentWeather(double lat, double lon) {
        try {
            String response = api.getCurrentWeather(lat, lon).execute().body();
            return new WeatherInfo(20.5, "Солнечно");
        } catch (Exception e) {
            return new WeatherInfo(0, "Ошибка: " + e.getMessage());
        }
    }
}
```
