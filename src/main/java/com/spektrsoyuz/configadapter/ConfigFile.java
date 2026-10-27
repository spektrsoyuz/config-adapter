/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import org.spongepowered.configurate.CommentedConfigurationNode;

/**
 * Shared interface for a configuration file that can be loaded and saved.
 *
 * @since 1.0.1
 */
public interface ConfigFile {

    // Loads the configuration from disk.
    void load();

    // Saves the configuration to disk.
    void save();

    // Returns the backing configuration node
    CommentedConfigurationNode getNode();

}
