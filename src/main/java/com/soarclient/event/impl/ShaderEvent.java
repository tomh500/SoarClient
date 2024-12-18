package com.soarclient.event.impl;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.event.Event;

import net.minecraft.client.shader.ShaderGroup;

public class ShaderEvent extends Event {

	private List<ShaderGroup> groups = new ArrayList<>();

	public List<ShaderGroup> getGroups() {
		return groups;
	}
}