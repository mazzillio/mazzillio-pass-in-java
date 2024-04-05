package mazzillio.passin.services;

import lombok.RequiredArgsConstructor;
import mazzillio.passin.Dto.AttendeeBadgeDto;
import mazzillio.passin.Dto.AttendeeBadgeResponseDto;
import mazzillio.passin.Dto.AttendeeDetails;
import mazzillio.passin.Dto.AttendeesListResponseDto;
import mazzillio.passin.domain.attendee.Attendee;
import mazzillio.passin.domain.checkin.CheckIn;
import mazzillio.passin.domain.exceptions.AttendeeAlreadyExistsException;
import mazzillio.passin.domain.exceptions.AttendeeNotFoundException;
import mazzillio.passin.repositories.AttendeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvents(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDto getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvents(eventId);
        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeesListResponseDto(attendeeDetailsList);
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        var isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent()) {
            throw new AttendeeAlreadyExistsException();
        }
    }

    public AttendeeBadgeResponseDto getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        var attendee = getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in")
                .buildAndExpand(attendeeId)
                .toUri()
                .toString();
        return new AttendeeBadgeResponseDto(new AttendeeBadgeDto(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId()));
    }

    public void checkInAttendee(String attendeeId) {
        var attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(AttendeeNotFoundException::new);
    }
}
