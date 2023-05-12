package ru.yandex.practicum.filmorate.exception;

public class InternalServerError extends RuntimeException {
    private final String parameter;

    public InternalServerError(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
