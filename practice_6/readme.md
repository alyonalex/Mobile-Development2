Отчёт по 6 практической работе
----
В рамках данной практической работы был изучен принцип работы с фрагментами (Fragment), который является важным компонентом Android-приложений. 

FragmentApp
---
Для работы был создан новый модуль. В gradle файл модуля добавлена новая зависимость для работы с фрагментами.
```java
implementation("androidx.fragment:fragment:1.8.9")
```
Далее был создан фрагмент, который принимает номер по списку и отображает его в TextView.
```JAVA
public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(BlankFragment.class.getSimpleName(), "onCreateView");
        View view   =  inflater.inflate(R.layout.fragment_blank,  container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        int numberStudent = requireArguments().getInt("my_number_student", 0);
        Log.d(BlankFragment.class.getSimpleName(), String.valueOf(numberStudent));
        TextView tv = view.findViewById(R.id.textNumber);
        tv.setText("Номер по списку: " + numberStudent);
    }
}
```
Далее в MainActivity была реализована логика добавления данного фрагмента с использованием метода newInstance(). Данные передаются с помощью Bundle.

```java
public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putInt("my_number_student", 15);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, BlankFragment.class, args)
                    .commit();
        }
    }
}
```

**Приложение после запуска работает корректно:**

<img width="451" height="815" alt="Снимок экрана 2025-11-11 112730" src="https://github.com/user-attachments/assets/23f5345a-03a4-410f-b7d9-d8291232e043" />

FragmentManagerApp
---
Для работы был создан новый модуль. 
Разработаны два фрагмента: HeaderFragment, который содержит список стран и DetailsFragment, который необходим для отображения информации о выбранной стране.

**HeaderFragment**
```java
public class HeaderFragment extends Fragment {
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ListView listView = view.findViewById(R.id.listView);

        String[] countries = {"Венгрия", "Великобритания", "Германия", "Испания", "Канада", "Марокко",
                "Норвегия", "Португалия", "Россия", "Турция", "ОАЭ", "Уругвай", "Франция"};
        listView.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, countries));

        listView.setOnItemClickListener((AdapterView<?> parent, View v, int pos, long id) ->
                viewModel.setSomeValue(countries[pos]));
    }
}
```
**DetailsFragment**
```java
public class DetailsFragment extends Fragment {
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        TextView tv = view.findViewById(R.id.textDetails);

        viewModel.getSomeValue().observe(getViewLifecycleOwner(), data -> {
            Log.d(DetailsFragment.class.getSimpleName(), data);
            tv.setText("Вы выбрали страну: " + data);
        });
    }
}
```
Связь между данными фрагментами реализована при помощи **SharedViewModel**, экземпляр которой создаётся в обоих классах.
```java
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    public void setSomeValue(String item) {
        selectedItem.setValue(item);
    }
    public LiveData<String> getSomeValue() {
        return selectedItem;
    }
}
```

**Приложение после запуска отображает список стран:**

<img width="463" height="818" alt="Снимок экрана 2025-11-11 112554" src="https://github.com/user-attachments/assets/6da48573-ce70-4f64-8868-92405b99e49d" />


**При клике на страну в нижней части экрана выводится её название:**

<img width="448" height="823" alt="Снимок экрана 2025-11-11 112650" src="https://github.com/user-attachments/assets/28e9b87f-e228-4b88-8c44-0810078ee8cf" />

Далее реализация этого задания была немного изменена. Была создана отдельная модель, которая описывает поля: страна, столица и численность населения.
```java
public class Country {
    private final String name;
    private final String capital;
    private final int population;

    public Country(String name, String capital, int population) {
        this.name = name;
        this.capital = capital;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return name;
    }
}
```
CountryViewModel для передачи данных между фрагментами:
```java
public class CountryViewModel extends ViewModel {
    private final MutableLiveData<Country> selectedCountry = new MutableLiveData<>();

    public void selectCountry(Country country) {
        selectedCountry.setValue(country);
    }

    public LiveData<Country> getSelectedCountry() {
        return selectedCountry;
    }
}
```
В HeaderFragment теперь не создаётся строковый массив данных, а создаётся экземпляр класса Country в формате списка, который заполняется соответствующими данными.
```java
public class HeaderFragment extends Fragment {

    private CountryViewModel viewModel;
    private List<Country> countries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CountryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countries = new ArrayList<>();
        countries.add(new Country("Венгрия", "Будапешт", 9603634 ));
        countries.add(new Country("Германия", "Берлин", 83491249));
        countries.add(new Country("Испания", "Мадрид", 49315949));
        countries.add(new Country("Россия", "Москва", 146119928));
        countries.add(new Country("Португалия", "Лиссабон", 10467366));
        countries.add(new Country("Франция", "Париж", 67421162));

        ListView listView = view.findViewById(R.id.countriesListView);

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                countries
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            Country selectedMovie = countries.get(position);

            viewModel.selectCountry(selectedMovie);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, DetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
```
**DetailsFragment**
```java
public class DetailsFragment extends Fragment {

    private CountryViewModel viewModel;
    private TextView nameTextView;
    private TextView capitalTextView;
    private TextView populationTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CountryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.nameTextView);
        capitalTextView = view.findViewById(R.id.capitalTextView);
        populationTextView = view.findViewById(R.id.populationTextView);

        viewModel.getSelectedCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                nameTextView.setText(country.getName());
                capitalTextView.setText("Столица: " + country.getCapital());
                populationTextView.setText("Численность населения: " + country.getPopulation());
            }
        });
    }
}
```
**Приложение после запуска отображает список стран:**

