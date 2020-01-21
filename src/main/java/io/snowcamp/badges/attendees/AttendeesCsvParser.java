package io.snowcamp.badges.attendees;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public List<Attendee> parse(File aFile, final Map<String, List<String>> univPerTickets) {
        requireNonNull(aFile);
        List<Attendee> result;

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(csvSeparator)
                .build();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(aFile)).withCSVParser(csvParser).build()) {
            result = reader.readAll()
                    .stream()
                    .skip(1)
                    .map(row -> mapToAttendee(row, univPerTickets))
                    .collect(Collectors.toList())
                    ;


        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv file " + aFile.getPath(), e);
        }

        return result;
    }


     private Attendee mapToAttendee(String[] aCsvRow, final Map<String, List<String>> univPerTickets) {
        requireNonNull(aCsvRow);
         final String ticket = aCsvRow[9].replace("\"", "").trim();
         return new Attendee.Builder()
                            .lastName(capitalizeName(aCsvRow[7]))
                            .firstName(capitalizeName(aCsvRow[8]))
                            .ticket(ticket)
                            .type(from(aCsvRow[11]))
                            .status(Attendee.Status.from(aCsvRow[13].trim()))
                            .universities(univPerTickets.get(ticket))
                            .build();
    }

    private String capitalizeName(final String aName) {
        requireNonNull(aName);
        return capitalize(aName.toLowerCase(), '-', ' ');
    }

}
