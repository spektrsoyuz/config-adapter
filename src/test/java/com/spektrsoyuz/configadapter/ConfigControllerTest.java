/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.mock.ConfigController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
                welcome-list = [
                    "<prefix> <gray>Welcome to the server, <player>!</gray>"
                    "<prefix> <gray>Have a nice day!</gray>"
                ]
                """);

        // Initialize the mock controller
        this.controller = new ConfigController(plugin, messagesKey);
        this.controller.init();
    }

    @Test
    void testGetMessage() {
        // Get the component
        final Component result = this.controller.getMessage(
                "welcome",
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertNotNull(result);

        // Serialize the component to a string
        final String message = MiniMessage.miniMessage().serialize(result);
        assertTrue(message.contains("prefix"));
        assertTrue(message.contains("Welcome to the server,"));
        assertTrue(message.contains("xBumbleBee"));
    }

    @Test
    void testGetMessageList() {
        // Get the list of components
        final List<Component> results = this.controller.getMessageList(
                "welcome-list",
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertNotNull(results);
        assertEquals(2, results.size());

        // Serialize the first component to a string
        final String firstMessage = MiniMessage.miniMessage().serialize(results.getFirst());
        assertTrue(firstMessage.contains("prefix"));
        assertTrue(firstMessage.contains("Welcome to the server,"));
        assertTrue(firstMessage.contains("xBumbleBee"));

        // Serialize the second component to a string
        final String secondMessage = MiniMessage.miniMessage().serialize(results.get(1));
        assertTrue(secondMessage.contains("prefix"));
        assertTrue(secondMessage.contains("Have a nice day!"));
    }

    @Test
    void testGetString() {
        // Get the string
        final String result = this.controller.getString("prefix");
        assertEquals("prefix", result);
    }

}
