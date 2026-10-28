/**
 * Copyright (c) 2026 SpektrSoyuz. All rights reserved.
 */
package com.spektrsoyuz.configadapter.mock;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * Mock mapped 'primary' config for testing.
 *
 * @since 1.0.5
 */
@Getter
@Setter
@Accessors(fluent = true)
@ConfigSerializable
public final class PrimaryConfig {

    private String name = "default";

    private boolean enabled = true;

}
