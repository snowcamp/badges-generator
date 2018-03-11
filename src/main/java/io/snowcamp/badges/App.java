package io.snowcamp.badges;

import io.snowcamp.badges.attendees.Attendee;
import io.snowcamp.badges.attendees.AttendeesCsvParser;
import io.snowcamp.badges.generators.BadgeGenerator;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author ctranxuan
 */
public class App {
    private static Logger LOGGER = getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info(() -> "generating the badges...");

        String fileName = "participants_snowcamp-2018.csv";
        File csvFile = new File(fileName);
        List<Attendee> attendees = new AttendeesCsvParser(';').parse(csvFile);

        LocalDateTime now = LocalDateTime.now();
        File workingDir = new File(new File("target"), now.format(ISO_LOCAL_DATE_TIME));
        List<File> badges = new BadgeGenerator(workingDir).generateBadges(attendees);

        LOGGER.info(() -> "... badges succesfully generated: " + badges.size() + "/" + attendees.size());
    }
}
