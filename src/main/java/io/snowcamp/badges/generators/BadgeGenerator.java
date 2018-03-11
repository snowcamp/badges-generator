package io.snowcamp.badges.generators;

import io.snowcamp.badges.attendees.Attendee;
import io.snowcamp.badges.attendees.AttendeeFilters;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author ctranxuan
 */
public final class BadgeGenerator {
    private static Logger LOGGER = getLogger(BadgeGenerator.class);

    private final PdfGenerator pdfGenerator;
    private final SvgGenerator svgGenerator;
    private final QrCodeGenerator qrCodeGenerator;
    private final AttendeeFilters attendeeFilters;

    public BadgeGenerator(File workingDir) {
        requireNonNull(workingDir);

        File qrCodeDir = createSubWorkingDirIfNotExist(workingDir,"qrcodes");
        qrCodeGenerator = new QrCodeGenerator(qrCodeDir);

        File svgDir = createSubWorkingDirIfNotExist(workingDir,"svg");
        svgGenerator = new SvgGenerator(svgDir);
        File badgesDir = createSubWorkingDirIfNotExist(workingDir,"badges");

        pdfGenerator = new PdfGenerator(badgesDir);

        attendeeFilters = new AttendeeFilters();
    }

    public List<File> generateBadges(List<Attendee> aAttendees) {
        requireNonNull(aAttendees);

        return aAttendees
                    .stream()
                    .filter(this::filterValidatedAttendee)
                    .filter(this::filterUniversitiesZeroPrice)
                    .limit(1)
                    .peek(a -> LOGGER.info("starting to generate badge for {} {}, {}", a.firstName(), a.lastName(), a.ticket()))
                    .map(this::generateQrCode)
                    .map(this::generateSvgBadge)
                    .map(this::generatePdfBadge)
                    .peek(t -> LOGGER.info("badge successfully generated for {} {}, {}", t._1.firstName(), t._1.lastName(), t._1.ticket()))
                    .map(t -> t._2)
                    .collect(Collectors.toList());
    }

    private Tuple2<Attendee, File> generatePdfBadge(Tuple2<Attendee, File> aAttendeeWithSvg) {
        requireNonNull(aAttendeeWithSvg);
        Attendee attendee = aAttendeeWithSvg._1;
        File pdf = pdfGenerator.pdf(attendee, aAttendeeWithSvg._2);

        LOGGER.info("  pdf successfully generated for {} {}, {}",
                                aAttendeeWithSvg._1.firstName(),
                                aAttendeeWithSvg._1.lastName(),
                                aAttendeeWithSvg._1.ticket());

        return Tuple.of(attendee, pdf);
    }

    private Tuple2<Attendee, File> generateSvgBadge(Tuple2<Attendee, File> aAttendeeWithQrCode) {
        requireNonNull(aAttendeeWithQrCode);
        Attendee attendee = aAttendeeWithQrCode._1;


        File svgFile = svgGenerator.svg(attendee, aAttendeeWithQrCode._2);
        LOGGER.info("  svg successfully generated for {} {}, {}",
                                    aAttendeeWithQrCode._1.firstName(),
                                    aAttendeeWithQrCode._1.lastName(),
                                    aAttendeeWithQrCode._1.ticket());

        return Tuple.of(attendee, svgFile);
    }

    private Tuple2<Attendee, File> generateQrCode(Attendee aAttendee) {
        requireNonNull(aAttendee);

        File qrCodeFile = qrCodeGenerator.qrCode(aAttendee);
        LOGGER.info("  QRCode successfully generated for {} {}, {}", aAttendee.firstName(), aAttendee.lastName(), aAttendee.ticket());
        return Tuple.of(aAttendee, qrCodeFile);
    }

    private File createSubWorkingDirIfNotExist(File aParent, String aDirName) {
        requireNonNull(aDirName);

        File dir = new File(aParent, aDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private boolean filterUniversitiesZeroPrice(Attendee aAttendee) {
        requireNonNull(aAttendee);
        return attendeeFilters.filterUniversitiesZeroPrice(aAttendee);
    }

    private boolean filterValidatedAttendee(Attendee aAttendee) {
        requireNonNull(aAttendee);
        return attendeeFilters.filterValidatedAttendee(aAttendee);
    }
}
