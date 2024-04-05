package mazzillio.passin.domain.exceptions;

public class AttendeeNotFoundException extends RuntimeException {
    public AttendeeNotFoundException(){
        super("Attendee not found!");
    }
}
