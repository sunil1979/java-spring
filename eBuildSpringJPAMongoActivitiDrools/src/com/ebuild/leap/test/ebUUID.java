package com.ebuild.leap.test;

import java.util.Date;

public class ebUUID {

	private static ebUUID ref;
	private static long incrvar;
	private static int serverId;
	private static long uuid;
	private static long startupTime;

	private ebUUID() {

	}

	public static void main(String[] args) {

		ebUUID eb = ebUUID.getEbUUID();

		for (int i = 0; i < 10000; i++)
			System.out.println("uuid: " + eb.getUUID());
	}

	public static synchronized ebUUID getEbUUID() {
		if (ref == null) {
			ref = new ebUUID();
			ref.init();
		}
		return ref;
	}

	private void init() {
		try {
			incrvar = 0;
			serverId = 1;
			uuid = 0;
			startupTime = ((new Date()).getTime()) / 1000;
		} catch (Exception e) {
			System.err.println("Cannot init ebUUID");
		}
	}

	public long getUUID() {
		try {
			uuid = ((serverId & 255) << 56);
			uuid = uuid + (startupTime << 24);
			uuid = uuid + incrvar++;
			// System.out.println("uuid: " + uuid);
		} catch (Exception e) {
			System.err.println("Cannot init ebUUID");
		}

		return uuid;
	}

}
