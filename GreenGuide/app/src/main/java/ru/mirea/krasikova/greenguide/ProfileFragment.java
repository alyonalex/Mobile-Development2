package ru.mirea.krasikova.greenguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.greenguide.auth.LoginFragment;

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
