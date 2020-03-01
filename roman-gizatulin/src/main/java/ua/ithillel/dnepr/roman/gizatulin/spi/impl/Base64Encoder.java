package ua.ithillel.dnepr.roman.gizatulin.spi.impl;

import ua.ithillel.dnepr.roman.gizatulin.spi.Encoder;

import java.util.Base64;

public class Base64Encoder implements Encoder {
    @Override
    public byte[] encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    @Override
    public byte[] decode(byte[] data) {
        return Base64.getDecoder().decode(data);
    }
}
