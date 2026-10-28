/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.mock.ConfigController;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AbstractConfigController}.
 *
 * @since 1.0.1
 */
class ConfigControllerTest {

    @TempDir
    Path tempDir;

    private ConfigController controller;

    @BeforeEach
    void setUp() throws IOException {
        final String primaryKey = "config.conf";
        final String messagesKey = "messages.conf";

        // Mock dependencies
        final JavaPlugin plugin = mock(JavaPlugin.class);
        final ComponentLogger logger = mock(ComponentLogger.class);

        // Redirect plugin data path to temp directory
        when(plugin.getDataPath()).thenReturn(this.tempDir);
        when(plugin.getComponentLogger()).thenReturn(logger);

        // Add config files to the temp directory
        Files.writeString(this.tempDir.resolve(primaryKey), "debug = true");
        Files.writeString(this.tempDir.resolve(messagesKey), """
                prefix = "prefix"
                welcome = "<prefix> <gray>Welcome to the server, <player>!</gray>"
                """);

        // Initialize the mock controller
        this.controller = new ConfigController(plugin);
        this.controller.init();
    }

    @Test
    void testInitLoadsConfigs() {
        // Verify configs are loaded
        assertTrue(this.controller.configManager.isLoaded());
    }

}
