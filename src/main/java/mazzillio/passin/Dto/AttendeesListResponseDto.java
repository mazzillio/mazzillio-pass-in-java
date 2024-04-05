package mazzillio.passin.Dto;

import lombok.Getter;

import java.util.List;


public record AttendeesListResponseDto(List<AttendeeDetails> attendees) {
}
