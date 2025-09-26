package ru.mirea.krasikova.greenguide.domain.repository;

public interface AuthRepository {
    boolean login(String username, String password);
    boolean isAuthorized();
}
