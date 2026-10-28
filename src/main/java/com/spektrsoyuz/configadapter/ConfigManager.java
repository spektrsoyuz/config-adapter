/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.wrapper.ConfigFile;
import com.spektrsoyuz.configadapter.wrapper.ConfigWrapper;
import com.spektrsoyuz.configadapter.wrapper.ObjectConfigWrapper;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages configuration files.
 *
 * @since 1.0.0
 */
public final class ConfigManager {

    private final JavaPlugin plugin;
    private final ComponentLogger logger;
    private final ConfigFormat format;
    private final Map<String, ConfigFile> configs;

    // Constructor
    public ConfigManager(final JavaPlugin plugin) {
        this(plugin, ConfigFormat.HOCON);
    }

    // Constructor
    public ConfigManager(final JavaPlugin plugin, final ConfigFormat format) {
        this.plugin = plugin;
        this.logger = plugin.getComponentLogger();
        this.format = format;
        this.configs = new ConcurrentHashMap<>();
    }

    // Adds a resource-backed config to the manager
    public void addConfig(final String fileName) {
        this.configs.put(fileName, new ConfigWrapper(this.plugin, fileName, this.format));
    }

    // Adds an object-mapped config to the manager
    public <T> void addConfig(final String fileName, final Class<T> configClass) {
        this.configs.put(fileName, new ObjectConfigWrapper<>(this.plugin, fileName, configClass, this.format));
    }

    // Loads all configs
    public void load() {
        for (final ConfigFile config : this.configs.values()) {
            config.load();
        }
    }

    // Saves all configs
    public void save() {
        for (final ConfigFile config : this.configs.values()) {
            config.save();
        }
    }

    // Checks if all configs are loaded
    public boolean isLoaded() {
        for (final ConfigFile config : this.configs.values()) {
            if (config.getNode() == null) return false;
        }
        return true;
    }

    // Returns a value from a config
    public <T> T get(
            final String configKey,
            final String nodeName,
            final Class<T> clazz,
            final T defaultInstance
    ) {
        final ConfigFile wrapper = this.configs.get(configKey);
        if (wrapper == null) return defaultInstance;

        final Object[] path = nodeName.split("\\.");

        try {
            final T config = wrapper.getNode().node(path).get(clazz);

            return config != null
                    ? config
                    : defaultInstance;
        } catch (final SerializationException e) {
            this.logger.error("Failed to load '{}' from '{}', using default value", path, configKey, e);
            return defaultInstance;
        }
    }

    // Returns a list of values from a config
    public <T> List<T> getList(
            final String configKey,
            final String nodeName,
            final Class<T> clazz
    ) {
        final ConfigFile wrapper = this.configs.get(configKey);
        if (wrapper == null) return Collections.emptyList();

        final Object[] path = nodeName.split("\\.");

        try {
            final List<T> list = wrapper.getNode().node(path).getList(clazz);

            return list != null
                    ? list
                    : Collections.emptyList();
        } catch (final SerializationException e) {
            this.logger.error("Failed to load '{}' from '{}', using default value", path, configKey, e);
            return Collections.emptyList();
        }
    }

    // Sets a value in a config
    public <T> void set(
            final String configKey,
            final String nodeName,
            final Class<T> clazz,
            final T instance
    ) {
        final ConfigFile wrapper = this.configs.get(configKey);
        if (wrapper == null) return;

        final Object[] path = nodeName.split("\\.");

        try {
            wrapper.getNode().node(path).set(clazz, instance);
        } catch (final SerializationException e) {
            this.logger.error("Failed to save '{}' to '{}'", path, configKey, e);
        }
    }

    // Sets a list of values in a config
    public <T> void setList(
            final String configKey,
            final String nodeName,
            final Class<T> clazz,
            final List<T> instance
    ) {
        final ConfigFile wrapper = this.configs.get(configKey);
        if (wrapper == null) return;

        final Object[] path = nodeName.split("\\.");

        try {
            wrapper.getNode().node(path).setList(clazz, instance);
        } catch (final SerializationException e) {
            this.logger.error("Failed to save '{}' to '{}'", path, configKey, e);
        }
    }

    // Returns the mapped config object, or null if missing or not object-mapped
    @SuppressWarnings("unchecked")
    public <T> T getMapped(final String configKey,
                           final Class<T> configClass) {
        final ConfigFile wrapper = this.configs.get(configKey);
        if (!(wrapper instanceof ObjectConfigWrapper<?> objectWrapper)) {
            return null;
        }

        final Object mapped = objectWrapper.getConfig();
        if (!(configClass.isInstance(mapped))) {
            return null;
        }

        return (T) mapped;
    }

}
