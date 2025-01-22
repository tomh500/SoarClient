package com.soarclient.libraries.soarium.console;

import org.jetbrains.annotations.NotNull;

import com.soarclient.libraries.soarium.console.message.MessageLevel;

public interface ConsoleSink {
    void logMessage(@NotNull MessageLevel level, @NotNull String text, boolean translatable, double duration);
}
