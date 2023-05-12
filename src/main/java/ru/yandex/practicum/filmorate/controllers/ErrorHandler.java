package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler extends RuntimeException {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse IdExceptionHandler(final IdException e) {
        return new ErrorResponse(String.format(e.getParameter()));
    }
}
