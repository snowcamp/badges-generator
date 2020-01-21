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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.snowcamp.badges.attendees.Attendee.Type.*;
import static io.snowcamp.badges.generators.SvgVariablesTemplate.*;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * @author ctranxuan
 */
public final class SvgGenerator implements WithAttendeeFileName {
    private static final String SVG_BADGE_TEMPLATE = "template-badge.svg.mustache";
    private static final String SVG_EXTENSION = ".svg";

    private final File workingDir;
    private final BadgeColors badgeColors;

    public SvgGenerator(BadgeColors aBadgeColors, File aWorkingDir) {
        badgeColors = requireNonNull(aBadgeColors);
        workingDir = requireNonNull(aWorkingDir);
    }

    public File svg(Attendee aAttendee, File aQrCodeFile) {
        requireNonNull(aAttendee);
        requireNonNull(aQrCodeFile);

        String svgFileName = badgeFileName(aAttendee, SVG_EXTENSION);
        File svgFile = new File(workingDir, svgFileName);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(svgFile))) {
            MustacheFactory mustacheFactory = new DefaultMustacheFactory();
            Mustache mustache = mustacheFactory.compile(SVG_BADGE_TEMPLATE);

            Map<String, Object> scopes = new HashMap<>();
            if (!"TBD".equalsIgnoreCase(aAttendee.lastName())
                    && !"SNOWCAMP".equalsIgnoreCase(aAttendee.lastName())) {
                scopes.put(FIRST_NAME, aAttendee.firstName());
                scopes.put(LAST_NAME, aAttendee.lastName());

            }

            byte[] bytes = Files.readAllBytes(aQrCodeFile.toPath());
            String qr = "data:image/png;base64," + new String(Base64.getEncoder()
                    .encode(bytes));
            scopes.put("qr", qr);

            switch (aAttendee.type()) {
                case ATTENDEE:
                    scopes.put(COLOR, badgeColors.color(ATTENDEE));
                    scopes.put(TYPE, "Attendee");
                    break;

                case SPEAKER:
                    scopes.put(COLOR, badgeColors.color(SPEAKER));
                    scopes.put(TYPE, "Speaker");
                    break;

                case SPONSOR:
                    scopes.put(COLOR, badgeColors.color(SPONSOR));
                    scopes.put(TYPE, "Sponsor");
                    break;

                case STAFF:
                    scopes.put(COLOR, badgeColors.color(STAFF));
                    scopes.put(TYPE, "Staff");
                    break;

                default:
                    throw new IllegalArgumentException("unsupported type " + aAttendee.type() + " for " + aAttendee);
            }

            final List<String> universities = aAttendee.universities();
            if (!universities.isEmpty()) {
                scopes.put(UNIVERSITY1, universities.get(0));
                if (universities.size() > 1) { // FIXME refactor
                    scopes.put(UNIVERSITY2, universities.get(1));
                }
            }

            mustache.execute(writer, scopes);
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException("failed to generate svg file " + svgFile.getPath() + " for " + aAttendee,  e);
        }

        return svgFile;
    }

}
