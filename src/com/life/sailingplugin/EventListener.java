package com.life.sailingplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.EntityPlayer;

public class EventListener implements Listener {

	public EventListener() {
		
	}
	
	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		if(!(event.getEntered() instanceof Player))
			return;
		if(!(event.getVehicle() instanceof Boat))
			return;

		Vehicle boat = event.getVehicle();
		if(boat.getPassengers().size() == 0 || !(boat.getPassengers().get(0) instanceof Player)) {
			SailingState state = Main.getInstance().getSailingStateMap().get(boat.getUniqueId());
			if(state == null) {
				state = new SailingState();
				Main.getInstance().getSailingStateMap().put(boat.getUniqueId(), state);
			}
			state.setPlayer((Player) event.getEntered());
		}
	}
	
	@EventHandler
	public void onVehicleExit(VehicleExitEvent event) {
		if(!(event.getExited() instanceof Player))
			return;
		if(!(event.getVehicle() instanceof Boat))
			return;
		
		SailingState state = Main.getInstance().getSailingStateMap().get(event.getVehicle().getUniqueId());
		if(state != null && event.getExited() == state.getPlayer()) {
			state.setPlayer(null);
		}
	}
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event) {
		if(!(event.getVehicle() instanceof Boat))
			return;
		
		Vehicle boat = event.getVehicle();
		SailingState state = Main.getInstance().getSailingStateMap().get(boat.getUniqueId());
		if(state == null || state.getPlayer() == null)
			return;
		
		if(state.getFuel() <= 0) {
			boat.setVelocity(new Vector(0, 0, 0));
		} else if(state.isFastSailing()) {
			Player player = state.getPlayer();
			EntityPlayer p = ((CraftPlayer) player).getHandle();
			
			if(p.motX != 0.0D || p.motZ != 0.0D) {
				Location loc = boat.getLocation();
				Vector vec = loc.getDirection().add(new Vector(p.motX, 0, p.motZ).normalize());
				if(Math.abs(vec.getX()) > 0.1D || Math.abs(vec.getZ()) > 0.1D) {
					boat.setVelocity(loc.getDirection().multiply(0.95D));
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getItem() == null)
			return;
		if(event.getHand() != EquipmentSlot.HAND)
			return;
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getItem().getType() == Material.COAL) {
				Player player = event.getPlayer();
				Entity vehicle = player.getVehicle();
				if(vehicle != null && vehicle instanceof Boat) {
					SailingState state = Main.getInstance().getSailingStateMap().get(vehicle.getUniqueId());
					if(state != null && player == state.getPlayer()) {
						int amount = event.getItem().getAmount();
						if(amount > 1) {
							event.getItem().setAmount(amount - 1);
						} else {
							player.getInventory().setItemInMainHand(null);
						}
						state.addFuel();
					}
				}
				return;
			}
		}
	}
}
