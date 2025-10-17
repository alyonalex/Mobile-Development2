package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.repository.AuthRepository;

public class RegisterUseCase {
    private final AuthRepository repository;

    public RegisterUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public boolean execute(String email, String password) {
        return repository.register(email, password);
    }
}
