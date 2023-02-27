package com.tsintergy.ssc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOTest {
    String INIT_FILENAME = "init.properties";
    @Test void readResources() {
        String content = IO.readResources(INIT_FILENAME);
        System.out.println("Content: " + content);
        assertNotNull(content);
        assertTrue(content.contains("删除"));
    }
}
