package io.snowcamp.badges.attendees;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


/**
 * @author ctranxuan
 */
public final class Attendee {
    public enum Status {
        VALID("Validé"),
        CANCEL("Annulé");

        private final static Map<String, Status> LOOKUP = HashMap.of(VALID.name, VALID,
                CANCEL.name, CANCEL);

        private final String name;

        Status(final String aName) {
            name = aName;
        }

        public static Status from(final String aName) {
            return LOOKUP.get(aName)
                    .getOrElseThrow(() -> new IllegalArgumentException("unsupported status:" + aName));

        }
    }

    public enum Type {
        ATTENDEE,
        SPEAKER,
        SPONSOR,
        STAFF;

        public static Type from(final String aType) {
            requireNonNull(aType);
            Type result;

            if (aType.startsWith("Sponsor stand")) {
                result = SPONSOR;

            } else if (aType.startsWith("Speaker")) {
                result = SPEAKER;

            } else if (aType.startsWith("Staff")) {
                result = STAFF;

            } else {
                result = ATTENDEE;

            }

            return result;
        }
    }

    public static class Builder {

        private String lastName;
        private String firstName;
        private Status status;
        private Type type;
        private String ticket;

        public Builder lastName(String aLastName) {
            lastName = aLastName;
            return this;
        }

        public Builder firstName(String aFirstName) {
            firstName = aFirstName;
            return this;
        }

        public Builder status(Status aStatus) {
            status = aStatus;
            return this;
        }

        public Builder type(Type aType) {
            type = aType;
            return this;
        }

        public Builder ticket(String aTicket) {
            ticket = aTicket;
            return this;
        }

        public Attendee build() {
            return new Attendee(this);
        }

    }

    private final String lastName;
    private final String firstName;
    private final Status status;
    private final Type type;
    private final String ticket;

    private Attendee(Builder aBuilder) {
        lastName = requireNonNull(aBuilder.lastName);
        firstName = requireNonNull(aBuilder.firstName);
        status = requireNonNull(aBuilder.status);
        type = requireNonNull(aBuilder.type);
        ticket = requireNonNull(aBuilder.ticket);
    }

    public String lastName() {
        return lastName;
    }

    public String firstName() {
        return firstName;
    }

    public Status status() {
        return status;
    }

    public Type type() {
        return type;
    }

    public String ticket() {
        return ticket;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;
        Attendee attendee = (Attendee) aO;
        return Objects.equals(lastName, attendee.lastName) &&
                Objects.equals(firstName, attendee.firstName) &&
                status == attendee.status &&
                type == attendee.type &&
                Objects.equals(ticket, attendee.ticket);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lastName, firstName, status, type, ticket);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
