package com.life.sailingplugin.network;

public class ServerPacketSailingState extends Packet {
	
    public ServerPacketSailingState(int fuel, boolean fastSailing, int cooltime) {
        super(PacketType.SERVER_SAILING_STATE);
        builder.append(fuel).append(":").append(fastSailing ? "1" : "0").append(":").append(cooltime);
    }
}
