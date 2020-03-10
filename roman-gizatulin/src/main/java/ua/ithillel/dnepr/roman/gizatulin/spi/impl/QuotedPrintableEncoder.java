package ua.ithillel.dnepr.roman.gizatulin.spi.impl;

import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.roman.gizatulin.spi.Encoder;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class QuotedPrintableEncoder implements Encoder {
    private static final String QUOTED_PRINTABLE = "Q";

    @Override
    public byte[] encode(byte[] data) {
        String out;
        try {
            out = MimeUtility.encodeText(
                    new String(data, StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8.toString(),
                    QUOTED_PRINTABLE);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to encode", e);
        }
        return StringUtils.defaultString(out, StringUtils.EMPTY).getBytes();
    }

    @Override
    public byte[] decode(byte[] data) {
        String out;
        try {
            out = MimeUtility.decodeText(
                    new String(data, StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to decode");
        }
        return StringUtils.defaultString(out, StringUtils.EMPTY).getBytes();
    }
}
