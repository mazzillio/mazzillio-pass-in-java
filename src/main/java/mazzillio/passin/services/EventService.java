package mazzillio.passin.services;

import lombok.RequiredArgsConstructor;
import mazzillio.passin.Dto.*;
import mazzillio.passin.domain.attendee.Attendee;
import mazzillio.passin.domain.event.Event;
import mazzillio.passin.domain.exceptions.EventFullException;
import mazzillio.passin.domain.exceptions.EventNotFoundException;
import mazzillio.passin.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        var event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvents(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDto createEvent(EventRequestDto eventRequestDto) {
        Event event = new Event();
        event.setTitle(eventRequestDto.title());
        event.setDetails(eventRequestDto.details());
        event.setMaximumAttendees(eventRequestDto.maximumAttendees());
        event.setSlug(createSlug(eventRequestDto.title()));
        this.eventRepository.save(event);
        return new EventIdDto(event.getId());
    }

    public AttendeeIdDto registerAttendeeOnEvent(String eventId, AttendeeRegisterDto registerDto) {
        this.attendeeService.verifyAttendeeSubscription(registerDto.email(), eventId);
        var event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvents(eventId);
        if (event.getMaximumAttendees() <= attendeeList.size()) {
            throw new EventFullException();
        }
        Attendee newAttendee = new Attendee();
        newAttendee.setName(registerDto.name());
        newAttendee.setEmail(registerDto.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        var attendee = this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDto(attendee.getId());
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private String createSlug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
