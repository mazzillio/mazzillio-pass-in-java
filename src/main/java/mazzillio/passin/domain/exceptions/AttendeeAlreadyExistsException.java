package mazzillio.passin.domain.exceptions;

public class AttendeeAlreadyExistsException extends RuntimeException{
    public AttendeeAlreadyExistsException(){
        super("Attendee already registered in event");
    }
}
