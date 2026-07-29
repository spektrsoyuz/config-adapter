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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        // Get the value
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

        // Get the list
        final List<String> list = this.configManager.getList(configKey, "key", String.class);
        assertNotNull(list);
        assertEquals(expectedList.size(), list.size());
        assertTrue(list.containsAll(expectedList));
    }

    @Test
    void testDefault() {
        // Load config
        final String configKey = "config.conf";
        this.configManager.addConfig(configKey);
        this.configManager.load();

        // Attempt to get the value
        final String value = this.configManager.get(configKey, "key", String.class, "default");
        assertEquals("default", value);
    }

    @Test
    void testDefaultList() {
        // Load config
        final String configKey = "config.conf";
        this.configManager.addConfig(configKey);
        this.configManager.load();

        // Attempt to get the list
        final List<String> list = this.configManager.getList(configKey, "key", String.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void testMissing() {
        final String configKey = "config.conf";

        // Attempt to get the value
        final String value = this.configManager.get(configKey, "key", String.class, null);
        assertNull(value);
    }

    @Test
    void testMissingList() {
        final String configKey = "config.conf";

        // Attempt to get the list
        final List<String> list = this.configManager.getList(configKey, "key", String.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void testLoadAndSave() throws IOException {
        // Add configs
        final String configKey = "config.conf";
        final String messagesKey = "messages.conf";

        this.configManager.addConfig(configKey);
        this.configManager.addConfig(messagesKey);
        this.configManager.load();

        // Modify the configs
        this.configManager.set(configKey, "key", String.class, "value");
        this.configManager.set(messagesKey, "key", String.class, "value");

        // Save the configs
        this.configManager.save();

        // Verify the files were modified
        final String configContent = Files.readString(this.tempDir.resolve(configKey));
        final String messagesContent = Files.readString(this.tempDir.resolve(messagesKey));

        assertTrue(configContent.contains("value"));
        assertTrue(messagesContent.contains("value"));

        // Get the values
        final String configValue = this.configManager.get(configKey, "key", String.class, null);
        final String messagesValue = this.configManager.get(messagesKey, "key", String.class, null);
        assertEquals("value", configValue);
        assertEquals("value", messagesValue);
    }

}
