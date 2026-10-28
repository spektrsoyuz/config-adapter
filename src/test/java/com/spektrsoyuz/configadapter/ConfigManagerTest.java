/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        this.configManager.addConfig("config.conf", ExampleConfig.class);

        assertFalse(this.configManager.isLoaded());

        // Load config manager
        this.configManager.load();

        assertTrue(this.configManager.isLoaded());
        assertTrue(Files.exists(this.tempDir.resolve("config.conf")));
    }

    @Test
    void testGetMissing() {
        assertNull(this.configManager.get("config.conf", ExampleConfig.class));
    }

    @Test
    void testLoadAndSave() throws IOException {
        // Add configs
        final String configKey = "config.conf";
        final String messagesKey = "messages.conf";

        this.configManager.addConfig(configKey, ExampleConfig.class);
        this.configManager.addConfig(messagesKey, ExampleConfig.class);
        this.configManager.load();

        final ExampleConfig config = this.configManager.get(configKey, ExampleConfig.class);
        final ExampleConfig messages = this.configManager.get(messagesKey, ExampleConfig.class);
        assertNotNull(config);
        assertNotNull(messages);

        config.name = "value";
        messages.name = "value";

        // Save the configs
        this.configManager.save();

        // Verify the files were modified
        final String configContent = Files.readString(this.tempDir.resolve(configKey));
        final String messagesContent = Files.readString(this.tempDir.resolve(messagesKey));

        assertTrue(configContent.contains("value"));
        assertTrue(messagesContent.contains("value"));
    }

    @Test
    void testMappedConfigLoadAndSave() throws IOException {
        // Add config
        final String configKey = "mapped.conf";
        this.configManager.addConfig(configKey, ExampleConfig.class);
        this.configManager.load();

        assertTrue(this.configManager.isLoaded());
        assertTrue(Files.exists(this.tempDir.resolve(configKey)));

        final ExampleConfig mapped = this.configManager.get(configKey, ExampleConfig.class);
        assertNotNull(mapped);
        assertEquals("default", mapped.name);

        // Save the config
        mapped.name = "updated";
        this.configManager.save();

        final String content = Files.readString(this.tempDir.resolve(configKey));
        assertTrue(content.contains("updated"));
    }

    @ConfigSerializable
    static class ExampleConfig {
        String name = "default";
    }

}
