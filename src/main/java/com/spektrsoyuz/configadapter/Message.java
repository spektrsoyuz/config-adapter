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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * MiniMessage template that can be stored as a scalar in any config file.
 *
 * @since 1.0.5
 */
public final class Message {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final String template;

    // Constructor
    public Message(final @Nullable String template) {
        this.template = template != null ? template : "";
    }

    // Returns the raw MiniMessage template
    public @NotNull String raw() {
        return this.template;
    }

    // Deserializes this template with extra resolvers
    public @NotNull Component asComponent(final TagResolver... extra) {
        return MINI_MESSAGE.deserialize(this.template, extra)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // Deserializes this template, always resolving {@code <prefix>}
    public @NotNull Component asComponent(final @Nullable String prefix, final TagResolver... extra) {
        return this.asComponent(
                Placeholder.parsed("prefix", prefix != null ? prefix : ""),
                TagResolver.resolver(extra)
        );
    }

    // Deserializes a list of templates with extra resolvers
    public static @NotNull List<Component> asComponents(
            final @Nullable List<Message> messages,
            final TagResolver... extra
    ) {
        return asComponents(messages, null, extra);
    }

    // Deserializes a list of templates, always resolving {@code <prefix>}
    public static @NotNull List<Component> asComponents(
            final @Nullable List<Message> messages,
            final @Nullable String prefix,
            final TagResolver... extra
    ) {
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }

        final List<Component> components = new ArrayList<>(messages.size());
        for (final Message message : messages) {
            if (message == null) {
                continue;
            }

            components.add(prefix != null
                    ? message.asComponent(prefix, extra)
                    : message.asComponent(extra));
        }

        return components;
    }

}
