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
import ru.mirea.krasikova.domain.usecases.RegisterUseCase;
import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth auth;
    private AuthRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        repository = new AuthRepositoryImpl(requireContext());
        RegisterUseCase registerUseCase = new RegisterUseCase(repository);

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.usernameInput.getText().toString();
            String pass = binding.passwordInput.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            repository.saveUserType("authorized");
                            Toast.makeText(requireContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_register_to_plantList);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.backToLoginButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigateUp()
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
