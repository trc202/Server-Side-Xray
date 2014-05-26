package com.trc202.ServerSideXray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ServerSideXray extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");	
	
	private final CXPlrListener PlayerListener = new CXPlrListener(this);
	private final CXBlockListener BlockListener = new CXBlockListener(this);
	private CommandManager cm = new CommandManager(this);
	
	static String mainDirectory = "plugins/ServerSideXray";
    private static File playerinfosaved = new File(mainDirectory + File.separator + "reloadinfo.ser");
    private static File playerinfopersistent = new File(mainDirectory + File.separator + "persistentPlrInfo.ser");
    private static File playerglowinfo = new File(mainDirectory + File.separator + "glowPlrInfo.ser");
	
	private volatile HashMap<UUID,PlayerInfo> playerlist = new HashMap<UUID,PlayerInfo>();
	private HashMap<UUID, PlayerInfo> persistentplrinfo = new HashMap<UUID, PlayerInfo>();
	private volatile HashMap<UUID, PlayerInfo> glowBlockPlayerList = new HashMap<UUID, PlayerInfo>();
	private volatile ArrayList<BlocksToBeSent> blocksunsentlist = new ArrayList<BlocksToBeSent>();
	
	@Override
	public void onDisable() {
		log.info("[" + this.getDescription().getName() + "]" + " is disabled.");
		saveReloadPlayerList();
		savePlayerList();
		saveGlowList();
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(PlayerListener, this);
		pm.registerEvents(BlockListener, this);
		scheduleTask();
		loadReloadPlayerList();
		loadPlayerList();
		loadGlowList();
		log.info("[" + this.getDescription().getName() + "]" + " is enabled.");

	}
	private void saveGlowList()
	{
		try {
			new File(mainDirectory).mkdir(); //directory for the config file
			if(!playerglowinfo.exists())
			{
				playerglowinfo.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(playerglowinfo);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Collection<PlayerInfo> playerinfovalues = glowBlockPlayerList.values();
			Iterator<PlayerInfo> i =playerinfovalues.iterator();
			while(i.hasNext())
			{
				PlayerInfo plrinfo = i.next();
				plrinfo.prepareForSerialization();
			}
			oos.writeObject(glowBlockPlayerList);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadGlowList()
	{
		try {
			if(!playerglowinfo.exists())
			{
				return;
			}
			FileInputStream fis = new FileInputStream(playerglowinfo);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
            if (obj instanceof HashMap<?,?>) {
            	@SuppressWarnings("unchecked")
				HashMap<String, PlayerInfo> temp = (HashMap<String, PlayerInfo>) obj;
            	Collection<PlayerInfo> c = temp.values();
            	Iterator<PlayerInfo> i = c.iterator();
            	while(i.hasNext())
            	{
            		PlayerInfo plr = i.next();
            		if((getServer().getPlayer(plr.getPlayerUUID()) != null) && (getServer().getPlayer(plr.getPlayerUUID()).isOnline()))
            		{
            			log.info("Loaded " + getServer().getPlayer(plr.getPlayerUUID()).getName() + " into glow player list");
            			plr.deSerilize(this);
            			glowBlockPlayerList.put(plr.getPlayerUUID(), plr);
            		}
            	}
            }
        	fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			log.info("Server Side Xray has encountered an error loading from the file");
			playerinfosaved.delete();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void savePlayerList()
	{
		try {
			new File(mainDirectory).mkdir(); //directory for the config file
			if(!playerinfopersistent.exists())
			{
				playerinfopersistent.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(playerinfopersistent);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(persistentplrinfo);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPlayerList()
	{
		try {
			if(!playerinfopersistent.exists())
			{
				return;
			}
			FileInputStream fis = new FileInputStream(playerinfopersistent);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
            if (obj instanceof HashMap<?,?>) {
            	@SuppressWarnings("unchecked")
				HashMap<String, PlayerInfo> temp = (HashMap<String, PlayerInfo>) obj;
            	Collection<PlayerInfo> c = temp.values();
            	Iterator<PlayerInfo> i = c.iterator();
            	while(i.hasNext())
            	{
            		PlayerInfo plr = i.next();
            		persistentplrinfo.put(plr.getPlayerUUID(), plr);
            	}
            }
        	fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			log.info("Server Side Xray has encountered an error loading from the file");
			playerinfosaved.delete();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveReloadPlayerList()
	{
		try {
			new File(mainDirectory).mkdir(); //directory for the config file
			if(!playerinfosaved.exists())
			{
				playerinfosaved.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(playerinfosaved);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Collection<PlayerInfo> playerinfovalues = playerlist.values();
			Iterator<PlayerInfo> i =playerinfovalues.iterator();
			while(i.hasNext())
			{
				PlayerInfo plrinfo = i.next();
				plrinfo.prepareForSerialization();
			}
			oos.writeObject(playerlist);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadReloadPlayerList()
	{
		try {
			if(!playerinfosaved.exists())
			{
				return;
			}
			FileInputStream fis = new FileInputStream(playerinfosaved);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
            if (obj instanceof HashMap<?,?>) {
            	@SuppressWarnings("unchecked")
				HashMap<String, PlayerInfo> playerinfohashmap = (HashMap<String, PlayerInfo>) obj;
            	Collection<PlayerInfo> values = playerinfohashmap.values();
            	Iterator<PlayerInfo> i = values.iterator();
            	while(i.hasNext())
            	{
            		PlayerInfo temp = i.next();
            		if(getServer().getPlayer(temp.getPlayerUUID()) != null && getServer().getPlayer(temp.getPlayerUUID()).isOnline())
            		{
            			temp.deSerilize(this);
            			addToPlayerList(temp);	
            		}
            	}
            }
            fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			log.info("Server Side Xray has encountered an error loading from the file");
			playerinfosaved.delete();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void scheduleTask() {
		BukkitScheduler schedule = getServer().getScheduler();
		schedule.scheduleSyncRepeatingTask(this, new Runnable() {

		    @SuppressWarnings("deprecation")
			public void run() 
		    {
	    		ArrayList<BlocksToBeSent> synclist = blocksunsentlist;
		    	if(synclist.isEmpty())
		    	{
		    		return;
		    	}
		    	Iterator<BlocksToBeSent> BlockIterator = synclist.iterator();
				while ( BlockIterator.hasNext() )
				{
					BlocksToBeSent blockstobesent = BlockIterator.next();
					if(blockstobesent.waitATurn() == true)
					{
						blockstobesent.setWait(false);
					}
					else if(blockstobesent.hasQuedblocks() == true)
		    		{
		    			Player plr = getServer().getPlayer(blockstobesent.getPlayer());
		    			if(plr != null && plr.isOnline())
		    			{
		    				ArrayList<Block> glassLocations = blockstobesent.getGlass(100);
		    				for(Block b : glassLocations)
		    				{
		    					if(b.getType() != Material.AIR)
		    					{
		    						plr.sendBlockChange(b.getLocation(), Material.GLASS, (byte)0);
		    					}
		    				}
		    				ArrayList<Block> originalblocks = blockstobesent.getOriginal(100);
		    				for(Block b : originalblocks)
		    				{
		    					plr.sendBlockChange(b.getLocation(), b.getTypeId(), b.getData());
		    				}
		    				ArrayList<Block> glowstoneLocations = blockstobesent.getGlowstone(100);
		    				for(Block b : glowstoneLocations)
		    				{
		    					plr.sendBlockChange(b.getLocation(), Material.GLOWSTONE, (byte)0);
		    				}
		    			}
		    			else
		    			{
		    				BlockIterator.remove();
		    			}
		    			
		    		}
		    		else
		    		{
		    			BlockIterator.remove();
		    		}
				}
		    	}
		}, 0L, 5L);
		
	}

	public PlayerInfo getPlrInfo(UUID playerUUID)
	{
		return playerlist.get(playerUUID);
	}
	public PlayerInfo getGlowstonePlrInfo(UUID playerUUID)
	{
		return glowBlockPlayerList.get(playerUUID);
	}
	
	/*
	 * Will take the x,y,z and get +/- that many blocks in each direction from location
	 * Adds the blocks into the hashmap for blocks to be sent
	 */
	@SuppressWarnings("deprecation")
	public BlocksToBeSent getFilteredBlocksinRadius(Location location, Player p , PlayerInfo plrInfo) {
		int thex = plrInfo.getXarea();
		int they = plrInfo.getYarea();
		int thez = plrInfo.getZarea();
		boolean getGlowstone = plrInfo.isGlowstoneenabled();
		ArrayList<Block> hiddenblocks = new ArrayList<Block>();
		ArrayList<Block> blocksbelowores = new ArrayList<Block>();
		thex = Math.abs(thex);
		they = Math.abs(they);
		thez = Math.abs(thez);
		int negx = (thex * -1);
		int negy = (they * -1);
		int negz = (thez * -1);
		World theworld = location.getWorld();
		for(int y = negy;y < they;y++ )
		{
			if(location.getY() + y >= 0)
			{
				for(int x = negx; x< thex; x++)
				{
					for(int z = negz; z < thez; z++)
					{
						int blockid = theworld.getBlockTypeIdAt((location.getBlockX() + x), (location.getBlockY() + y), (location.getBlockZ() + z));
							if(plrInfo.shouldBeHidden(Material.getMaterial(blockid)))
							{
								Block ablock = theworld.getBlockAt((location.getBlockX() + x), (location.getBlockY() + y), (location.getBlockZ() + z));
								hiddenblocks.add(ablock);
							}
							else if(getGlowstone == true)
							{
								if(plrInfo.shouldBeHighlighted(Material.getMaterial(blockid)))
								{
									Block ablock = theworld.getBlockAt((location.getBlockX() + x), (location.getBlockY() + y), (location.getBlockZ() + z));
									if(!(plrInfo.shouldBeHighlighted(Material.getMaterial(ablock.getRelative(BlockFace.DOWN).getTypeId()))))
									{
										blocksbelowores.add(ablock.getRelative(BlockFace.DOWN));
									}
								}
							}
							else
							{
								//do nothing
							}
					}
				}
			}
		}
		for(Block glowblock : blocksbelowores)
		{
			if(hiddenblocks.contains(glowblock))
			{
				hiddenblocks.remove(glowblock);
			}
		}
		BlocksToBeSent tmpholder = new BlocksToBeSent(p.getUniqueId());
		tmpholder.addToGlass(hiddenblocks);
		tmpholder.addToGlowstone(blocksbelowores);
		return tmpholder;
	}
	
	/*
	 * Checks the hashmap and removes any blocks that were already in the hashmap
	 */
	public BlocksToBeSent removePreviouslySentBlocks(BlocksToBeSent blocklist, PlayerInfo plrinfo)
	{
		ArrayList<Block> previouslydrawn = plrinfo.getDrawnBlocks();
		Iterator<Block> glassIterator = blocklist.getGlass().iterator();
		while ( glassIterator.hasNext() ){
			Block b = glassIterator.next();
			if(previouslydrawn.contains(b))
			{
				glassIterator.remove();
			}
		  }
		Iterator<Block> glowIterator = blocklist.getGlowstone().iterator();
		while ( glowIterator.hasNext() ){
			Block b = glowIterator.next();
			if(previouslydrawn.contains(b))
			{
				glowIterator.remove();
			}
		}
		return blocklist;
	}


	/*
	 * Removes all blocks from the drawn block list
	 */
	public void clearDrawnBlocks(UUID playerUUID)
	{
		PlayerInfo plrinfo = getPlrInfo(playerUUID);
		plrinfo.clearDrawnBlocks();
	}
	public void clearDrawnGlowBlocks(UUID playerUUID)
	{
		PlayerInfo plrinfo = getGlowstonePlrInfo(playerUUID);
		plrinfo.clearDrawnBlocks();
	}

	public boolean isAnyOneUsingXray()
	{
		return (!playerlist.isEmpty());
	}
	public boolean isUsingXRay(UUID playerUUID) {
		if(playerlist.containsKey(playerUUID))
		{
			return true;
		}
		return false;
	}
	public boolean isUsingGlowBlock(UUID playerUUID) {
		if(glowBlockPlayerList.containsKey(playerUUID))
		{
			return true;
		}
		return false;
	}
	
	public void addtoBlockSendingList(BlocksToBeSent btbs)
	{
		blocksunsentlist.add(btbs);
	}
	
	public ArrayList<Player> getPlayersinChunk(Chunk chunkin)
	{
		//long start = System.currentTimeMillis();
		ArrayList<Player> players = new ArrayList<Player>();
		for(int x = -1; x <= 1; x++)
		{
			for(int z = -1; z <= 1; z++)
			{
				Chunk chunk = chunkin.getWorld().getChunkAt(chunkin.getX() + x, chunkin.getZ() + z);
				for(Entity possableplayer : chunk.getEntities())
				{
					if( possableplayer instanceof Player)
					{
						players.add((Player) possableplayer);
					}
				}
			}
		}
		//long finish = System.currentTimeMillis();
		//log.info("took " + (finish-start) + " miliseconds to get through getPlayersInChunk");
		//log.info("Equates to " + ((finish-start)/50)  + " ticks");
		return players;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if(command.getName().equalsIgnoreCase("sx") || command.getName().equalsIgnoreCase("Xray"))
		{
			if(sender instanceof Player)
			{
				Player plr = (Player) sender;
				return cm.processXrayCommand(plr, args);
			}
			else
			{
				log.info("[Server Side Xray] Player Only Command Used");
				return true;
			}
		}
		else if(command.getName().equalsIgnoreCase("gs") || command.getName().equalsIgnoreCase("Glow"))
		{
			if(sender instanceof Player)
			{
				Player plr = (Player) sender;
				return cm.processGlowstoneCommand(plr, args);
			}
			else
			{
				log.info("[Server Side Xray] Player Only Command Used");
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	public void addToPersistantplayerList(PlayerInfo p)
	{
		PlayerInfo store = new PlayerInfo(p.getPlayerUUID());
		store.importHiddenHighlightedBlocks(p);
		persistentplrinfo.put(store.getPlayerUUID(), store);
	}
	public PlayerInfo getFromPersistantplayerList(UUID playerUUID)
	{
		if(persistentplrinfo.containsKey(playerUUID))
		{
			PlayerInfo p = persistentplrinfo.get(playerUUID);
			PlayerInfo out = new PlayerInfo(p);
			return out;
		}
		else
		{
			return new PlayerInfo(playerUUID);
		}
	}

	public void addToPlayerList(PlayerInfo p)
	{
		playerlist.put(p.getPlayerUUID(), p);
	}
	public void removeFromPlayerList(UUID playerUUID)
	{
		playerlist.remove(playerUUID);
	}
	public void addToGlowPlayerList(PlayerInfo p)
	{
		glowBlockPlayerList.put(p.getPlayerUUID(), p);
	}
	public void removeFromGlowPlayerList(UUID playerUUID)
	{
		glowBlockPlayerList.remove(playerUUID);
		return;
	}
}
