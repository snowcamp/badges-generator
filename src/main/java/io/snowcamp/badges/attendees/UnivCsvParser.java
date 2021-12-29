package io.snowcamp.badges.attendees;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class UnivCsvParser {
    private final char csvSeparator;

    public UnivCsvParser(final char csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    public Map<String, List<String>> parse(Path file) {
        requireNonNull(file);
        Map<String, List<String>> result;

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(csvSeparator)
                .build();

        List<String> toto = new ArrayList<>();
        toto.stream()
            .map(s -> Pair.of(s, s))
            .map(p -> p)
            .collect(groupingBy(Pair::getKey));

        try(CSVReader reader = new CSVReaderBuilder(new FileReader(file.toFile())).withCSVParser(csvParser).build()) {
            result =
                    reader.readAll()
                           .stream()
                           .skip(1) // header
                           .map(this::mapToTicketUnivPair)
                           .collect(groupingBy(Pair::getKey, mapping(Pair::getValue, toList())))
                           ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("no csv file found: " + file, e);
        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv file " + file, e);
        }
        return result;
    }

    private Pair<String, String> mapToTicketUnivPair(final String[] csvRow) {
        requireNonNull(csvRow);
        // Pair.of("ticket number", "name of the University")
        return Pair.of(csvRow[5].replace("\"", "").trim(), csvRow[0]);
    }
}
