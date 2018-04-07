package io.snowcamp.badges;

import io.snowcamp.badges.attendees.Attendee;
import io.snowcamp.badges.attendees.AttendeesCsvParser;
import io.snowcamp.badges.configuration.BadgeProperties;
import io.snowcamp.badges.configuration.InputOutputProperties;
import io.snowcamp.badges.generators.BadgeColors;
import io.snowcamp.badges.generators.BadgeGenerator;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author ctranxuan
 */
@SpringBootApplication
public class App implements CommandLineRunner {
    private static Logger LOGGER = getLogger(App.class);
    private final BadgeColors badgeColors;
    private final String fileName;
    private final String outputDirectoryName;

    public App(@Autowired InputOutputProperties aInputOutputProperties,
               @Autowired BadgeProperties aBadgeProperties) {
        requireNonNull(aInputOutputProperties);
        requireNonNull(aBadgeProperties);

        fileName = aInputOutputProperties.getInputFile();
        outputDirectoryName = aInputOutputProperties.getOutputDirectory();
        badgeColors = new BadgeColors(aBadgeProperties.getColors());
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info(() -> "generating the badges...");

        File csvFile = new File(fileName);
        List<Attendee> attendees = new AttendeesCsvParser(';').parse(csvFile);

        LocalDateTime now = LocalDateTime.now();
        File workingDir = new File(new File(outputDirectoryName), now.format(ISO_LOCAL_DATE_TIME));
        List<File> badges = new BadgeGenerator(badgeColors, workingDir).generateBadges(attendees);

        LOGGER.info(() -> "... badges succesfully generated: " + badges.size() + "/" + attendees.size());
    }
}
