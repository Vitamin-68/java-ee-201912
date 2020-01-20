package ua.ithillel.dnepr.roman.gizatulin.example;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class Program00 {

    public static void main(String[] args) {
        String uuid = UUID.nameUUIDFromBytes(String.valueOf(new Object()).getBytes()).toString();
        log.info("=== application started ===");
    }
}
