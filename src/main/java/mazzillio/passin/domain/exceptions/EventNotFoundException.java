package mazzillio.passin.domain.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String event) {
        super("Event not found with id" + event);
    }
}
