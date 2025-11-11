package ru.mirea.krasikova.greenguide.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.domain.usecases.LoginUseCase;
import ru.mirea.krasikova.greenguide.HomeFragment;
import ru.mirea.krasikova.greenguide.R;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton, guestButton, registerButton;
    private FirebaseAuth auth;
    private AuthRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        guestButton = view.findViewById(R.id.guestButton);
        registerButton = view.findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(requireContext());
        LoginUseCase loginUseCase = new LoginUseCase(repository);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String pass = passwordInput.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    repository.saveUserType("authorized");
                    Toast.makeText(requireContext(), "Вход успешен", Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                } else {
                    Toast.makeText(requireContext(), "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        guestButton.setOnClickListener(v -> {
            repository.saveUserType("guest");
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        });

        registerButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
