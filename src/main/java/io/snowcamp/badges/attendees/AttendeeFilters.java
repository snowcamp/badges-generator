package io.snowcamp.badges.attendees;

import static io.snowcamp.badges.attendees.Attendee.Status.VALID;
import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
public final class AttendeeFilters {

    public boolean filterUniversitiesZeroPrice(Attendee aAttendee) {
        requireNonNull(aAttendee);
        return !"University early bird (0,00€)".equals(aAttendee.type())
                || !"University (0,00€)".equals(aAttendee.type());
    }

    public boolean filterValidatedAttendee(Attendee aAttendee) {
        requireNonNull(aAttendee);
        return aAttendee.status().equals(VALID);
    }
}
