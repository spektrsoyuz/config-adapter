/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ConfigWrapper}.
 *
 * @since 1.0.1
 */
class ConfigWrapperTest {

    @TempDir
    Path tempDir;

    private JavaPlugin plugin;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        this.plugin = mock(JavaPlugin.class);
        final ComponentLogger logger = mock(ComponentLogger.class);

        // Redirect plugin data path to temp directory
        when(this.plugin.getDataPath()).thenReturn(this.tempDir);
        when(this.plugin.getComponentLogger()).thenReturn(logger);
    }

    @Test
    void testLoad() {
        final ConfigWrapper configWrapper = new ConfigWrapper(this.plugin, "config.conf");

        // Load the node
        configWrapper.load();

        assertTrue(Files.exists(this.tempDir.resolve("config.conf")));
        assertNotNull(configWrapper.getNode());
    }

    @Test
    void testSave() throws IOException {
        final ConfigWrapper configWrapper = new ConfigWrapper(this.plugin, "config.conf");
        configWrapper.load();

        // Modify the node
        final var node = configWrapper.getNode();
        node.node("key").set("value");

        // Save the node
        configWrapper.save();

        // Read the file
        final String value = Files.readString(this.tempDir.resolve("config.conf"));

        assertTrue(value.contains("key"));
        assertTrue(value.contains("value"));
    }

}
