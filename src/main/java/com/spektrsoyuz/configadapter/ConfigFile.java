/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import org.spongepowered.configurate.CommentedConfigurationNode;

/**
 * Shared contract for a configuration file that can be loaded and saved.
 *
 * @since 1.0.1
 */
public interface ConfigFile {

    /**
     * Loads this configuration from disk.
     */
    void load();

    /**
     * Saves this configuration to disk.
     */
    void save();

    /**
     * Returns the backing configuration node, or {@code null} if not loaded.
     *
     * @return the node
     */
    CommentedConfigurationNode getNode();

}
