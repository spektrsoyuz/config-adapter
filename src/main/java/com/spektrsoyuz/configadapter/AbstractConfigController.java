/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract class for a configuration controller.
 *
 * @since 1.0.1
 */
public abstract class AbstractConfigController {

    protected final JavaPlugin plugin;
    protected final ConfigManager configManager;

    // Constructor
    public AbstractConfigController(final JavaPlugin plugin) {
        this(plugin, ConfigFormat.HOCON);
    }

    // Constructor
    public AbstractConfigController(final JavaPlugin plugin,
                                    final ConfigFormat format) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin, format);
    }

    // Initializes the controller
    public abstract void init();

}
