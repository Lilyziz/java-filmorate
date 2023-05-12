package ru.yandex.practicum.filmorate.exception;

public class IdException extends RuntimeException {
    private final String parameter;
    public IdException(String parameter) {
        this.parameter = parameter;
    }
    public String getParameter() {
        return parameter;
    }
}
