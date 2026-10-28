/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MiniMessage template that can be stored as a scalar in any config file.
 *
 * @since 1.0.5
 */
public final class Message {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final String template;
    private final String prefix;

    // Constructor
    public Message(final @Nullable String template) {
        this(template, "");
    }

    // Constructor with a prefix captured from the owning config
    public Message(final @Nullable String template,
                   final @Nullable String prefix) {
        this.template = template != null
                ? template
                : "";
        this.prefix = prefix != null
                ? prefix
                : "";
    }

    // Returns the raw MiniMessage template
    public @NotNull String raw() {
        return this.template;
    }

    // Deserializes a template
    public @NotNull Component asComponent(final boolean showPrefix,
                                          final TagResolver... resolvers) {
        if (!(showPrefix)) {
            return MINI_MESSAGE.deserialize(this.template, resolvers)
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        }

        // Add prefix to template
        return MINI_MESSAGE.deserialize(
                this.template,
                Placeholder.parsed("prefix", this.prefix),
                TagResolver.resolver(resolvers)
        ).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

}
