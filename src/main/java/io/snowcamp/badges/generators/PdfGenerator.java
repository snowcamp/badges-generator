package io.snowcamp.badges.generators;

import de.bripkens.svgexport.SVGExport;
import io.snowcamp.badges.attendees.Attendee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static de.bripkens.svgexport.Format.PDF;
import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
public final class PdfGenerator implements WithAttendeeFileName {
    private final File workingDir;

    public PdfGenerator(File aWorkingDir) {
        workingDir = requireNonNull(aWorkingDir);
    }

    public File pdf(Attendee aAttendee, File aSvgFile) {
        requireNonNull(aAttendee);
        requireNonNull(aSvgFile);

        String badgeName = badgeFileName(aAttendee, ".pdf");
        File pdfFile = new File(workingDir, badgeName);

        try (FileInputStream in = new FileInputStream(aSvgFile); FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            new SVGExport().setInput(in)
                    .setOutput(outputStream)
                    .setTranscoder(PDF)
                    .transcode();

        } catch (Exception e) {
            throw new RuntimeException("failed to generate pdf file " + pdfFile.getPath() + " for " + aAttendee,  e);

        }

        return pdfFile;
    }
}
