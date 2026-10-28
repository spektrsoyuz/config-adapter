/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.mock;

import com.spektrsoyuz.configadapter.Message;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;

/**
 * Mock mapped 'messages' config for testing.
 *
 * @since 1.0.5
 */
@ConfigSerializable
public final class MessagesConfig {

    public String prefix = "prefix";

    public Message welcome = new Message("<prefix> <gray>Welcome</gray>");

    @Setting("welcomeList")
    public ArrayList<Message> welcomeList = new ArrayList<>();

}
