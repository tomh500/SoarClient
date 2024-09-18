package com.soarclient.libraries.sodium.client.gui.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class OptionPage {
	private final IChatComponent name;
	private final ImmutableList<OptionGroup> groups;
	private final ImmutableList<Option<?>> options;

	public OptionPage(String name, ImmutableList<OptionGroup> groups) {
		this(new ChatComponentText(name), groups);
	}

	public OptionPage(IChatComponent name, ImmutableList<OptionGroup> groups) {
		this.name = name;
		this.groups = groups;
		Builder<Option<?>> builder = ImmutableList.builder();
		UnmodifiableIterator var4 = groups.iterator();

		while (var4.hasNext()) {
			OptionGroup group = (OptionGroup)var4.next();
			builder.addAll(group.getOptions());
		}

		this.options = builder.build();
	}

	public IChatComponent getNewName() {
		return this.name;
	}

	public IChatComponent getName() {
		return this.getNewName();
	}

	public ImmutableList<OptionGroup> getGroups() {
		return this.groups;
	}

	public ImmutableList<Option<?>> getOptions() {
		return this.options;
	}
}
