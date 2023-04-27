package com.life.sailingplugin.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.life.sailingplugin.Main;

import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class Packet {

	protected StringBuilder builder;
	
	public Packet(String type) {
		builder = new StringBuilder(type);
	}
	
	public void sendTo(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
		out.writeByte(0);
		out.writeInt(bytes.length);
		out.write(bytes);
		player.sendPluginMessage(Main.getInstance(), "sailing", out.toByteArray());
	}
}
