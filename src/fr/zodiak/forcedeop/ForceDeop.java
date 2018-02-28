package fr.zodiak.forcedeop;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ForceDeop extends JavaPlugin {
	static List<String> ops;
	@Override
	public void onEnable() {
		saveDefaultConfig();
		ops = this.getConfig().getStringList("CanBeOp");
		getCommand("forcedeop").setExecutor(new Commands(new File("" + this.getDataFolder().getPath() + "/config.yml")));
		getLogger().info("Loaded configuration.");
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (OfflinePlayer op : Bukkit.getOperators()) {
					if (!ops.contains(op.getName())) op.setOp(false);
				}
			}
		}.runTaskTimerAsynchronously(this, 1, 1);
	}
}