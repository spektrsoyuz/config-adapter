/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MessageManager}.
 *
 * @since 1.0.1
 */
class MessageManagerTest {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private final String configKey = "messages.yml";

    @BeforeEach
    void setUp() {
        // Mock dependencies
        this.configManager = mock(ConfigManager.class);

        // Initialize the message manager
        this.messageManager = new MessageManager(
                this.configManager,
                MiniMessage.miniMessage(),
                this.configKey
        );

        // Set prefix mock behavior
        when(this.configManager.get(this.configKey, "prefix", String.class, ""))
                .thenReturn("prefix");
    }

    @Test
    void testGetMessage() {
        final String key = "key";
        final String rawMessage = "<prefix> <gray>Welcome to the server, <player>!</gray>";

        when(this.configManager.get(this.configKey, key, String.class, null))
                .thenReturn(rawMessage);

        // Get the component
        final Component result = this.messageManager.getMessage(
                key,
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertNotNull(result);
        assertNotEquals(Component.text(key), result);
        assertEquals(TextDecoration.State.FALSE, result.decoration(TextDecoration.ITALIC));

        // Serialize the component to a string
        final String message = MiniMessage.miniMessage().serialize(result);
        assertTrue(message.contains("prefix"));
        assertTrue(message.contains("Welcome to the server,"));
        assertTrue(message.contains("xBumbleBee"));
    }

    @Test
    void testGetMessageEmpty() {
        final String key = "key";

        when(this.configManager.get(this.configKey, key, String.class, null))
                .thenReturn(null);

        // Attempt to get the component
        final Component result = this.messageManager.getMessage(key);
        assertNotNull(result);
        assertEquals(Component.text(key), result);
    }

    @Test
    void testGetMessageList() {
        final String key = "key";
        final List<String> rawMessages = List.of(
                "<prefix> <gray>Welcome to the server, <player>!</gray>",
                "<prefix> <gray>Have a nice day!</gray>"
        );

        when(this.configManager.getList(this.configKey, key, String.class))
                .thenReturn(rawMessages);

        // Get the list of components
        final List<Component> results = this.messageManager.getMessageList(
                key,
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertNotNull(results);
        assertEquals(rawMessages.size(), results.size());

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
    void testGetMessageListEmpty() {
        final String key = "key";

        when(this.configManager.getList(this.configKey, key, String.class))
                .thenReturn(Collections.emptyList());

        // Attempt to get the list of components
        final List<Component> results = this.messageManager.getMessageList(key);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testGetString() {
        final String key = "key";
        final String expectedValue = "value";

        when(this.configManager.get(this.configKey, key, String.class, ""))
                .thenReturn(expectedValue);

        final String result = this.messageManager.getString(key);
        assertEquals(expectedValue, result);
    }

    @Test
    void testGetPrefix() {
        final String result = this.messageManager.getPrefix();
        assertEquals("prefix", result);
    }

}
