/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract class for a configuration controller.
 *
 * @since 1.0.1
 */
public abstract class AbstractConfigController {

    protected final JavaPlugin plugin;
    protected final ConfigManager configManager;
    protected final MessageManager messageManager;

    // Constructor
    public AbstractConfigController(
            final JavaPlugin plugin,
            final String messageConfigKey
    ) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
        this.messageManager = new MessageManager(this.configManager, MiniMessage.miniMessage(), messageConfigKey);
    }

    // Initializes the controller
    public abstract void init();

    // Returns a component from the message config
    public @NotNull Component getMessage(
            final String key,
            final TagResolver... resolvers
    ) {
        return this.messageManager.getMessage(key, resolvers);
    }

    // Returns a list of components from the message config
    public @NotNull List<Component> getMessageList(
            final String key,
            final TagResolver... resolvers
    ) {
        return this.messageManager.getMessageList(key, resolvers);
    }

    // Returns a string from the message config
    public @NotNull String getString(
            final String key
    ) {
        return this.messageManager.getString(key);
    }

}
