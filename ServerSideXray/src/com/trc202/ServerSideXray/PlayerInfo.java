package com.trc202.ServerSideXray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
	private String plrname;
	private int xarea = 5;
	private int yarea = 5;
	private int zarea = 5;
	private boolean glowstoneenabled = true;
	private ArrayList<Material> hiddenblocks;
	private ArrayList<Material> highlitedblocks;
	public PlayerInfo(String playername)
	{
		Drawnblocks = new ArrayList<Block>();
		BlockLocationList = new ArrayList<SerializableLocation>();
		hiddenblocks = new ArrayList<Material>();
		highlitedblocks = new ArrayList<Material>();
		plrname = playername;
		hiddenblocks.add(Material.STONE);
		hiddenblocks.add(Material.DIRT);
		hiddenblocks.add(Material.GRASS);
		hiddenblocks.add(Material.GRAVEL);
		hiddenblocks.add(Material.SAND);
		hiddenblocks.add(Material.SANDSTONE);
		highlitedblocks.add(Material.DIAMOND_ORE);
		highlitedblocks.add(Material.GOLD_ORE);
		highlitedblocks.add(Material.CHEST);
		highlitedblocks.add(Material.IRON_ORE);
	}
	public PlayerInfo(PlayerInfo info)
	{
		Drawnblocks = new ArrayList<Block>();
		BlockLocationList = new ArrayList<SerializableLocation>();
		hiddenblocks = new ArrayList<Material>();
		highlitedblocks = new ArrayList<Material>();
		plrname = info.getPlayerName();
		hiddenblocks = info.getHiddenBlocksCopy();
		highlitedblocks = info.getHighlitedBlocksCopy();
		
	}
	public String getPlayerName()
	{
		return plrname;
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
		this.glowstoneenabled = glowstoneenabled;
	}

	public boolean isGlowstoneenabled() {
		return glowstoneenabled;
	}
	
	public void addToHiddenblocks(Material hiddenblock)
	{
		if(!this.hiddenblocks.contains(hiddenblock))
		{
			hiddenblocks.add(hiddenblock);
		}
		return;
	}
	public void removeFromHiddenblocks(Material hiddenblock)
	{
		if(this.hiddenblocks.contains(hiddenblock))
		{
			this.hiddenblocks.remove(hiddenblock);
		}
		return;
	}
	public boolean shouldBeHidden(Material m)
	{
		return hiddenblocks.contains(m);
	}
	public String listHiddenBlocks()
	{
		String materialList = "";
		for(Material m : hiddenblocks)
		{
			materialList = materialList + m.toString() + " ";
		}
		return materialList;
	}
	public ArrayList<Material> getHiddenBlocksCopy()
	{
		ArrayList<Material> out = new ArrayList<Material>();
		for(Material m: hiddenblocks)
		{
			out.add(m);
		}
		return out;
	}
	
	public void addtohighlitedblocks(Material hiddenblock)
	{
		if(!this.highlitedblocks.contains(hiddenblock))
		{
			highlitedblocks.add(hiddenblock);
		}
		return;
	}
	public void removefromhighlitedblocks(Material highlitedblock)
	{
		if(this.highlitedblocks.contains(highlitedblock))
		{
			this.highlitedblocks.remove(highlitedblock);
		}
	}
	public boolean shouldBeHighlited(Material m)
	{
		return highlitedblocks.contains(m);
	}
	public String listHighlitedBlocks()
	{
		String materialList = "";
		for(Material m : highlitedblocks)
		{
			materialList = materialList + m.toString() + " ";
		}
		return materialList;
	}
	public ArrayList<Material> getHighlitedBlocksCopy()
	{
		ArrayList<Material> out = new ArrayList<Material>();
		for(Material m : highlitedblocks)
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
		this.highlitedblocks = plrinfo.getHighlitedBlocksCopy();
		this.hiddenblocks = plrinfo.getHiddenBlocksCopy();
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
