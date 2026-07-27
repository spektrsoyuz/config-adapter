package com.spektrsoyuz.configadapter;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Model class for a configuration file.
 *
 * @since 1.0.0
 */
public final class ConfigWrapper {

    private final JavaPlugin plugin;
    private final String fileName;
    private final Path path;
    private final HoconConfigurationLoader loader;

    @Getter
    private CommentedConfigurationNode node;

    // Constructor
    public ConfigWrapper(final JavaPlugin plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.path = plugin.getDataPath().resolve(fileName);

        // Build the config loader
        this.loader = HoconConfigurationLoader.builder()
                .path(this.path)
                .prettyPrinting(true)
                .build();
    }

    // Loads the config file into the node
    public void load() {
        // Check if file is missing at path
        if (Files.notExists(this.path)) {
            // Create file if missing
            this.plugin.getComponentLogger().info("Config file '{}' not found, creating it", this.fileName);
            this.plugin.saveResource(this.fileName, false);
        }

        // Load the config node using the loader
        try {
            this.node = this.loader.load();
        } catch (final ConfigurateException e) {
            this.plugin.getComponentLogger().error("Failed to load config '{}'", this.fileName, e);

            if (this.node == null) {
                // Create node if missing
                this.node = this.loader.createNode();
            }
        }
    }

    // Saves the config node to the file
    public void save() {
        if (this.node == null) return;

        // Save node using the loader
        try {
            this.loader.save(this.node);
        } catch (final ConfigurateException e) {
            this.plugin.getComponentLogger().error("Failed to save config '{}'", this.fileName, e);
        }
    }

}