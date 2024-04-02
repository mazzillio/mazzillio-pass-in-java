create unique index events_slug_key on events (slug);
create unique index attendees_event_id_email_key on attendees (event_id,email);
create unique index check_ins_attendee_id_key on check_ins (attendee_id);