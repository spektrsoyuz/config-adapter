/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ConfigManager}.
 *
 * @since 1.0.1
 */
class ConfigManagerTest {

    @TempDir
    Path tempDir;

    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        final JavaPlugin plugin = mock(JavaPlugin.class);
        final ComponentLogger logger = mock(ComponentLogger.class);

        // Redirect plugin data path to temp directory
        when(plugin.getDataPath()).thenReturn(this.tempDir);
        when(plugin.getComponentLogger()).thenReturn(logger);

        // Initialize the config manager
        this.configManager = new ConfigManager(plugin);
    }

    @Test
    void testAddConfig() {
        // Add config
        this.configManager.addConfig("config.conf");

        assertFalse(this.configManager.isLoaded());

        // Load config manager
        this.configManager.load();

        assertTrue(this.configManager.isLoaded());
        assertTrue(Files.exists(this.tempDir.resolve("config.conf")));
    }

    @Test
    void testSetAndGet() {
        // Load config
        final String configKey = "config.conf";
        this.configManager.addConfig(configKey);
        this.configManager.load();

        // Save a value to the config
        this.configManager.set(configKey, "key", String.class, "value");

        // Retrieve the value
        final String value = this.configManager.get(configKey, "key", String.class, null);
        assertEquals("value", value);
    }

    @Test
    void testSetAndGetList() {
        // Load config
        final String configKey = "config.conf";
        this.configManager.addConfig(configKey);
        this.configManager.load();

        // Save a list to the config
        final List<String> expectedList = List.of("value1", "value2");
        this.configManager.setList(configKey, "key", String.class, List.of("value1", "value2"));

        // Retrieve the list
        final List<String> list = this.configManager.getList(configKey, "key", String.class);

        assertNotNull(list);
        assertEquals(expectedList.size(), list.size());
        assertTrue(list.containsAll(expectedList));
    }

}
