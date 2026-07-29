/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.mock;

import com.spektrsoyuz.configadapter.AbstractConfigController;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Mock config controller for testing.
 *
 * @since 1.0.1
 */
public final class ConfigController extends AbstractConfigController {

    // Constructor
    public ConfigController(
            final JavaPlugin plugin,
            final String messageConfigKey
    ) {
        super(plugin, messageConfigKey);
    }

    @Override
    public void init() {
        this.configManager.addConfig("config.conf");
        this.configManager.addConfig("messages.conf");
        this.configManager.load();
    }

}
