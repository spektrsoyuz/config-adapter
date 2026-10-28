/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import com.spektrsoyuz.configadapter.wrapper.ConfigWrapper;
import com.spektrsoyuz.configadapter.wrapper.ConfigWrapperImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages configuration files.
 *
 * @since 1.0.0
 */
public final class ConfigManager {

    private final JavaPlugin plugin;
    private final ConfigFormat format;
    private final Map<String, ConfigWrapper> configs;

    // Constructor
    public ConfigManager(final JavaPlugin plugin) {
        this(plugin, ConfigFormat.HOCON);
    }

    // Constructor
    public ConfigManager(final JavaPlugin plugin,
                         final ConfigFormat format) {
        this.plugin = plugin;
        this.format = format;
        this.configs = new ConcurrentHashMap<>();
    }

    // Adds an object-mapped config to the manager
    public <T> void addConfig(final String fileName,
                              final Class<T> configClass) {
        this.configs.put(fileName, new ConfigWrapperImpl<>(this.plugin, fileName, configClass, this.format));
    }

    // Loads all configs
    public void load() {
        for (final ConfigWrapper config : this.configs.values()) {
            config.load();
        }
    }

    // Saves all configs
    public void save() {
        for (final ConfigWrapper config : this.configs.values()) {
            config.save();
        }
    }

    // Checks if all configs are loaded
    public boolean isLoaded() {
        for (final ConfigWrapper config : this.configs.values()) {
            if (config.getNode() == null) return false;
        }
        return true;
    }

    // Returns the mapped config object, or null if missing
    @SuppressWarnings("unchecked")
    public <T> T get(final String configKey,
                     final Class<T> configClass) {
        final ConfigWrapper wrapper = this.configs.get(configKey);

        if (!(wrapper instanceof ConfigWrapperImpl<?> objectWrapper)) {
            return null;
        }

        final Object mapped = objectWrapper.getConfig();
        if (!(configClass.isInstance(mapped))) {
            return null;
        }

        return (T) mapped;
    }

}
