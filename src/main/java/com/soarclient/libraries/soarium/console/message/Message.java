package com.soarclient.libraries.soarium.console.message;

public record Message(MessageLevel level, String text, boolean translated, double duration) {

}
