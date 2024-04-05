package mazzillio.passin.config;

import mazzillio.passin.Dto.ErrorResponseDto;
import mazzillio.passin.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(AttendeeAlreadyExistsException.class)
    public ResponseEntity<Object> handleAttendeeAlreadyExistsConflict(AttendeeAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity<Object> handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<Object> handleEventFullBadRequest(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(AttendeeAlreadyCheckInException.class)
    public ResponseEntity<Object> handleAttendeeAlreadyCheckInConflict(AttendeeAlreadyCheckInException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
