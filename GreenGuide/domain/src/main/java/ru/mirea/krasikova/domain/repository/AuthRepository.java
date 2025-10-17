package ru.mirea.krasikova.domain.repository;

public interface AuthRepository {
    boolean login(String email, String password);
    boolean register(String email, String password);
    boolean isAuthorized();
    void logout();

    void saveUserType(String userType);
    String getUserType();
}
