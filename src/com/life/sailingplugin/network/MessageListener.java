package com.life.sailingplugin.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.life.sailingplugin.Main;

import java.nio.charset.StandardCharsets;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener {

    public MessageListener() {
        
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        in.readByte();
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        Main.getInstance().handleMessage(player, new String(bytes, StandardCharsets.UTF_8));
    }
}
