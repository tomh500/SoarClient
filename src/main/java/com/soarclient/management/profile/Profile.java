package com.soarclient.management.profile;

import com.google.gson.JsonElement;

public abstract class Profile {
	
    private final String name;

    public Profile(final String name) {
        this.name = name;
    }

    public abstract void load(final JsonElement jsonElement) throws Throwable;

    public abstract JsonElement save() throws Throwable;

    public String getName() {
        return this.name;
    }
}
