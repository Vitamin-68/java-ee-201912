package ua.ithillel.dnepr.roman.gizatulin.spi;

public interface Encoder {
    byte[] encode(byte[] data);

    byte[] decode(byte[] data);
}
