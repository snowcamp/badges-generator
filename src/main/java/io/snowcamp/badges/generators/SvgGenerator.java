package io.snowcamp.badges.generators;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.snowcamp.badges.attendees.Attendee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
public final class SvgGenerator implements WithAttendeeFileName {
    private final File workingDir;

    public SvgGenerator(File aWorkingDir) {
        workingDir = requireNonNull(aWorkingDir);
    }

    public File svg(Attendee aAttendee, File aQrCodeFile) {
        requireNonNull(aAttendee);
        requireNonNull(aQrCodeFile);

        String svgFileName = badgeFileName(aAttendee, ".svg");
        File svgFile = new File(workingDir, svgFileName);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(svgFile))) {
            MustacheFactory mustacheFactory = new DefaultMustacheFactory();
            Mustache mustache = mustacheFactory.compile("template-badge.svg.mustache");

            Map<String, Object> scopes = new HashMap<>();
            if (!"TBD".equalsIgnoreCase(aAttendee.lastName())
                    && !"ACONIT".equalsIgnoreCase(aAttendee.lastName())) {
                scopes.put("firstname", aAttendee.firstName());
                scopes.put("lastname", aAttendee.lastName());

            } else if ("ACONIT".equalsIgnoreCase(aAttendee.lastName())) {
                scopes.put("firstname", "");
                scopes.put("lastname", "ACONIT");

            }

            byte[] bytes = Files.readAllBytes(aQrCodeFile.toPath());
            String qr = "data:image/png;base64," + new String(Base64.getEncoder()
                    .encode(bytes));
            scopes.put("qr", qr);

            switch (aAttendee.type()) {
                case ATTENDEE:
                    scopes.put("color", "#FFCD00");
                    scopes.put("type", "Attendee");
                    break;

                case SPEAKER:
                    scopes.put("color", "#4586FF");
                    scopes.put("type", "Speaker");
                    break;

                case SPONSOR:
                    scopes.put("color", "#ae74d8");
                    scopes.put("type", "Sponsor");
                    break;

                case STAFF:
                    scopes.put("color", "#ee0000");
                    scopes.put("type", "Staff");
                    break;

                default:
                    throw new IllegalArgumentException("unsupported type " + aAttendee.type() + " for " + aAttendee);
            }

            mustache.execute(writer, scopes);
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException("failed to generate svg file " + svgFile.getPath() + " for " + aAttendee,  e);
        }

        return svgFile;
    }

}
