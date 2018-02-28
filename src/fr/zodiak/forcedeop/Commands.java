package fr.zodiak.forcedeop;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Commands implements CommandExecutor {
	File cfgFile;
	
	public Commands(File cfgFile) {
		this.cfgFile = cfgFile;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] arg) {
		if (c.getName().equalsIgnoreCase("forcedeop")) {
			if (!s.hasPermission("forcedeop.reload") && !s.hasPermission("forcedeop.add") && !s.hasPermission("forcedeop.remove")) {
				s.sendMessage("§cInsufficient permissions.");
				return true;
			}
			
			if (arg.length == 0) {
				s.sendMessage("§c" + c.getUsage().replaceAll(">", ":"));
			} else {
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
				
				if (arg[0].equalsIgnoreCase("reload")) {
					if (!s.hasPermission("forcedeop.reload")) {
						s.sendMessage("§cInsufficient permissions.");
						return true;
					}
					ForceDeop.ops = cfg.getStringList("CanBeOp");
					s.sendMessage("§7Successfully reloaded ForceDeop configuration.");
					return true;
				} else if (arg.length == 2) {
					String w;
					if (arg[0].equalsIgnoreCase("add")) {
						if (!s.hasPermission("forcedeop.add")) {
							s.sendMessage("§cInsufficient permissions.");
							return true;
						}
						if (ForceDeop.ops.contains(arg[1])) {
							s.sendMessage("§cError: That player is already on the configuration.");
							return true;
						}
						ForceDeop.ops.add(arg[1]);
						if (!Bukkit.getOfflinePlayer(arg[1]).hasPlayedBefore())
							s.sendMessage("§cWarning: That player has never played on this server.");
						w = "added";
					} else if (arg[0].equalsIgnoreCase("remove")) {
						if (!s.hasPermission("forcedeop.remove")) {
							s.sendMessage("§cInsufficient permissions.");
							return true;
						}
						if (!ForceDeop.ops.contains(arg[1])) {
							s.sendMessage("§cError: That player is not on the configuration.");
							return true;
						}
						ForceDeop.ops.remove(arg[1]);
						if (!Bukkit.getOfflinePlayer(arg[1]).hasPlayedBefore())
							s.sendMessage("§cWarning: That player has never played on this server.");
						w = "removed";
					} else {
						s.sendMessage("§c" + c.getUsage().replaceAll(">", ":"));
						return true;
					}
					cfg.set("CanBeOp", ForceDeop.ops);
					try {
						cfg.save(cfgFile);
					} catch (IOException e) {
						s.sendMessage("§cError: Can not write the configuration file.");
					}
					s.sendMessage("§7Successfully " + w + " " + arg[1] + " to the configuration.");
				} else
					s.sendMessage("§c" + c.getUsage().replaceAll(">", ":"));
			}
		}

		return true;
	}

}
