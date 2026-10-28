/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

/**
 * Serializes a {@link Message} as a MiniMessage scalar string.
 *
 * @since 1.0.5
 */
@NoArgsConstructor
public final class MessageSerializer implements TypeSerializer<Message> {

    public static final MessageSerializer INSTANCE = new MessageSerializer();

    // Deserializes a Message from the given configuration node
    @Override
    public Message deserialize(final @NonNull Type type,
                               final ConfigurationNode node) {
        if (node.virtual() || node.isNull()) {
            return null;
        }

        return new Message(node.getString());
    }

    // Serializes a Message to the given configuration node
    @Override
    public void serialize(final @NonNull Type type,
                          final @Nullable Message obj,
                          final @NonNull ConfigurationNode node)
            throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.set(obj.raw());
    }

}
