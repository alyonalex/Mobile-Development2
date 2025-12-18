Отчёт по 7 практической работе
----
В рамках данной практической работы были изучены принципы работы с навигацией в разработке Android-приложений.

BottomNavigationApp - навигация через нижнюю панель
---
1. Для работы был создан новый модуль. В gradle файл модуля добавлены зависимости, необходимые для работы с навигацией, также подключен ViewBinding.
2. Созданы три фрагмента: HomeFragment, который выводит приветствие, InfoFragment, который содержит информацию и ProfileFragment, который выводит личные данные (имя, фамилию и номер группы). Для них настроены файлы разметки.
3. Создан граф навигации mobile_navigation.xml, который объединяет данные фрагменты. ID фрагментов в графе синхронизированы с ID пунктов меню для автоматической навигации.

```xml
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/navigation"
    app:startDestination="@id/navigation_home">

    <fragment
        android:id="@+id/navigation_home"
        android:name="ru.mirea.krasikova.bottomnavigationapp.HomeFragment"
        android:label="Home"
        tools:layout="@layout/fragment_home" />

    <fragment
        android:id="@+id/navigation_info"
        android:name="ru.mirea.krasikova.bottomnavigationapp.InfoFragment"
        android:label="Info"
        tools:layout="@layout/fragment_info" />

    <fragment
        android:id="@+id/navigation_profile"
        android:name="ru.mirea.krasikova.bottomnavigationapp.ProfileFragment"
        android:label="Profile"
        tools:layout="@layout/fragment_profile" />
</navigation>
```

4. В MainActivity инициализирован NavController для навигации между фрагментами, к которому подключён BottomNavigationView.
```JAVA
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // NavController для навигации между фрагментами
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Подключение BottomNavigationView к NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
```

5. Настроены кастомные цвета для темы.

**Экран главной страницы:**

<img width="428" height="816" alt="Снимок экрана 2025-12-18 195231" src="https://github.com/user-attachments/assets/441cd2b2-4205-4cb3-8a3c-1b47ca13f53f" />

**Экран страницы с информацией:**

<img width="428" height="820" alt="Снимок экрана 2025-12-18 195400" src="https://github.com/user-attachments/assets/7134dacc-76fb-4cc2-9079-045eb639a2f1" />

**Экран профиля:**

<img width="434" height="812" alt="Снимок экрана 2025-12-18 195408" src="https://github.com/user-attachments/assets/cb56d4d8-4c9a-4842-af02-da9dedb61049" />


NavigationDrawerApp - навигация через шторку
---
1. Для работы был создан новый модуль. В gradle файл модуля добавлены зависимости, необходимые для работы с навигацией, также подключен ViewBinding.
2. Аналогично, как в прошлом модуле созданы три фрагмента и настроены файлы разметки.
3. Настроены файлы nav_header_main.xml, content_main.xml, app_bar_main.xml и activity_main.xml.
4. В MainActivity инициализирован компонент AppBarConfiguration, который хранит информацию о верхнеуровневых экранах. Toolbar подключён как ActionBar. Инициализирован NavController, который управляет переходами между фрагментами.
5. Настроены кастомные цвета для темы

**Навигационная панель-шторка в приложении:**

<img width="461" height="818" alt="Снимок экрана 2025-12-18 210945" src="https://github.com/user-attachments/assets/3c7f3b6d-2b5f-423e-adc1-25dd1bc3403a" />

**Экран главной страницы:**

<img width="430" height="815" alt="Снимок экрана 2025-12-18 210954" src="https://github.com/user-attachments/assets/8e9bd83e-9c4b-4f5b-aae1-abb398ad8374" />

**Экран страницы с информацией:**

<img width="438" height="808" alt="Снимок экрана 2025-12-18 211004" src="https://github.com/user-attachments/assets/8e958a9c-b7d0-40da-932a-da5ecdd6c484" />

**Экран профиля:**

<img width="455" height="813" alt="Снимок экрана 2025-12-18 211016" src="https://github.com/user-attachments/assets/498cc5f5-666a-40e9-8ca1-1a801ea76595" />


