package com.life.sailingplugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.life.sailingplugin.network.MessageListener;
import com.life.sailingplugin.network.PacketType;

public final class Main extends JavaPlugin {
	
    private static Main instance;
    
    private static Map<UUID, SailingState> sailingStateMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "sailing");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "sailing", new MessageListener());
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TimeTask(), 20L, 20L);
    }

    @Override
    public void onDisable() {
    	
    }

    public static Main getInstance() {
        return instance;
    }

    public Map<UUID, SailingState> getSailingStateMap() {
    	return sailingStateMap;
    }
    
    // PacketHandle
  	public void handleMessage(Player player, String data) {
  		String code = data.substring(0, 2);
  		if(code.equals(PacketType.CLIENT_FAST_SAILING)) {
  			SailingState state = sailingStateMap.get(UUID.fromString(data.substring(2)));
  			if(state != null) {
  				state.castFastSailing();
  			}
  		}
  	}
}
