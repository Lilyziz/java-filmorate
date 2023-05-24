package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    public boolean isValid(User user) throws BadRequestException {
        throwIfEmailInvalid(user.getEmail());
        throwIfLoginInvalid(user.getLogin());
        throwIfBirthdayInvalid(user.getBirthday());
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    private void throwIfEmailInvalid(String email) throws BadRequestException {
        if (email == null || email == "") {
            throw new BadRequestException("No email");
        }
        if (!email.contains("@")) {
            throw new BadRequestException("Email must be with @");
        }
    }

    private void throwIfLoginInvalid(String login) throws BadRequestException {
        if (login == null || login == "") {
            throw new BadRequestException("No login");
        }
        if (login.contains(" ")) {
            throw new BadRequestException("Login must be without spaces");
        }
    }

    private void throwIfBirthdayInvalid(LocalDate birthdate) throws BadRequestException {
        if (birthdate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Birthday can't be in the future");
        }
    }
}
