package ru.mirea.krasikova.greenguide.domain.usecases;

import ru.mirea.krasikova.greenguide.domain.repository.AuthRepository;

public class LoginUseCase {
    private final AuthRepository repository;

    public LoginUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public boolean execute(String username, String password) {
        return repository.login(username, password);
    }
}
