/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Supported configuration file formats.
 *
 * @since 1.0.3
 */
public enum ConfigFormat {

    HOCON {
        @Override
        ConfigurationLoader<CommentedConfigurationNode> createLoader(final Path path) {
            return HoconConfigurationLoader.builder()
                    .path(path)
                    .prettyPrinting(true)
                    .defaultOptions(messageOptions())
                    .build();
        }
    },
    YAML {
        @Override
        ConfigurationLoader<CommentedConfigurationNode> createLoader(final Path path) {
            return YamlConfigurationLoader.builder()
                    .path(path)
                    .nodeStyle(NodeStyle.BLOCK)
                    .indent(2)
                    .defaultOptions(messageOptions())
                    .build();
        }
    };

    // Returns a ConfigurationLoader for the given path
    abstract ConfigurationLoader<CommentedConfigurationNode> createLoader(final Path path);

    // Registers the Message scalar serializer
    private static UnaryOperator<ConfigurationOptions> messageOptions() {
        return options -> options.serializers(builder -> builder
                .register(Message.class, MessageSerializer.INSTANCE)
                .register(MessageList.class, MessageListSerializer.INSTANCE));
    }

}
