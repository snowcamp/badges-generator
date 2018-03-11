package io.snowcamp.badges.generators;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.snowcamp.badges.attendees.Attendee;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import static com.google.zxing.EncodeHintType.CHARACTER_SET;
import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * @author ctranxuan
 */
public final class QrCodeGenerator implements WithAttendeeFileName {
    private final File workingDir;

    public QrCodeGenerator(File aWorkingDir) {
        workingDir = requireNonNull(aWorkingDir);
    }

    public File qrCode(Attendee aAttendee) {
        requireNonNull(aAttendee);
        String qrCodeFileName = badgeFileName(aAttendee, ".png");
        File qrCodeFile = new File(workingDir, qrCodeFileName);

        try {

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(ERROR_CORRECTION, H);
            hints.put(EncodeHintType.QR_VERSION, 1);
            hints.put(CHARACTER_SET, UTF_8.name());

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(aAttendee.ticket(), BarcodeFormat.QR_CODE, 120, 120);

            Path path = FileSystems.getDefault().getPath(qrCodeFile.getPath());

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


        } catch (IOException | WriterException e) {
            throw new RuntimeException("failed to generate QRCode file " + qrCodeFile.getPath() + " for " + aAttendee,  e);

        }

        return qrCodeFile;
    }
}
