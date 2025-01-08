package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface ServerEventListener {

	void onJoinServer();
	void onLeaveServer();
	
	class JoinServerEvent extends AbstractEvent<ServerEventListener> {

		public static final int ID = 17;
		
		@Override
		public void call(ServerEventListener listener) {
			listener.onJoinServer();
		}
	}
	
	class LeaveServerEvent extends AbstractEvent<ServerEventListener> {

		public static final int ID = 18;
		
		@Override
		public void call(ServerEventListener listener) {
			listener.onLeaveServer();
		}
	}
}
