/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.mock.PrimaryConfig;
import com.spektrsoyuz.configadapter.wrapper.ConfigWrapperImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ConfigWrapperImpl}.
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
    void testLoad() throws IOException {
        final ConfigWrapperImpl<PrimaryConfig> configWrapper =
                new ConfigWrapperImpl<>(this.plugin, "config.conf", PrimaryConfig.class);

        // Load the mapped config
        configWrapper.load();

        assertTrue(Files.exists(this.tempDir.resolve("config.conf")));
        assertNotNull(configWrapper.getNode());
        assertNotNull(configWrapper.getConfig());
        assertEquals("default", configWrapper.getConfig().name());
        assertTrue(configWrapper.getConfig().enabled());

        final String content = Files.readString(this.tempDir.resolve("config.conf"));
        assertTrue(content.contains("name"));
        assertTrue(content.contains("default"));
        assertTrue(content.contains("enabled"));
    }

    @Test
    void testLoadPopulatesBlankFile() throws IOException {
        final Path configPath = this.tempDir.resolve("config.conf");
        Files.createFile(configPath);

        final ConfigWrapperImpl<PrimaryConfig> configWrapper =
                new ConfigWrapperImpl<>(this.plugin, "config.conf", PrimaryConfig.class);
        configWrapper.load();

        assertNotNull(configWrapper.getConfig());
        assertEquals("default", configWrapper.getConfig().name());
        assertTrue(configWrapper.getConfig().enabled());

        final String content = Files.readString(configPath);
        assertTrue(content.contains("name"));
        assertTrue(content.contains("default"));
        assertTrue(content.contains("enabled"));
    }

    @Test
    void testLoadAddsMissingEntries() throws IOException {
        final Path configPath = this.tempDir.resolve("config.conf");
        Files.writeString(configPath, "name=custom\n");

        final ConfigWrapperImpl<PrimaryConfig> configWrapper =
                new ConfigWrapperImpl<>(this.plugin, "config.conf", PrimaryConfig.class);
        configWrapper.load();

        assertEquals("custom", configWrapper.getConfig().name());
        assertTrue(configWrapper.getConfig().enabled());

        final String content = Files.readString(configPath);
        assertTrue(content.contains("name"));
        assertTrue(content.contains("custom"));
        assertTrue(content.contains("enabled"));
        assertFalse(content.contains("default"));
    }

    @Test
    void testSave() throws IOException {
        final ConfigWrapperImpl<PrimaryConfig> configWrapper =
                new ConfigWrapperImpl<>(this.plugin, "config.conf", PrimaryConfig.class);
        configWrapper.load();

        // Modify the mapped config
        configWrapper.getConfig().name("updated");

        // Save the mapped config
        configWrapper.save();

        // Read the file
        final String value = Files.readString(this.tempDir.resolve("config.conf"));

        assertTrue(value.contains("name"));
        assertTrue(value.contains("updated"));
    }

}