<img width="463" height="820" alt="Снимок экрана 2025-11-11 120452" src="https://github.com/user-attachments/assets/d1f4b528-8719-4536-83f2-6c21ac7b0204" />

**При клике на страну открывается отдельный экран с подробной информацией:**

<img width="452" height="812" alt="Снимок экрана 2025-11-11 120502" src="https://github.com/user-attachments/assets/959b2bf0-157e-4e2d-b837-d508a6bb84e7" />

ResultApiFragmentApp
---
Для работы был создан новый модуль. 
Разработаны два фрагмента: DataFragment, который необходим для отправки данных (содержит текстовое поле и кнопку) и BottomSheetFragment, который является диалоговым фрагментом и используется для отображения полученных данных.  

**DataFragment**
```java
public class DataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText = view.findViewById(R.id.editTextData);
        Button button = view.findViewById(R.id.buttonOpenSheet);

        button.setOnClickListener(v -> {
            String dataToSend = editText.getText().toString();

            Bundle result = new Bundle();
            result.putString(BottomSheetFragment.BUNDLE_KEY, dataToSend);

            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getChildFragmentManager(), "MyBottomSheetFragment");

            getChildFragmentManager().setFragmentResult(BottomSheetFragment.REQUEST_KEY, result);
        });
    }
}
```

**BottomSheetFragment**
```java
public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "data_request_key";
    public static final String BUNDLE_KEY = "data_bundle_key";
    private TextView textViewResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewResult = view.findViewById(R.id.textViewResult);

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY, this, (requestKey, result) -> {
            String data = result.getString(BUNDLE_KEY);
            textViewResult.setText(data);
        });
    }
}
```
**После отправки данных они отображаются внизу страницы в диалоговом окне:**

<img width="452" height="820" alt="Снимок экрана 2025-11-11 123003" src="https://github.com/user-attachments/assets/f59db73c-d790-4694-b04b-8d7f921f07a5" />

GreenGuide
---
В проекте настроено использование фрагментов вместо Activity и навигация между ними. Теперь существует только MainActivity, которая является точкой входа в приложение и служит контейнером для фрагментов — внутри неё динамически меняются экраны.

Добавлен новый фрагмент "Профиль", переход к которому осуществляется также, как и к остальным экранам через основное меню. Данный фрагмент получает экземпляр Firebase и AuthRepository и проверяет наличие пользователя. Если пользователь вошёл в систему как гость, то в профиле отображается его статус "гость". Если же он авторизовался, то в профиле отображается его статус "авторизованный" и выводится email. Также добавлена кнопка для выхода из аккаунта.

**ProfileFragment**
```java
public class ProfileFragment extends Fragment {

    private TextView userStatusText, UserEmailText;
    private Button btnLogout;
    private AuthRepository authRepository;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userStatusText = view.findViewById(R.id.userStatusText);
        UserEmailText = view.findViewById(R.id.UserEmailText);
        btnLogout = view.findViewById(R.id.btnLogout);

        authRepository = new AuthRepositoryImpl(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userType = authRepository.getUserType();

        if (currentUser != null) {
            userStatusText.setText("Статус: авторизованный");
            authRepository.saveUserType("authorized");
            UserEmailText.setText("Email: " + currentUser.getEmail());
        } else {
            userStatusText.setText("Статус: гость");
            authRepository.saveUserType("guest");
            UserEmailText.setText("Email: ---");
        }

        btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            authRepository.logout();
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        });

        return view;
    }
}
```

**Главный экран приложения после входа:**

<img width="488" height="846" alt="Снимок экрана 2025-11-11 145923" src="https://github.com/user-attachments/assets/fb283372-1c9d-47be-87f3-e2317839c841" />

**Экран профиля:**

<img width="499" height="845" alt="Снимок экрана 2025-11-11 145941" src="https://github.com/user-attachments/assets/a8775013-5bde-4cad-aee9-c981b3708992" />

**Список растений как и раньше отображается корректно:**
<img width="499" height="821" alt="Снимок экрана 2025-11-11 134619" src="https://github.com/user-attachments/assets/26e79432-305a-4081-a0f8-24a2953e6341" />

**Просмотр информации о растении работает корректно:**

<img width="470" height="819" alt="Снимок экрана 2025-11-11 134626" src="https://github.com/user-attachments/assets/a8999dd9-4b08-43c8-9a91-79bcbb2e15bb" />

**Страница для добавление нового растения работает корректно:**

<img width="465" height="821" alt="Снимок экрана 2025-11-11 134711" src="https://github.com/user-attachments/assets/4d0a8c9f-8f3b-42a5-b3d8-8e926411ff66" />

**Страница для просмотра погоды работает корректно:**

<img width="508" height="817" alt="Снимок экрана 2025-11-11 134602" src="https://github.com/user-attachments/assets/b79c4b7e-f48b-4751-96bc-6166ae13f168" />

**Выход из профиля:**

<img width="493" height="847" alt="Снимок экрана 2025-11-11 145950" src="https://github.com/user-attachments/assets/2aa3daac-5682-41c0-a20e-32af38ffb7df" />
