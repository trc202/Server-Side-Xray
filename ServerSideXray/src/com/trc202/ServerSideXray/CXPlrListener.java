package com.trc202.ServerSideXray;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CXPlrListener implements Listener {
	public static ServerSideXray plugin;
	public CXPlrListener(ServerSideXray instance){
		plugin = instance;
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove (PlayerMoveEvent  	event)
	{
		if(event.isCancelled())
		{
			return;
		}
		Location from = event.getFrom();
		Location to = event.getTo();
		if(locationHasChanged(from, to))
		{
			if(plugin.isUsingXRay(event.getPlayer().getName()))
			{
				PlayerInfo plrinfo = plugin.getPlrInfo(event.getPlayer().getName());
				BlocksToBeSent tmpblocks = plugin.getFilteredBlocksinRadius(event.getTo(), event.getPlayer(), plrinfo);
				BlocksToBeSent filtered = plugin.removePreviouslySentBlocks(tmpblocks, plrinfo);
				plrinfo.addToDrawnBlocks(filtered.getGlass());
				plrinfo.addToDrawnBlocks(filtered.getGlowstone());
				plugin.addtoBlockSendingList(filtered);
				
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(plugin.isUsingGlowBlock(event.getPlayer().getName()))
		{
			if(event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.DISPENSER)
			{
				return;
			}
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if(event.getPlayer().getItemInHand().getType() == Material.AIR)
				{
					Block changedblock = event.getClickedBlock();
					BlocksToBeSent temp = new BlocksToBeSent(event.getPlayer().getName());
					ArrayList<Block> b = new ArrayList<Block>();
					b.add(changedblock);
					temp.addToGlowstone(b);
					temp.setWait(true);
					plugin.getGlowstonePlrInfo(event.getPlayer().getName()).addToDrawnBlocks(b);
					plugin.addtoBlockSendingList(temp);
				}
			}
		}
	}


	private boolean locationHasChanged(Location from, Location to) {
		if(from.getBlockX() != to.getBlockX())
		{
			return true;
		}
		else if(from.getBlockY() != to.getBlockY())
		{
			return true;
		}
		else if(from.getBlockZ() != to.getBlockZ())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String plrname = event.getPlayer().getName();
		if(plugin.isUsingXRay(plrname))
		{
			plugin.clearDrawnBlocks(plrname);
		}
		if(plugin.isUsingGlowBlock(plrname))
		{
			plugin.clearDrawnGlowBlocks(plrname);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event)
	{
		String plrname = event.getPlayer().getName();
		if(plugin.isUsingXRay(plrname))
		{
			plugin.clearDrawnBlocks(plrname);
		}
		if(plugin.isUsingGlowBlock(plrname))
		{
			plugin.clearDrawnGlowBlocks(plrname);
		}
	}
}
