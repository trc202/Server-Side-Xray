package com.trc202.ServerSideXray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PlayerInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7417287492680850041L;
	/**
	 * 
	 */
	private transient ArrayList<Block> Drawnblocks;
	private ArrayList<SerializableLocation> BlockLocationList;
	private UUID plrUUID;
	private int xarea = 5;
	private int yarea = 5;
	private int zarea = 5;
	private boolean glowStoneEnabled = true;
	private ArrayList<Material> hiddenBlocks;
	private ArrayList<Material> highlightedBlocks;
	public PlayerInfo(UUID playerUUID)
	{
		Drawnblocks = new ArrayList<Block>();
		BlockLocationList = new ArrayList<SerializableLocation>();
		hiddenBlocks = new ArrayList<Material>();
		highlightedBlocks = new ArrayList<Material>();
		plrUUID = playerUUID;
		hiddenBlocks.add(Material.STONE);
		hiddenBlocks.add(Material.DIRT);
		hiddenBlocks.add(Material.GRASS);
		hiddenBlocks.add(Material.GRAVEL);
		hiddenBlocks.add(Material.SAND);
		hiddenBlocks.add(Material.SANDSTONE);
		highlightedBlocks.add(Material.DIAMOND_ORE);
		highlightedBlocks.add(Material.GOLD_ORE);
		highlightedBlocks.add(Material.CHEST);
		highlightedBlocks.add(Material.IRON_ORE);
		highlightedBlocks.add(Material.LAPIS_ORE);
		highlightedBlocks.add(Material.EMERALD_ORE);
	}
	public PlayerInfo(PlayerInfo info)
	{
		Drawnblocks = new ArrayList<Block>();
		BlockLocationList = new ArrayList<SerializableLocation>();
		hiddenBlocks = new ArrayList<Material>();
		highlightedBlocks = new ArrayList<Material>();
		plrUUID = info.getPlayerUUID();
		hiddenBlocks = info.getHiddenBlocksCopy();
		highlightedBlocks = info.getHighlightedBlocksCopy();
		
		
	}
	public UUID getPlayerUUID()
	{
		return plrUUID;
	}
	public void addToDrawnBlocks(ArrayList<Block> blockstoadd)
	{
		Drawnblocks.addAll(blockstoadd);
	}
	public ArrayList<Block> getDrawnBlocks()
	{
		return Drawnblocks;
	}

	public void clearDrawnBlocks()
	{
		if(Drawnblocks != null)
		Drawnblocks.clear();
	}

	public void setXarea(int xarea) {
		this.xarea = xarea;
	}
	public int getXarea() {
		return xarea;
	}
	public void setYarea(int yarea) {
		this.yarea = yarea;
	}
	public int getYarea() {
		return yarea;
	}
	public void setZarea(int zarea) {
		this.zarea = zarea;
	}
	public int getZarea() {
		return zarea;
	}
	public void setGlowstoneenabled(boolean glowstoneenabled) {
		this.glowStoneEnabled = glowstoneenabled;
	}

	public boolean isGlowstoneenabled() {
		return glowStoneEnabled;
	}
	
	public void addToHiddenblocks(Material hiddenblock)
	{
		if(!this.hiddenBlocks.contains(hiddenblock))
		{
			hiddenBlocks.add(hiddenblock);
		}
		return;
	}
	public void removeFromHiddenblocks(Material hiddenblock)
	{
		if(this.hiddenBlocks.contains(hiddenblock))
		{
			this.hiddenBlocks.remove(hiddenblock);
		}
		return;
	}
	public boolean shouldBeHidden(Material m)
	{
		return hiddenBlocks.contains(m);
	}
	public String listHiddenBlocks()
	{
		String materialList = "";
		for(Material m : hiddenBlocks)
		{
			materialList = materialList + m.toString() + " ";
		}
		return materialList;
	}
	public ArrayList<Material> getHiddenBlocksCopy()
	{
		ArrayList<Material> out = new ArrayList<Material>();
		for(Material m: hiddenBlocks)
		{
			out.add(m);
		}
		return out;
	}
	
	public void addToHighlightedBlocks(Material hiddenblock)
	{
		if(!this.highlightedBlocks.contains(hiddenblock))
		{
			highlightedBlocks.add(hiddenblock);
		}
		return;
	}
	public void removefromhighlitedblocks(Material highlitedblock)
	{
		if(this.highlightedBlocks.contains(highlitedblock))
		{
			this.highlightedBlocks.remove(highlitedblock);
		}
	}
	public boolean shouldBeHighlighted(Material m)
	{
		return highlightedBlocks.contains(m);
	}
	public String listHighlightedBlocks()
	{
		String materialList = "";
		for(Material m : highlightedBlocks)
		{
			materialList = materialList + m.toString() + " ";
		}
		return materialList;
	}
	public ArrayList<Material> getHighlightedBlocksCopy()
	{
		ArrayList<Material> out = new ArrayList<Material>();
		for(Material m : highlightedBlocks)
		{
			out.add(m);
		}
		return out;
	}
	
	public ArrayList<Block> getDrawnBlocksofType(Material m)
	{
		ArrayList<Block> out = new ArrayList<Block>();
		for(Block b: Drawnblocks)
		{
			if(b.getType() == m)
			{
				out.add(b);
			}
		}
		return out;
	}
	public void removeDrawnBlocksofType(Material m)
	{
		Iterator<Block> i =Drawnblocks.iterator();
		while(i.hasNext())
		{
			Block b = i.next();
			if(b.getType() == m)
			{
				i.remove();
			}
		}
	}

	public void importHiddenHighlightedBlocks(PlayerInfo plrinfo)
	{
		this.highlightedBlocks = plrinfo.getHighlightedBlocksCopy();
		this.hiddenBlocks = plrinfo.getHiddenBlocksCopy();
	}
	
	public void prepareForSerialization()
	{
		BlockLocationList = new ArrayList<SerializableLocation>();
		for(Block b : Drawnblocks)
		{
			BlockLocationList.add(new SerializableLocation(b.getLocation()));
		}
		return;
	}	
	public void deSerilize(ServerSideXray plugin)
	{
		ArrayList<Block> temp = new ArrayList<Block>();
		for(SerializableLocation l :BlockLocationList)
		{
			Location blocklocation = l.getLocation(plugin);
			temp.add(blocklocation.getWorld().getBlockAt(blocklocation));
		}
		Drawnblocks = new ArrayList<Block>();
		addToDrawnBlocks(temp);
		BlockLocationList.clear();
		return;
	}
}
