package ru.mirea.krasikova.data.repository;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.krasikova.data.storage.SharedPrefsUserStorage;
import ru.mirea.krasikova.domain.repository.AuthRepository;

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
