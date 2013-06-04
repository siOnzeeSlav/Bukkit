package main.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import main.FoE;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class cmdBAN implements CommandExecutor {
	public FoE	plugin;
	
	public cmdBAN(FoE plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bancmd")) {
			try {
				if (sender.hasPermission("FoE.Ban")) {
					if (args.length > 0) {
						if (args.length > 1) {
							String reason = "";
							for (int i = 1; i < args.length; i++) {
								reason = (reason + (i > 1 ? " " : "") + args[i]);
							}
							plugin.banPlayer(sender.getName(), args[0], reason);
						} else {
							sender.sendMessage("Zadejte d�vod.");
						}
					} else {
						sender.sendMessage("Zadejte jm�no hr��e.");
					}
				} else {
					sender.sendMessage(plugin.nahradit(plugin.config.getString("Ostatni.KdyzNemaOpravneni"), sender.getName()));
				}
			} catch (Exception e) {
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				e.printStackTrace(printWriter);
				plugin.Error(writer.toString());
			}
		}
		return false;
	}
}