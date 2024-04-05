package mazzillio.passin.domain.exceptions;

public class EventFullException extends RuntimeException {
    public EventFullException(){
        super("Events is already Full");
    }
}
