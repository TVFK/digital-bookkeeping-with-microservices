package ru.taf.services.exception;

public class BookAlreadyTakenException extends RuntimeException{
    public BookAlreadyTakenException(String message) {
        super(message);
    }
}
