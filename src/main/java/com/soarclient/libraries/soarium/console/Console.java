package com.soarclient.libraries.soarium.console;

import org.jetbrains.annotations.NotNull;

import com.soarclient.libraries.soarium.console.message.Message;
import com.soarclient.libraries.soarium.console.message.MessageLevel;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Console implements ConsoleSink {
    public static final Console INSTANCE = new Console();

    private final ArrayDeque<Message> messages = new ArrayDeque<>();

    @Override
    public void logMessage(@NotNull MessageLevel level, @NotNull String text, boolean translatable, double duration) {
        Objects.requireNonNull(level);
        Objects.requireNonNull(text);

        this.messages.addLast(new Message(level, text, translatable, duration));
    }

    public Deque<Message> getMessageDrain() {
        return this.messages;
    }

    public static ConsoleSink instance() {
        return INSTANCE;
    }
}
