package ru.taf.client.exceptions;

public class BookAlreadyTakenException extends RuntimeException{
    public BookAlreadyTakenException(String message) {
        super(message);
    }
}
