package me.goonhaven.SimpleSilkShop;

//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import me.goonhaven.SimpleSilkShop.utils.SignUtils;

public class SignPlaceEvent implements Listener {
	@EventHandler
	public void onSignChange(SignChangeEvent sce) {
		Sign newSign = (Sign) sce.getBlock().getState();
		// Populate Sign object (clone) with contents (empty otherwise) so we can run
		// the getSignType method.
		for (int i = 0; i < 4; i++)
			newSign.setLine(i, sce.getLine(i));
		String signType = SignUtils.getSignType(newSign);
		if (sce.getPlayer().hasPermission("silkshop.place")) {
			newSign = SignUtils.formatSign(newSign, signType);
			// Change actual sign to prettier format.
			for (int i = 0; i < 4; i++) {
				sce.setLine(i, newSign.getLine(i));
			}
		} else if (signType == "BUY" || signType == "SELL") {
			sce.setLine(0, "");
			sce.setLine(1, "§4No permission");
			sce.setLine(2, "");
			sce.setLine(3, "");
		}
	}
}