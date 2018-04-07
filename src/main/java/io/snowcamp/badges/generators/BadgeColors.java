package io.snowcamp.badges.generators;

import io.snowcamp.badges.attendees.Attendee.Type;

import java.util.EnumMap;

import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
public class BadgeColors {
    private final EnumMap<Type, String> colors;

    public BadgeColors(EnumMap<Type, String> aColors) {
        colors = requireNonNull(aColors);
    }

    public String color(Type aAttendeeType) {
        requireNonNull(aAttendeeType);
        return colors.get(aAttendeeType);
    }
}
