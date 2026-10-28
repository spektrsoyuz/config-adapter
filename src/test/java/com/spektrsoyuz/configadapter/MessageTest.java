/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.mock.MessageConfig;
import com.spektrsoyuz.configadapter.type.Message;
import com.spektrsoyuz.configadapter.type.MessageList;
import com.spektrsoyuz.configadapter.wrapper.ObjectConfigWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Message}.
 *
 * @since 1.0.5
 */
class MessageTest {

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
    void testAsComponent() {
        final Message message = new Message(
                "<prefix> <gray>Welcome to the server, <player>!</gray>",
                "prefix"
        );

        // Deserialize with prefix and extra placeholders
        final Component result = message.asComponent(
                true,
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertNotNull(result);
        assertEquals(TextDecoration.State.FALSE, result.decoration(TextDecoration.ITALIC));

        final String serialized = MiniMessage.miniMessage().serialize(result);
        assertTrue(serialized.contains("prefix"));
        assertTrue(serialized.contains("Welcome to the server,"));
        assertTrue(serialized.contains("xBumbleBee"));
    }

    @Test
    void testAsComponents() {
        final MessageList messages = new MessageList(List.of(
                new Message("<prefix> <gray>Welcome to the server, <player>!</gray>", "prefix"),
                new Message("<prefix> <gray>Have a nice day!</gray>", "prefix")
        ));

        // Deserialize a list of templates
        final List<Component> results = messages.asComponents(
                true,
                Placeholder.parsed("player", "xBumbleBee")
        );

        assertEquals(2, results.size());

        final String firstMessage = MiniMessage.miniMessage().serialize(results.getFirst());
        assertTrue(firstMessage.contains("prefix"));
        assertTrue(firstMessage.contains("Welcome to the server,"));
        assertTrue(firstMessage.contains("xBumbleBee"));

        final String secondMessage = MiniMessage.miniMessage().serialize(results.get(1));
        assertTrue(secondMessage.contains("prefix"));
        assertTrue(secondMessage.contains("Have a nice day!"));
    }

    @Test
    void testAsComponentsEmpty() {
        assertTrue(new MessageList().asComponents(false).isEmpty());
        assertTrue(new MessageList(null).asComponents(false).isEmpty());
    }

    @Test
    void testLoadFromMappedConfig() throws IOException {
        // Write a mapped config with Message scalars
        Files.writeString(this.tempDir.resolve("messages.conf"), """
                prefix = "prefix"
                welcome = "<prefix> <gray>Welcome to the server, <player>!</gray>"
                welcomeList = [
                    "<prefix> <gray>Welcome to the server, <player>!</gray>"
                    "<prefix> <gray>Have a nice day!</gray>"
                ]
                """);

        final ObjectConfigWrapper<MessageConfig> wrapper =
                new ObjectConfigWrapper<>(this.plugin, "messages.conf", MessageConfig.class);
        wrapper.load();

        assertNotNull(wrapper.getConfig());
        assertEquals("prefix", wrapper.getConfig().prefix().raw());
        assertEquals("<prefix> <gray>Welcome to the server, <player>!</gray>", wrapper.getConfig().welcome().raw());

        // Deserialize mapped Message fields
        final Component welcome = wrapper.getConfig().welcome().asComponent(
                true,
                Placeholder.parsed("player", "xBumbleBee")
        );
        final String serialized = MiniMessage.miniMessage().serialize(welcome);
        assertTrue(serialized.contains("prefix"));
        assertTrue(serialized.contains("xBumbleBee"));

        final List<Component> list = wrapper.getConfig().welcomeList().asComponents(true);
        assertEquals(2, list.size());
    }

    @Test
    void testDefaultMappedPrefixResolved() {
        final ObjectConfigWrapper<MessageConfig> wrapper =
                new ObjectConfigWrapper<>(this.plugin, "messages.conf", MessageConfig.class);
        wrapper.load();

        assertNotNull(wrapper.getConfig());

        final Component welcome = wrapper.getConfig().welcome().asComponent(true);
        final String serialized = MiniMessage.miniMessage().serialize(welcome);

        assertTrue(serialized.contains("prefix"));
        assertTrue(serialized.contains("Welcome"));
    }

}
