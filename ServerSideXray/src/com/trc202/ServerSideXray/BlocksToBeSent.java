package com.trc202.ServerSideXray;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;

public class BlocksToBeSent {

	private ArrayList<Block> drawAsGlowstone;
	private ArrayList<Block> drawAsGlass;
	private ArrayList<Block> drawAsOriginal;
	private UUID playerUUID;
	private boolean wait;
	
	public BlocksToBeSent(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
		drawAsGlowstone = new ArrayList<Block>();
		drawAsGlass = new ArrayList<Block>();
		drawAsOriginal = new ArrayList<Block>();
		wait = false;
	}
	
	public UUID getPlayer()
	{
		return playerUUID;
	}
	/*
	 * Returns all blocks in glowstone list
	 */
	public ArrayList<Block> getGlowstone()
	{
		return drawAsGlowstone;
	}
	public ArrayList<Block> getGlass()
	{
		return drawAsGlass;
	}
	/*
	 * removes blocks from the list and returns amount of blocks specified if the list contains that many 
	 * otherwise returns what ever is left in the list
	 */
	public ArrayList<Block> getGlowstone(int amount)
	{
		if(drawAsGlowstone.size() <= amount )
		{
			ArrayList<Block> returnedlist = new ArrayList<Block>(drawAsGlowstone);
			drawAsGlowstone.clear();
			return returnedlist;
		}
		else
		{
			ArrayList<Block> partalglowstone = new ArrayList<Block>();
			for(int i = 0; i < amount; i++)
			{
				partalglowstone.add(drawAsGlowstone.get(0));
				drawAsGlowstone.remove(0);
			}
			return partalglowstone;
			
		}
	}
	/*
	 * removes blocks from the list and returns amount of blocks specified if the list contains that many 
	 * otherwise returns what ever is left in the list 
	 */
	public ArrayList<Block> getGlass(int amount)
	{
		if(drawAsGlass.size() <= amount )
		{
			ArrayList<Block> returnedlist = new ArrayList<Block>(drawAsGlass);
			drawAsGlass.clear();
			return returnedlist;
		}
		else
		{
			ArrayList<Block> partalglowstone = new ArrayList<Block>();
			for(int i = 0; i < amount; i++)
			{
				partalglowstone.add(drawAsGlass.get(0));
				drawAsGlass.remove(0);
			}
			return partalglowstone;
			
		}
	}
	public ArrayList<Block> getOriginal(int amount)
	{
		if(drawAsOriginal.size() <= amount )
		{
			ArrayList<Block> returnedlist = new ArrayList<Block>(drawAsOriginal);
			drawAsOriginal.clear();
			return returnedlist;
		}
		else
		{
			ArrayList<Block> partaloriginal = new ArrayList<Block>();
			for(int i = 0; i < amount; i++)
			{
				partaloriginal.add(drawAsOriginal.get(0));
				drawAsOriginal.remove(0);
			}
			return partaloriginal;
		}
	}
	public boolean hasQuedblocks()
	{
		if((drawAsGlowstone.size() > 0) || (drawAsGlass.size() > 0) || (drawAsOriginal.size() > 0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean waitATurn()
	{
		return wait;
	}
	public void setWait(boolean b)
	{
		wait = b;
	}
	
	public void removeBlockFromGlass(Block b)
	{
		drawAsGlass.remove(b);
	}
	public void removeBlockFromGlowstone(Block b)
	{
		drawAsGlowstone.remove(b);
	}
	
	public void addToGlowstone(ArrayList<Block> glowstoneblocks)
	{
		drawAsGlowstone.addAll(glowstoneblocks);
	}
	public void addToGlass(ArrayList<Block> glassblocks)
	{
		drawAsGlass.addAll(glassblocks);
	}
	public void addToOriginal(ArrayList<Block> blocks)
	{
		drawAsOriginal.addAll(blocks);
	}
}
