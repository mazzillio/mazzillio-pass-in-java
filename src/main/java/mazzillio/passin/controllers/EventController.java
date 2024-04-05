package mazzillio.passin.controllers;

import lombok.RequiredArgsConstructor;
import mazzillio.passin.Dto.*;
import mazzillio.passin.services.AttendeeService;
import mazzillio.passin.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        var event = this.eventService.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDto> createEvent(@RequestBody EventRequestDto eventRequestDto, UriComponentsBuilder uriComponentsBuilder) {
        var event = this.eventService.createEvent(eventRequestDto);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.eventId()).toUri();
        return ResponseEntity.created(uri).body(event);
    }
    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeesListResponseDto> getEventAttendees(@PathVariable String eventId){
        var event = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDto> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRegisterDto attendeeRegisterDto, UriComponentsBuilder uriComponentsBuilder) {
        var register = this.eventService.registerAttendeeOnEvent(eventId,attendeeRegisterDto);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(register.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(register);
    }
}
