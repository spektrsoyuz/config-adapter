/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.mock;

import com.spektrsoyuz.configadapter.type.Message;
import com.spektrsoyuz.configadapter.type.MessageList;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * Mock mapped 'messages' config for testing.
 *
 * @since 1.0.5
 */
@ConfigSerializable
public final class MessagesConfig {

    public Message prefix = new Message("prefix");

    public Message welcome = new Message("<prefix> <gray>Welcome</gray>");

    @Setting("welcomeList")
    public MessageList welcomeList = new MessageList();

}
