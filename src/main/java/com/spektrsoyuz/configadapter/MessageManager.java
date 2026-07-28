/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages serialization of plugin messages.
 *
 * @since 1.0.0
 */
public final class MessageManager {

    private final ConfigManager configManager;
    private final String configKey;

    // Constructor
    public MessageManager(
            final ConfigManager configManager,
            final String configKey
    ) {
        this.configManager = configManager;
        this.configKey = configKey;
    }

    // Returns a component from the message config
    public @NotNull Component getMessage(
            final String key,
            final MiniMessage miniMessage,
            final TagResolver... resolvers
    ) {
        // Retrieve the message prefix from the config
        final String prefix = this.configManager.getConfig(this.configKey, "prefix", String.class, "");

        // Retrieve the message from the config
        final String rawMessage = this.configManager.getConfig(this.configKey, key, String.class, null);

        final List<TagResolver> tagResolvers = new ArrayList<>(List.of(resolvers));
        tagResolvers.add(Placeholder.parsed("prefix", prefix));

        // Deserialize the message
        return miniMessage.deserialize(rawMessage, tagResolvers.toArray(TagResolver[]::new))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // Returns a list of components from the message config
    public @NotNull List<Component> getMessageList(
            final String key,
            final MiniMessage miniMessage,
            final TagResolver... resolvers
    ) {
        // Retrieve the message prefix from the config
        final String prefix = this.configManager.getConfig(this.configKey, "prefix", String.class, "");

        // Retrieve the messages from the config
        final List<Component> components = new ArrayList<>();
        final List<String> messages = this.configManager.getList(this.configKey, key, String.class);

        if (!(messages.isEmpty())) {
            final List<TagResolver> tagResolvers = new ArrayList<>(List.of(resolvers));
            tagResolvers.add(Placeholder.parsed("prefix", prefix));

            final TagResolver[] resolverArray = tagResolvers.toArray(TagResolver[]::new);

            for (final String message : messages) {
                // Deserialize the message
                components.add(miniMessage.deserialize(message, resolverArray)
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }
        }

        return components;
    }

    // Returns a string from the message config
    public String getString(
            final String key
    ) {
        return this.configManager.getConfig(this.configKey, key, String.class, "");
    }

}
