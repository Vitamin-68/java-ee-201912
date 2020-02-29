package ua.ithillel.dnepr.roman.gizatulin.spi.impl;

import ua.ithillel.dnepr.roman.gizatulin.spi.Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncoder implements Encoder {
    private static final String QUOTED_PRINTABLE = "Q";

    @Override
    public byte[] encode(byte[] data) {
        try {
            return URLEncoder.encode(
                    new String(data, StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8.toString()).getBytes();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to encode", e);
        }
    }

    @Override
    public byte[] decode(byte[] data) {
        try {
            return URLDecoder.decode(
                    new String(data, StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8.toString()).getBytes();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to decode", e);
        }
    }
}
