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
    private final MiniMessage miniMessage;

    private String prefix;

    // Constructor
    public MessageManager(
            final ConfigManager configManager,
            final MiniMessage miniMessage,
            final String configKey
    ) {
        this.configManager = configManager;
        this.miniMessage = miniMessage;
        this.configKey = configKey;
    }

    // Returns a component from the message config
    public @NotNull Component getMessage(
            final String key,
            final TagResolver... resolvers
    ) {
        // Retrieve the message prefix from the config
        if (this.prefix == null) this.cachePrefix();

        // Retrieve the message from the config
        final String rawMessage = this.configManager.getConfig(this.configKey, key, String.class, null);
        if (rawMessage == null) return Component.empty();

        final TagResolver tags = TagResolver.builder()
                .resolvers(resolvers)
                .resolver(Placeholder.parsed("prefix", this.prefix))
                .build();

        // Deserialize the message
        return this.miniMessage.deserialize(rawMessage, tags)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // Returns a list of components from the message config
    public @NotNull List<Component> getMessageList(
            final String key,
            final TagResolver... resolvers
    ) {
        // Retrieve the message prefix from the config
        if (this.prefix == null) this.cachePrefix();

        // Retrieve the messages from the config
        final List<Component> components = new ArrayList<>();
        final List<String> messages = this.configManager.getList(this.configKey, key, String.class);

        if (!(messages.isEmpty())) {
            final TagResolver tags = TagResolver.builder()
                    .resolvers(resolvers)
                    .resolver(Placeholder.parsed("prefix", this.prefix))
                    .build();

            for (final String message : messages) {
                // Deserialize the message
                components.add(this.miniMessage.deserialize(message, tags)
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

    // Caches the message prefix
    private void cachePrefix() {
        this.prefix = this.configManager.getConfig(this.configKey, "prefix", String.class, "");
    }

}
