/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

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
                    .build();
        }
    };

    abstract ConfigurationLoader<CommentedConfigurationNode> createLoader(Path path);
}
