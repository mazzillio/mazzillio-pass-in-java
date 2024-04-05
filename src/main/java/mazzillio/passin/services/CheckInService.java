package mazzillio.passin.services;

import lombok.RequiredArgsConstructor;
import mazzillio.passin.domain.attendee.Attendee;
import mazzillio.passin.domain.checkin.CheckIn;
import mazzillio.passin.domain.exceptions.AttendeeAlreadyCheckInException;
import mazzillio.passin.repositories.CheckInRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkInRepository.save(newCheckIn);

    }

    private void verifyCheckInExists(String id) {
        if(getCheckIn(id).isPresent()) {
            throw new AttendeeAlreadyCheckInException();
        }
    }

    public Optional<CheckIn> getCheckIn(String id) {
        return this.checkInRepository.findByAttendeeId(id);
    }
}
