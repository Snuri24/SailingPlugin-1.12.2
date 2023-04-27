package com.life.sailingplugin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.life.sailingplugin.network.ServerPacketSailingState;

public class SailingState {

	public static final int MAX_FUEL = 360;
	public static final int FAST_SAILING_DURATION = 7;
	public static final int FAST_SAILING_COOLTIME = 10;
	
	private Player player;
	private int fuel;
	private boolean fastSailing;
	private int t;
	
	public SailingState() {
		player = null;
		fuel = 0;
		fastSailing = false;
		t = 0;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		if(player != null) {
			ServerPacketSailingState packet = new ServerPacketSailingState(fuel, fastSailing, t);
			packet.sendTo(player);
		}
	}
	
	public void update() {
		if(fuel > 0) {
			if(fastSailing) {
				fuel -= 2;
				if(fuel < 0) {
					fuel = 0;
				}
			} else {
				fuel -= 1;
			}
		}
		
		if(t > 0) {
			t --;
			
			if(fastSailing && t <= (FAST_SAILING_COOLTIME - FAST_SAILING_DURATION)) {
				fastSailing = false;
				if(player != null) {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_CHIME, 0.5F, 1.0F);
				}
			}
		}
		
		if(player != null) {
			ServerPacketSailingState packet = new ServerPacketSailingState(fuel, fastSailing, t);
			packet.sendTo(player);
		}
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public boolean isFastSailing() {
		return fastSailing;
	}
	
	public int getCooltime() {
		return t;
	}
	
	public void addFuel() {
		fuel += 180;
		if(fuel > MAX_FUEL) {
			fuel = MAX_FUEL;
		}
		
		if(player != null) {
			ServerPacketSailingState packet = new ServerPacketSailingState(fuel, fastSailing, t);
			packet.sendTo(player);
			
		    player.sendTitle("", "보트에 연료가 주입되었습니다.", 0, 10, 10);
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1.0F, 1.0F);
		}
	}
	
	public void castFastSailing() {
		fastSailing = true;
		t = FAST_SAILING_COOLTIME;
		
		if(player != null) {
			ServerPacketSailingState packet = new ServerPacketSailingState(fuel, fastSailing, t);
			packet.sendTo(player);
			
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_CHIME, 0.5F, 1.0F);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
				if(player != null) {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_CHIME, 0.5F, 1.0F);
				}
			}, 10L);
		}
	}
}
