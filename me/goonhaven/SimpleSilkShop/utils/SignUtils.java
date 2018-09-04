package me.goonhaven.SimpleSilkShop.utils;

import org.bukkit.block.Sign;

import net.md_5.bungee.api.ChatColor;

public class SignUtils {
	public static String getSignType(Sign sign) {
		String type = "INVALID";
		String[] signLines = sign.getLines();
		if (signLines.length == 4) {
			if (signLines[0].equalsIgnoreCase("[Buy Spawner]") || signLines[0].equalsIgnoreCase("[bs]") || signLines[0].equalsIgnoreCase("§5[Buy Spawner]")) {
				try {
					if ((Integer.parseInt(signLines[1]) > 0)) {
						if (isValidMob(signLines[2])) {
							if (signLines[3].matches("\\$?[1-9]+[0-9]*(\\.[0-9]+)?")) {
								type = "BUY";
							} else
								type = "INVALID_PRICE";
						} else
							type = "INVALID_MOB";
					} else
						type = "INVALID_QTY";
				} catch (NumberFormatException nfe) {
					type = "INVALID_QTY";
				}
			}
			if (signLines[0].equalsIgnoreCase("[Sell Spawner]") || signLines[0].equalsIgnoreCase("[ss]") || signLines[0].equalsIgnoreCase("§5[Sell Spawner]")) {
				try {
					if ((Integer.parseInt(signLines[1]) > 0)) {
						if (isValidMob(signLines[2])) {
							if (signLines[3].matches("\\$?[1-9]+[0-9]*(\\.[0-9]+)?")) {
								type = "SELL";
							} else
								type = "INVALID_PRICE";
						} else
							type = "INVALID_MOB";
					} else
						type = "INVALID_QTY";
				} catch (NumberFormatException nfe) {
					type = "INVALID_QTY";
				}
			}
		}
		return type;
	}

	// Checks sign type and returns formatted (beautified) sign.
	public static Sign formatSign(Sign sign, String signType) {
		if (signType.equals("INVALID_QTY"))
			sign.setLine(1, ChatColor.DARK_RED + sign.getLine(1));
		if (signType.equals("INVALID_MOB"))
			sign.setLine(2, ChatColor.DARK_RED + sign.getLine(2));
		if (signType.equals("INVALID_PRICE"))
			sign.setLine(3, ChatColor.DARK_RED + sign.getLine(3));
		if (signType.equals("BUY") || signType.equals("SELL")) {
			sign.setLine(0, ChatColor.DARK_PURPLE + "[" + formaliseText(signType) + " Spawner]");
			String line2 = sign.getLine(2);
			// Capitalise first letter of index 2
			line2 = formaliseText(line2);
			sign.setLine(2, line2);
			String line3 = sign.getLine(3);
			if (!line3.contains("$"))
				sign.setLine(3, String.format("$%.2f", Double.parseDouble(line3)));
		}
		// if INVALID, do nothing
		return sign;
		
	}

	public static boolean isValidMob(String mob) {
		String uCMob = mob.toUpperCase();
		if (uCMob.equals("BAT"))
			return true;
		if (uCMob.equals("BLAZE"))
			return true;
		if (uCMob.equals("CAVE_SPIDER"))
			return true;
		if (uCMob.equals("CHICKEN"))
			return true;
		if (uCMob.equals("COD"))
			return true;
		if (uCMob.equals("COW"))
			return true;
		if (uCMob.equals("CREEPER"))
			return true;
		if (uCMob.equals("DOLPHIN"))
			return true;
		if (uCMob.equals("DONKEY"))
			return true;
		if (uCMob.equals("DROWNED"))
			return true;
		if (uCMob.equals("ELDER_GUARDIAN"))
			return true;
		if (uCMob.equals("ENDER_DRAGON"))
			return true;
		if (uCMob.equals("ENDERMAN"))
			return true;
		if (uCMob.equals("ENDERMITE"))
			return true;
		if (uCMob.equals("EVOKER"))
			return true;
		if (uCMob.equals("GHAST"))
			return true;
		if (uCMob.equals("GIANT"))
			return true;
		if (uCMob.equals("GUARDIAN"))
			return true;
		if (uCMob.equals("HORSE"))
			return true;
		if (uCMob.equals("HUSK"))
			return true;
		if (uCMob.equals("ILLUSIONER"))
			return true;
		if (uCMob.equals("IRON_GOLEM"))
			return true;
		if (uCMob.equals("LLAMA"))
			return true;
		if (uCMob.equals("MAGMA_CUBE"))
			return true;
		if (uCMob.equals("MULE"))
			return true;
		if (uCMob.equals("OCELOT"))
			return true;
		if (uCMob.equals("PHANTOM"))
			return true;
		if (uCMob.equals("PIG"))
			return true;
		if (uCMob.equals("PIG_ZOMBIE"))
			return true;
		if (uCMob.equals("POLAR_BEAR"))
			return true;
		if (uCMob.equals("PUFFER_FISH"))
			return true;
		if (uCMob.equals("RABBIT"))
			return true;
		if (uCMob.equals("SALMON"))
			return true;
		if (uCMob.equals("SHEEP"))
			return true;
		if (uCMob.equals("SHULKER"))
			return true;
		if (uCMob.equals("SILVERFISH"))
			return true;
		if (uCMob.equals("SKELETON"))
			return true;
		if (uCMob.equals("SKELETON_HORSE"))
			return true;
		if (uCMob.equals("SLIME"))
			return true;
		if (uCMob.equals("SNOWMAN"))
			return true;
		if (uCMob.equals("SPIDER"))
			return true;
		if (uCMob.equals("SQUID"))
			return true;
		if (uCMob.equals("STRAY"))
			return true;
		if (uCMob.equals("TROPICAL_FISH"))
			return true;
		if (uCMob.equals("TURTLE"))
			return true;
		if (uCMob.equals("VEX"))
			return true;
		if (uCMob.equals("VILLAGER"))
			return true;
		if (uCMob.equals("VINDICATOR"))
			return true;
		if (uCMob.equals("WITCH"))
			return true;
		if (uCMob.equals("WITHER"))
			return true;
		if (uCMob.equals("WITHER_SKELETON"))
			return true;
		if (uCMob.equals("WOLF"))
			return true;
		if (uCMob.equals("ZOMBIE"))
			return true;
		if (uCMob.equals("ZOMBIE_HORSE"))
			return true;
		if (uCMob.equals("ZOMBIE_VILLAGER"))
			return true;
		return false;
	}

	public static String formaliseText(String text) {
		return text.substring(0, 1).toUpperCase()
				+ ((text.length() > 1) ? text.substring(1, text.length()).toLowerCase() : "");
	}
}
