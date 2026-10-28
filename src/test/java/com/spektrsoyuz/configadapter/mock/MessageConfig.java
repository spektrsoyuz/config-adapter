/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.mock;

import com.spektrsoyuz.configadapter.type.Message;
import com.spektrsoyuz.configadapter.type.MessageList;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * Mock mapped 'messages' config for testing.
 *
 * @since 1.0.5
 */
@Getter
@Accessors(fluent = true)
@ConfigSerializable
public final class MessageConfig {

    private final Message prefix = new Message("prefix");

    private final Message welcome = new Message("<prefix> <gray>Welcome</gray>");

    @Setting("welcomeList")
    private final MessageList welcomeList = new MessageList();

}
