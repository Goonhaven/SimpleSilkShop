package me.goonhaven.SimpleSilkShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.goonhaven.SimpleSilkShop.utils.SignUtils;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class SignClickEvent implements Listener {
	private SimpleSilkShop plugin;
	private Economy economy;

	public SignClickEvent(SimpleSilkShop sssInstance) {
		this.plugin = sssInstance;
		economy = plugin.getEconomy();
	}

	@EventHandler
	public void onSignClick(PlayerInteractEvent pie) {
		Player player = pie.getPlayer();
		Block block = pie.getClickedBlock();
		if (block != null && block.getType().equals(Material.SIGN)) {
			if (pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Sign sign = (Sign) block.getState();
				String signType = SignUtils.getSignType(sign);
				if (signType.equals("BUY"))
					buy(sign, player);
				if (signType.equals("SELL"))
					sell(sign, player);
			}
		}
	}

	private void buy(Sign buySign, Player playerBuying) {
		int qtyBuying = Integer.parseInt(buySign.getLine(1));
		String item = buySign.getLine(2).toUpperCase();
		double price = Double.parseDouble(buySign.getLine(3).substring(1, buySign.getLine(3).length()));
		double balance = economy.getBalance(playerBuying);
		// Check if empty slots (ONLY storage contents, not incl armour / off-hand) or
		// player has itemstack with qty <= (fullstack - qtyToBuy).
		if (balance >= price) {
			if (giveItems(item, qtyBuying, playerBuying)) {
				economy.withdrawPlayer(playerBuying, price);
				playerBuying.sendMessage(
						ChatColor.RED + String.format("$%.2f", price) + ChatColor.GOLD + " taken from your account!");
			}
			else
				playerBuying.sendMessage(ChatColor.YELLOW + "No inventory space for spawner(s)!");
		} else
			playerBuying.sendMessage(ChatColor.GOLD + "Insufficient funds!");
	}

	private void sell(Sign sellSign, Player playerSelling) {
		int qtySelling = Integer.parseInt(sellSign.getLine(1));
		String item = sellSign.getLine(2).toUpperCase();
		double price = Double.parseDouble(sellSign.getLine(3).substring(1, sellSign.getLine(3).length()));
		Inventory inv = playerSelling.getInventory();

		ItemStack spawnerItem = new ItemStack(Material.SPAWNER, 1);
		ItemMeta spawnerMeta = spawnerItem.getItemMeta();
		spawnerMeta.setDisplayName(ChatColor.AQUA + SignUtils.formaliseText(item) + " Spawner");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(item);
		spawnerMeta.setLore(lore);
		spawnerItem.setItemMeta(spawnerMeta);

		if (inv.containsAtLeast(spawnerItem, qtySelling)) {
			spawnerItem.setAmount(qtySelling);
			// Removes specific item / qty from player inv and will remove < qty if possible
			// as well - hence the if statement.
			inv.removeItem(spawnerItem);
			economy.depositPlayer(playerSelling, price);
			playerSelling.sendMessage(
					ChatColor.AQUA + String.format("$%.2f", price) + ChatColor.DARK_GREEN + " added to your account.");

		} else
			playerSelling.sendMessage(ChatColor.YELLOW + "You don't have " + qtySelling + " " + ChatColor.LIGHT_PURPLE
					+ SignUtils.formaliseText(item) + " Spawner(s) " + ChatColor.YELLOW + "in your inventory!");
	}

	// Item is passed with all CAPS. Private because very specific to this plugin
	// (only works with spawners).
	private boolean giveItems(String item, int quantity, Player player) {
		ItemStack spawnerItem = new ItemStack(Material.SPAWNER, quantity);
		ItemMeta spawnerMeta = spawnerItem.getItemMeta();
		spawnerMeta.setDisplayName(ChatColor.AQUA + SignUtils.formaliseText(item) + " Spawner");

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(item);
		spawnerMeta.setLore(lore);
		spawnerItem.setItemMeta(spawnerMeta);
		Inventory inv = player.getInventory();
		HashMap<Integer, ? extends ItemStack> invSpawners;
		if (inv.firstEmpty() > -1) {
			inv.addItem(spawnerItem);
			return true;
		} else {
			invSpawners = inv.all(Material.SPAWNER);
			if (!invSpawners.isEmpty()) {
				Iterator<Integer> invSpawnersKeys = invSpawners.keySet().iterator();
				ItemStack currentItem;
				boolean incrementableItem = false;
				while (!incrementableItem && invSpawnersKeys.hasNext()) {
					currentItem = invSpawners.get(invSpawnersKeys.next());
					if (currentItem.isSimilar(spawnerItem)
							&& (currentItem.getAmount() <= (spawnerItem.getMaxStackSize() - quantity))) {
						incrementableItem = true;
					}
				}
				if (incrementableItem) {
					inv.addItem(spawnerItem);
					return true;
				}
			}
		}
		/*
		 * if (invOverflow != null && !invOverflow.isEmpty()) { for (int i = 0; i <
		 * invOverflow.size(); i++) { player.getWorld().dropItem(player.getLocation(),
		 * invOverflow.get(i)); invOverflow.remove(i); }
		 * player.sendMessage(ChatColor.DARK_AQUA +
		 * "Some items were dropped as you have no inventory space!"); } else
		 * System.out.println("Overflow was empty or null!");
		 */
		return false;
	}
// End class SignClickEvent
}
