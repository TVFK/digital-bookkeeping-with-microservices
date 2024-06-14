package ru.taf.services.exceptions;

public class BookAlreadyTakenException extends RuntimeException{
    public BookAlreadyTakenException(String message) {
        super(message);
    }
}
