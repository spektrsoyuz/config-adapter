/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.mock.PrimaryConfig;
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
        this.configManager.addConfig("config.conf", PrimaryConfig.class);

        assertFalse(this.configManager.isLoaded());

        // Load config manager
        this.configManager.load();

        assertTrue(this.configManager.isLoaded());
        assertTrue(Files.exists(this.tempDir.resolve("config.conf")));
    }

    @Test
    void testGetMissing() {
        // Get a missing config
        assertNull(this.configManager.get("config.conf", PrimaryConfig.class));
    }

    @Test
    void testLoadAndSave() throws IOException {
        // Add configs
        final String configKey1 = "config.conf";
        final String configKey2 = "messages.conf";

        this.configManager.addConfig(configKey1, PrimaryConfig.class);
        this.configManager.addConfig(configKey2, PrimaryConfig.class);

        // Load config manager
        this.configManager.load();

        // Get the mapped configs
        final PrimaryConfig config1 = this.configManager.get(configKey1, PrimaryConfig.class);
        final PrimaryConfig config2 = this.configManager.get(configKey2, PrimaryConfig.class);
        assertNotNull(config1);
        assertNotNull(config2);

        // Modify the mapped configs
        config1.name("value");
        config2.name("value");

        // Save the configs
        this.configManager.save();

        // Verify the files were modified
        final String configContent = Files.readString(this.tempDir.resolve(configKey1));
        final String messagesContent = Files.readString(this.tempDir.resolve(configKey2));

        assertTrue(configContent.contains("value"));
        assertTrue(messagesContent.contains("value"));
    }

    @Test
    void testMappedConfigLoadAndSave() throws IOException {
        // Add config
        final String configKey = "config.conf";
        this.configManager.addConfig(configKey, PrimaryConfig.class);

        // Load config manager
        this.configManager.load();

        assertTrue(this.configManager.isLoaded());
        assertTrue(Files.exists(this.tempDir.resolve(configKey)));

        // Get the mapped config
        final PrimaryConfig config = this.configManager.get(configKey, PrimaryConfig.class);
        assertNotNull(config);
        assertEquals("default", config.name());

        // Modify the mapped config
        config.name("updated");

        // Save the config
        this.configManager.save();

        final String content = Files.readString(this.tempDir.resolve(configKey));
        assertTrue(content.contains("updated"));
    }

}
