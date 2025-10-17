package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.repository.AuthRepository;

public class LoginUseCase {
    private final AuthRepository repository;

    public LoginUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public boolean execute(String email, String password) {
        return repository.login(email, password);
    }
}
