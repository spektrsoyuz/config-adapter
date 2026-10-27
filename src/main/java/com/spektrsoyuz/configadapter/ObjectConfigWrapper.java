/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Model class for a configuration file backed by a {@code @ConfigSerializable} type.
 *
 * @param <T> the mapped configuration type
 * @since 1.0.1
 */
public final class ObjectConfigWrapper<T> implements ConfigFile {

    private final JavaPlugin plugin;
    private final String fileName;
    private final Class<T> configClass;
    private final Path path;
    private final ConfigurationLoader<CommentedConfigurationNode> loader;

    @Getter
    private CommentedConfigurationNode node;

    @Getter
    private T config;

    // Constructor
    public ObjectConfigWrapper(final JavaPlugin plugin,
                               final String fileName,
                               final Class<T> configClass) {
        this(plugin, fileName, configClass, ConfigFormat.HOCON);
    }

    // Constructor
    public ObjectConfigWrapper(final JavaPlugin plugin,
                               final String fileName,
                               final Class<T> configClass,
                               final ConfigFormat format) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configClass = configClass;
        this.path = plugin.getDataPath().resolve(fileName);

        // Build the config loader
        this.loader = format.createLoader(this.path);
    }

    // Loads the config file into the node and mapped object
    public void load() {
        boolean useDefaults = false;

        // Check if file is missing at path
        if (Files.notExists(this.path)) {
            this.plugin.getComponentLogger().info("Config file '{}' not found, creating it", this.fileName);
            useDefaults = true;

            try {
                // Create parent directories if missing
                if (this.path.getParent() != null) {
                    Files.createDirectories(this.path.getParent());
                }

                // Create an empty file for the mapped config
                Files.createFile(this.path);
            } catch (final IOException e) {
                this.plugin.getComponentLogger().error("Failed to create config file '{}'", this.fileName, e);
                return;
            }
        } else {
            try {
                useDefaults = Files.size(this.path) == 0;

                if (useDefaults) {
                    this.plugin.getComponentLogger().info("Config file '{}' is empty, using defaults", this.fileName);
                }
            } catch (final IOException e) {
                this.plugin.getComponentLogger().error("Failed to inspect config file '{}'", this.fileName, e);
            }
        }

        // Load the config node using the loader
        try {
            this.node = this.loader.load();
        } catch (final ConfigurateException e) {
            this.plugin.getComponentLogger().error("Failed to load config file '{}'", this.fileName, e);

            if (this.node == null) {
                // Create node if missing
                this.node = this.loader.createNode();
            }
        }

        // Deserialize the mapped config object
        try {
            this.config = this.node.get(this.configClass);
        } catch (final SerializationException e) {
            this.plugin.getComponentLogger().error("Failed to deserialize config file '{}'", this.fileName, e);
        }

        // Write object defaults when the file was missing or blank
        if (useDefaults) {
            this.plugin.getComponentLogger().info("Saving default values to config file '{}'", this.fileName);
            this.save();
        }
    }

    // Saves the mapped config object to the file
    public void save() {
        if (this.node == null || this.config == null) return;

        try {
            this.node.set(this.configClass, this.config);

            this.loader.save(this.node);
        } catch (final ConfigurateException e) {
            this.plugin.getComponentLogger().error("Failed to save config file '{}'", this.fileName, e);
        }
    }

}
