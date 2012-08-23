package com.trc202.ServerSideXray;

import java.io.Serializable;

import org.bukkit.Location;

public class SerializableLocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6760528113348601707L;
	
	
	String worldName = "";
	int x,y,z;
	
	
	public SerializableLocation(Location loc)
	{
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
		worldName = loc.getWorld().getName();
	}
	
	public Location getLocation(ServerSideXray plugin)
	{
		return new Location(plugin.getServer().getWorld(worldName), x, y, z);
	}

}
