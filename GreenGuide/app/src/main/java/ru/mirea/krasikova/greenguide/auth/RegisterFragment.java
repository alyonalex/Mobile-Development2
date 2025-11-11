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
import ru.mirea.krasikova.domain.usecases.RegisterUseCase;
import ru.mirea.krasikova.greenguide.HomeFragment;
import ru.mirea.krasikova.greenguide.R;

public class RegisterFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button registerButton, backToLoginButton;
    private FirebaseAuth auth;
    private AuthRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        registerButton = view.findViewById(R.id.registerButton);
        backToLoginButton = view.findViewById(R.id.backToLoginButton);

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(requireContext());
        RegisterUseCase registerUseCase = new RegisterUseCase(repository);

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String pass = passwordInput.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            repository.saveUserType("authorized");
                            Toast.makeText(requireContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show();

                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new HomeFragment())
                                    .commit();
                        } else {
                            Toast.makeText(requireContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        backToLoginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
