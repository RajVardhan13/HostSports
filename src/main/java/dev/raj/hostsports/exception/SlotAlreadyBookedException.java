package dev.raj.hostsports.exception;

public class SlotAlreadyBookedException extends RuntimeException{
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
