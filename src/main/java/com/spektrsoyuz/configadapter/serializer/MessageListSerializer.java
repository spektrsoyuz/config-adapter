/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.serializer;

import com.spektrsoyuz.configadapter.type.Message;
import com.spektrsoyuz.configadapter.type.MessageList;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializes a {@link MessageList} as a sequence of MiniMessage scalar strings.
 *
 * @since 1.0.6
 */
@NoArgsConstructor
public final class MessageListSerializer implements TypeSerializer<MessageList> {

    public static final MessageListSerializer INSTANCE = new MessageListSerializer();

    // Deserializes a MessageList from the given configuration node
    @Override
    public MessageList deserialize(final @NonNull Type type,
                                   final ConfigurationNode node) {
        if (node.virtual() || node.isNull()) {
            return null;
        }

        if (!node.isList()) {
            return new MessageList();
        }

        final List<Message> messages = new ArrayList<>();
        for (final ConfigurationNode child : node.childrenList()) {
            messages.add(MessageSerializer.INSTANCE.deserialize(Message.class, child));
        }

        return new MessageList(messages);
    }

    // Serializes a MessageList to the given configuration node
    @Override
    public void serialize(final @NonNull Type type,
                          final @Nullable MessageList obj,
                          final @NonNull ConfigurationNode node)
            throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        final List<String> templates = new ArrayList<>();
        for (final Message message : obj.raw()) {
            if (message == null) {
                continue;
            }

            templates.add(message.raw());
        }

        node.setList(String.class, templates);
    }

}
