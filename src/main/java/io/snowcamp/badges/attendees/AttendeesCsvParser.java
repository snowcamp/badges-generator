package io.snowcamp.badges.attendees;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.snowcamp.badges.attendees.Attendee.Type.from;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.text.WordUtils.capitalize;

/**
 * @author ctranxuan
 */
public final class AttendeesCsvParser {
    private final char csvSeparator;

    public AttendeesCsvParser(char aCsvSeparator) {
        csvSeparator = aCsvSeparator;

    }

    public List<Attendee> parse(File aFile) {
        requireNonNull(aFile);
        List<Attendee> result;

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(csvSeparator)
                .build();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(aFile)).withCSVParser(csvParser).build()) {
            result = reader.readAll()
                    .stream()
                    .skip(1)
                    .map(this::mapToAttendee)
                    .collect(Collectors.toList())
                    ;


        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv file " + aFile.getPath(), e);
        }

        return result;
    }


     private Attendee mapToAttendee(String[] aCsvRow) {
        requireNonNull(aCsvRow);
        return new Attendee.Builder()
                .lastName(capitalizeName(aCsvRow[4]))
                .firstName(capitalizeName(aCsvRow[5]))
                .ticket(aCsvRow[6].replace("\"", "").trim())
                .type(from(aCsvRow[7]))
                .status(Attendee.Status.from(aCsvRow[9].trim()))
                .build();
    }

    private String capitalizeName(final String aName) {
        requireNonNull(aName);
        return capitalize(aName.toLowerCase(), '-', ' ');
    }

}
