package mazzillio.passin.domain.exceptions;

public class AttendeeAlreadyCheckInException extends RuntimeException {
    public AttendeeAlreadyCheckInException(){
        super("Attendee already check-in");
    }
}
