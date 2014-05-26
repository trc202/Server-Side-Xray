package com.trc202.ServerSideXray;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CommandManager {
    private ServerSideXray plugin;
    
	public CommandManager(ServerSideXray plugin)
    {
		this.plugin = plugin;
    }
	
	public boolean processXrayCommand(Player player, String[] split)
	{
		if(player.hasPermission("ServerSideXray.Xray") || player.isOp())
		{
			Boolean startedxraying = false;
			PlayerInfo plrinfo = null;
			if(plugin.isUsingXRay(player.getUniqueId()))
			{
				plrinfo = plugin.getPlrInfo(player.getUniqueId());
			}
			else
			{
				plrinfo = plugin.getFromPersistantplayerList(player.getUniqueId());
				startedxraying = true;
			}
			Material blockmaterial = null;
			
			if(split.length == 0)
			{
				if(plugin.isUsingXRay(player.getUniqueId()))
				{
					BlocksToBeSent btbs = new BlocksToBeSent(player.getUniqueId());
					btbs.addToOriginal(plrinfo.getDrawnBlocks());
					plugin.addtoBlockSendingList(btbs);
					plugin.removeFromPlayerList(player.getUniqueId());
					player.sendMessage(ChatColor.RED + "Stopped Xraying");
					plugin.log.info(player.getName() + " Stopped Xraying");
					plugin.addToPersistantplayerList(plrinfo);
				}
				else
				{
					plugin.log.info(player.getName() + " Started Xraying");
					player.sendMessage(ChatColor.RED + "Started Xraying move to begin");
					plugin.addToPlayerList(plrinfo);
					
				}
				return true;
			}
			else if(split.length == 1)
			{
				if(split[0].equalsIgnoreCase("help"))
				{
					displayHelp(player);
				}
				else if(split[0].equalsIgnoreCase("list"))
				{
					player.sendMessage(ChatColor.RED + "Hidden Blocks: "+ChatColor.WHITE +plrinfo.listHiddenBlocks());
					player.sendMessage(ChatColor.RED + "Highlited Blocks: " + ChatColor.WHITE + plrinfo.listHighlightedBlocks());
					//List blocks hidden and highlighted
					return true;
				}
				else
				{
					displayHelp(player);
					player.sendMessage(ChatColor.RED + "Invalid Parameters");
				}
				return true;
			}		
			else if(split.length >= 3)
			{
				displayHelp(player);
				player.sendMessage(ChatColor.RED + "Invalid Parameters");
				//too few parameters
				//or too many parameters
				//notify player
				return true;
			}
			else if(split.length == 2)
			{
				if(split[0].equalsIgnoreCase("hide"))
				{
					blockmaterial = getMaterial(split[1]);
					if(blockmaterial == Material.AIR)
					{
						player.sendMessage(ChatColor.RED + "Invalid Block Type");
						return true;
					}
					if(isConflicting(plrinfo, blockmaterial,"Hide" ))
					{
						player.sendMessage(ChatColor.RED + "Conflicting blocks detected. Canceling.");
						return true;
					}
					plrinfo.addToHiddenblocks(blockmaterial);
					//Add material to players playerinfo
					if(startedxraying == true)
					{
						plugin.log.info(player.getName() + " Started Xraying");
						player.sendMessage(ChatColor.RED + "Started Xraying move to begin");
					}
					player.sendMessage(ChatColor.RED + "Hideing " + blockmaterial.name());
					plugin.addToPlayerList(plrinfo);
					return true;
				}
				else if(split[0].equalsIgnoreCase("show"))
				{
					blockmaterial = getMaterial(split[1]);
					if(blockmaterial == Material.AIR)
					{
						player.sendMessage(ChatColor.RED + "Invalid Block Type");
						return true;
					}
					
					//remove material to players playerinfo
					plrinfo.removeFromHiddenblocks(blockmaterial);
					ArrayList<Block> nowshown = plrinfo.getDrawnBlocksofType(blockmaterial);
					BlocksToBeSent nowshownblocks = new BlocksToBeSent(player.getUniqueId());
					nowshownblocks.addToOriginal(nowshown);
					plrinfo.removeDrawnBlocksofType(blockmaterial);
					plugin.addtoBlockSendingList(nowshownblocks);
					//Should iterate through hiddenblocks and remove all instances of it
					if(startedxraying == true)
					{
						plugin.log.info(player.getName() + " Started Xraying");
						player.sendMessage(ChatColor.RED + "Started Xraying move to begin");
					}
					player.sendMessage(ChatColor.RED + "Showing " + blockmaterial.name());
					plugin.addToPlayerList(plrinfo);
					return true;
				}
				else if(split[0].equalsIgnoreCase("highlight"))
				{
					blockmaterial = getMaterial(split[1]);
					if(blockmaterial == Material.AIR)
					{
						player.sendMessage(ChatColor.RED + "Invalid Block Type");
						return true;
					}
					if(isConflicting(plrinfo, blockmaterial,"Highlight" ))
					{
						player.sendMessage(ChatColor.RED + "Conflicting blocks detected. Canceling.");
						return true;
					}
					plrinfo.addToHighlightedBlocks(blockmaterial);
					if(startedxraying == true)
					{
						plugin.log.info(player.getName() + " Started Xraying");
						player.sendMessage(ChatColor.RED + "Started Xraying move to begin");
					}
					player.sendMessage(ChatColor.RED + "Highlighting " + blockmaterial.name());
					plugin.addToPlayerList(plrinfo);
					return true;
				}
				else if(split[0].equalsIgnoreCase("unhighlight"))
				{
					blockmaterial = getMaterial(split[1]);
					if(blockmaterial == Material.AIR)
					{
						player.sendMessage(ChatColor.RED + "Invalid Block Type");
						return true;
					}
					plrinfo.removefromhighlitedblocks(blockmaterial);
					//remove material from players playerinfo
					if(startedxraying == true)
					{
						plugin.log.info(player.getName() + " Started Xraying");
						player.sendMessage(ChatColor.RED + "Started Xraying move to begin");
					}
					player.sendMessage(ChatColor.RED + "Will no longer highlight " + blockmaterial.name());
					plugin.addToPlayerList(plrinfo);
					return true;
				}
				else
				{
					displayHelp(player);
					return true;
					//invalid command
				}
			}
		}
		displayHelp(player);
		return true;
	}
	
	 public boolean processGlowstoneCommand(Player player, String[] split)
	 {
		 if(player.hasPermission("ServerSideXray.glowstone") || player.isOp())
		 {
			 if(split.length == 0)
			 {
				 PlayerInfo plrinfo = null;
				 if(plugin.isUsingGlowBlock(player.getUniqueId()))
				 {
					 plrinfo = plugin.getGlowstonePlrInfo(player.getUniqueId());
				 }
				 else
				 {
					 plrinfo = new PlayerInfo(player.getUniqueId());
				 }
				 
				 if(plugin.isUsingGlowBlock(player.getUniqueId()))
				 {
					 BlocksToBeSent btbs = new BlocksToBeSent(player.getUniqueId());
					 btbs.addToOriginal(plrinfo.getDrawnBlocks());
					 plugin.addtoBlockSendingList(btbs);
					 plugin.removeFromGlowPlayerList(player.getUniqueId());
					 player.sendMessage(ChatColor.RED + "Stopped using Client glowstone");
					 return true;
				 }
				 else
				 {
					 plugin.addToGlowPlayerList(plrinfo);
					 player.sendMessage(ChatColor.RED + "Client side glowstone enabled");
					 player.sendMessage(ChatColor.RED + "Right Click a block with your bare hand to use");
					 //Send player message here
					 return true;
				 }
			 }
			 else
			 {
				 player.sendMessage(ChatColor.RED + "Invalid arguments. No arguments supported");
				 return false;
			 }
		 }
		 else
		 {
			 player.sendMessage(ChatColor.RED + "You lack the talent to use this spell");
			 //Player does not have permission for this command
			 return false;
		 }
	 }

	 private void displayHelp(Player p)
	 {
		 p.sendMessage(ChatColor.RED + "-----Server Side Xray-----");
		 p.sendMessage("Xray "+ ChatColor.DARK_PURPLE + "- Toggles ServerSide Xray with default blocks");
		 p.sendMessage("Xray hide [block] "+ ChatColor.DARK_PURPLE + "- Hides block from you");
		 p.sendMessage("Xray show [block] "+ ChatColor.DARK_PURPLE + "- Shows block to you");
		 p.sendMessage("Xray highlight [block] " + ChatColor.DARK_PURPLE +  "- Puts glowstone under block");
		 p.sendMessage("Xray unhighlight [block] "+ChatColor.DARK_PURPLE +  "- Removes glowstone under block");
		 p.sendMessage("Xray list " + ChatColor.DARK_PURPLE + "- Lists hidden & highlited blocks");
		 p.sendMessage("Glow " + ChatColor.DARK_PURPLE + "- Toggles right click(empty hand) client side glowstone");
		 p.sendMessage("Xray help "+ChatColor.DARK_PURPLE + "- Shows help");
		 
	 }

	 private boolean isConflicting(PlayerInfo plr, Material m, String s)
	 {
		 if(s.equalsIgnoreCase("Highlight"))
		 {
			 if(plr.shouldBeHidden(m))
			 {
				 return true;
			 }
			 return false;
		 }
		 else if(s.equalsIgnoreCase("Hide"))
		 {
			 if(plr.shouldBeHighlighted(m))
			 {
				 return true;
			 }
			 return false;
		 }
			 
		 return true;
	 }
	 
	 public static boolean isInteger(String str)
	 {
	     try
	     {
	    	 Integer.parseInt(str);
	    	 return true;
	     }
	     catch (NumberFormatException e)
	     {
	    	 return false;
	     }
	     
	 }
	 
	 @SuppressWarnings("deprecation")
	private Material getMaterial(String input)
	 {
		 Material blockmaterial;
		 if(isInteger(input))
			{
				int idnum = Integer.parseInt(input);
				blockmaterial = Material.getMaterial(idnum);
				if((blockmaterial == null) || (!blockmaterial.isBlock()))
				{
					return Material.AIR;
				}					
			}
			else 
			{
				String blockname = input.toUpperCase();
				blockmaterial = Material.getMaterial(blockname);
				if((blockmaterial == null) || (!blockmaterial.isBlock()))
				{
					return Material.AIR;
				}	
			}
		 return blockmaterial;
	 }

}
