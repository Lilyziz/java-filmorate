package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    public boolean isValid(User user) throws ValidationException {
        emailValidator(user.getEmail());
        loginValidator(user.getLogin());
        birthdayValidator(user.getBirthday());
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    private boolean emailValidator(String email) throws ValidationException {
        if (email == null || email == "") {
            throw new ValidationException("No email");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email must be with @");
        }
        return true;
    }

    private boolean loginValidator(String login) throws ValidationException {
        if (login == null || login == "") {
            throw new ValidationException("No login");
        }
        if (login.contains(" ")) {
            throw new ValidationException("Login must be without spaces");
        }
        return true;
    }

    private boolean birthdayValidator(LocalDate birthdate) throws ValidationException {
        if (birthdate.isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday can't be in the future");
        }
        return true;
    }
}
