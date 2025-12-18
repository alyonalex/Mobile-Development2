package ru.mirea.krasikova.greenguide.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.domain.usecases.LoginUseCase;
import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth auth;
    private AuthRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(requireContext());
        LoginUseCase loginUseCase = new LoginUseCase(repository);

        // Вход через email и пароль
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.usernameInput.getText().toString();
            String pass = binding.passwordInput.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            repository.saveUserType("authorized");
                            Toast.makeText(requireContext(), "Вход успешен", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_login_to_plantList);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Ошибка входа: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Гостевой вход
        binding.guestButton.setOnClickListener(v -> {
            repository.saveUserType("guest");
            Navigation.findNavController(view).navigate(R.id.action_login_to_plantList);
        });

        // Переход на регистрацию
        binding.registerButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_login_to_register)
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