GreenGuide
---
В проекте настроено использование навигации Bottom Navigation.
1. В gradle файл модуля добавлены зависимости, необходимые для работы с навигацией, также подключен ViewBinding.
2. Добавлено меню навигации bottom_nav_menu.xml, которое содержит 4 пункта: "профиль", "растения", "добавить" и "погода". ID пунктов меню синхронизированы с ID фрагментов в графе для автоматической навигации.

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/profileFragment"
        android:title="Профиль"
        android:icon="@drawable/ic_profile"/>

    <item
        android:id="@+id/plantListFragment"
        android:title="Растения"
        android:icon="@drawable/ic_plant"/>

    <item
        android:id="@+id/addPlantFragment"
        android:title="Добавить"
        android:icon="@drawable/ic_add"/>

    <item
        android:id="@+id/weatherFragment"
        android:title="Погода"
        android:icon="@drawable/ic_weather"/>

</menu>
```

4. Добавлен навигационный граф nav_graph_main.xml, описывающий логику переходов между фрагментами в приложении.

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/loginFragment">

    <!-- Экран авторизации -->
    <fragment
        android:id="@+id/loginFragment"
        android:name="ru.mirea.krasikova.greenguide.auth.LoginFragment" >
        <action
            android:id="@+id/action_login_to_plantList"
            app:destination="@id/plantListFragment"
            app:popUpTo="@id/loginFragment"
            app:popUpToInclusive="true" />
        <action
            android:id="@+id/action_login_to_register"
            app:destination="@id/registerFragment" />
    </fragment>

    <!-- Экран регистрации -->
    <fragment
        android:id="@+id/registerFragment"
        android:name="ru.mirea.krasikova.greenguide.auth.RegisterFragment" >
        <action
            android:id="@+id/action_register_to_plantList"
            app:destination="@id/plantListFragment"
            app:popUpTo="@id/loginFragment"
            app:popUpToInclusive="true"/>
    </fragment>

    <!-- Список растений -->
    <fragment
        android:id="@+id/plantListFragment"
        android:name="ru.mirea.krasikova.greenguide.plants.PlantListFragment">
        <action
            android:id="@+id/action_list_to_detail"
            app:destination="@id/plantDetailFragment"/>
    </fragment>

    <!-- Детали растения -->
    <fragment
        android:id="@+id/plantDetailFragment"
        android:name="ru.mirea.krasikova.greenguide.plants.PlantDetailFragment">
        <argument
            android:name="plant_id"
            app:argType="integer"/>
    </fragment>

    <!-- Добавление растения -->
    <fragment
        android:id="@+id/addPlantFragment"
        android:name="ru.mirea.krasikova.greenguide.plants.AddPlantFragment" />

    <!-- Погода -->
    <fragment
        android:id="@+id/weatherFragment"
        android:name="ru.mirea.krasikova.greenguide.weather.WeatherFragment"/>

    <!-- Профиль -->
    <fragment
        android:id="@+id/profileFragment"
        android:name="ru.mirea.krasikova.greenguide.ProfileFragment" />

</navigation>
```

5. Код фрагментов переписан с использованием navController вместо FragmentManager.
6. В MainActivity инициализирован компонент NavController, который контролирует переход между фрагментами, к нему подключен BottomNavigationView. Также осуществляется проверка текущего фрагмента, чтобы скрыть BottomNavigation для фрагментов авторизации и регистрации. Проверяется тип пользователя авторизованный/гость, чтобы скрыть/предоставить доступ к фрагменту для добавления нового растения.

```java
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepositoryImpl(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Подключение BottomNavigationView к NavController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        // Проверка текущего фрагмента, чтобы скрыть BottomNavigation для авторизации и регистрации
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            if (destId == R.id.loginFragment || destId == R.id.registerFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });

        // Проверка доступа к AddPlantFragment
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.addPlantFragment) {
                String userType = authRepository.getUserType();
                if (!"authorized".equals(userType)) {
                    android.widget.Toast.makeText(this,
                            "Только авторизованные пользователи могут добавлять растения",
                            android.widget.Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });
    }
}
```

**После авторизации в приложении открывается список растений**

<img width="473" height="846" alt="Снимок экрана 2025-12-18 172031" src="https://github.com/user-attachments/assets/33453c67-3641-4a06-a3c4-7aec31b58f77" />


**У гостевого пользователя отсутствует возможность перейти во вкладку "добавить"**

<img width="472" height="845" alt="Снимок экрана 2025-12-18 172043" src="https://github.com/user-attachments/assets/98a74c56-a375-4ea3-907d-8555d9138ab3" />


**Страница для просмотра погоды:**

<img width="505" height="849" alt="Снимок экрана 2025-12-18 172058" src="https://github.com/user-attachments/assets/2b5ac458-c6ed-40a3-bc27-3b750fa23a11" />



