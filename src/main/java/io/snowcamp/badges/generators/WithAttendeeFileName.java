package io.snowcamp.badges.generators;

import io.snowcamp.badges.attendees.Attendee;

import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
interface WithAttendeeFileName {

    default String badgeFileName(Attendee aAttendee, String aFileExtension) {
        requireNonNull(aAttendee);
        requireNonNull(aFileExtension);

        // homonyms can occur! Hence, we append the ticket
        return (aAttendee.lastName() + "_" + aAttendee.firstName() + "_" + aAttendee.ticket())
                // some people put '/' for two people
                .replace('/', '_')
                + aFileExtension;
    }



}
