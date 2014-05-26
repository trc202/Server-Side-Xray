package com.trc202.ServerSideXray;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


public class CXBlockListener implements Listener {
	public static ServerSideXray plugin;
	public CXBlockListener(ServerSideXray instance){
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!(plugin.isAnyOneUsingXray()))
		{
			return;
		}
		if(emitsLight(event.getBlockPlaced().getType()))
		{
			Chunk eventchunk = event.getPlayer().getWorld().getChunkAt(event.getBlockPlaced());
			ArrayList<Player> playersinarea = plugin.getPlayersinChunk(eventchunk);
			ArrayList<PlayerInfo> xrayers = new ArrayList<PlayerInfo>();
			for(Player possablexrayer : playersinarea )
			{
				if(plugin.isUsingXRay(possablexrayer.getUniqueId()))
				{
					xrayers.add(plugin.getPlrInfo(possablexrayer.getUniqueId()));
				}
			}
			for(PlayerInfo xrayuser : xrayers)
			{
				BlocksToBeSent temp = plugin.getFilteredBlocksinRadius(event.getBlock().getLocation(), plugin.getServer().getPlayer(xrayuser.getPlayerUUID()), xrayuser);
				temp.setWait(true);
				plugin.addtoBlockSendingList(temp);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!(plugin.isAnyOneUsingXray()))
		{
			return;
		}
		if(emitsLight(event.getBlock().getType()))
		{
			Chunk eventchunk = event.getPlayer().getWorld().getChunkAt(event.getBlock());
			ArrayList<Player> playersinarea = plugin.getPlayersinChunk(eventchunk);
			ArrayList<PlayerInfo> xrayers = new ArrayList<PlayerInfo>();
			for(Player possablexrayer : playersinarea )
			{
				if(plugin.isUsingXRay(possablexrayer.getUniqueId()))
				{
					xrayers.add(plugin.getPlrInfo(possablexrayer.getUniqueId()));
				}
			}
			for(PlayerInfo xrayuser : xrayers)
			{
				BlocksToBeSent temp = plugin.getFilteredBlocksinRadius(event.getBlock().getLocation(), plugin.getServer().getPlayer(xrayuser.getPlayerUUID()), xrayuser);
				temp.setWait(true);
				plugin.addtoBlockSendingList(temp);
			}
		}
	}

	private boolean emitsLight(Material type) {
		if(type == Material.TORCH)
		{
			return true;
		}
		else if(type == Material.GLOWSTONE)
		{
			return true;
		}
		else if(type == Material.REDSTONE_TORCH_ON)
		{
			return true;
		}
		else if(type == Material.JACK_O_LANTERN)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
