package ru.mirea.krasikova.greenguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.greenguide.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AuthRepository authRepository;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        authRepository = new AuthRepositoryImpl(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            binding.userStatusText.setText("Статус: авторизованный");
            authRepository.saveUserType("authorized");
            binding.UserEmailText.setText("Email: " + currentUser.getEmail());
        } else {
            binding.userStatusText.setText("Статус: гость");
            authRepository.saveUserType("guest");
            binding.UserEmailText.setText("Email: ---");
        }

        binding.btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            authRepository.logout();
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.loginFragment);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
