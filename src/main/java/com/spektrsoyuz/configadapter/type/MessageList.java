/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * List of MiniMessage templates that can be stored as a sequence in any config file.
 *
 * @since 1.0.6
 */
public final class MessageList {

    private final List<Message> messages;

    // Constructor
    public MessageList() {
        this(List.of());
    }

    // Constructor with messages
    public MessageList(final @Nullable List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            this.messages = List.of();
            return;
        }

        final List<Message> copy = new ArrayList<>(messages.size());

        for (final Message message : messages) {
            if (message != null) {
                copy.add(message);
            }
        }

        this.messages = List.copyOf(copy);
    }

    // Returns the raw templates
    public @NotNull List<Message> raw() {
        return this.messages;
    }

    // Deserializes each template
    public @NotNull List<Component> asComponents(final boolean showPrefix,
                                                 final TagResolver... resolvers) {
        if (this.messages.isEmpty()) {
            return List.of();
        }

        final List<Component> components = new ArrayList<>(this.messages.size());
        for (final Message message : this.messages) {
            if (message == null) {
                continue;
            }

            components.add(message.asComponent(showPrefix, resolvers));
        }

        return components;
    }

}
