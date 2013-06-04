package main.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import main.FoE;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class cmdCENZURA implements CommandExecutor {
	public FoE	plugin;
	
	public cmdCENZURA(FoE plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender odesilatel, Command prikaz, String label, String[] args) {
		if (prikaz.getName().equalsIgnoreCase("cenzuracmd")) {
			try {
				String jmenoHrace = odesilatel.getName();
				if ((odesilatel.isOp()) || (odesilatel.hasPermission("FoE.Cenzura"))) {
					if (args.length == 0) {
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Cenzura") + " add [Slovo]  " + ChatColor.GOLD + "P��d� sprost� slovo do listu.");
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Cenzura") + " del [Slovo]  " + ChatColor.GOLD + "Odstran� sprost� slovo z listu.");
					} else if (args[0].equalsIgnoreCase("add")) {
						List<String> b = plugin.config.getStringList("Cenzura.Slova");
						if (!b.contains(args[1])) {
							b.add(args[1]);
							plugin.config.set("Cenzura.Slova", b);
							odesilatel.sendMessage(args[1] + " bylo p�id�no do cenzury");
							plugin.saveConfig(plugin.config, plugin.configFile);
						} else {
							odesilatel.sendMessage(ChatColor.RED + "Toto slovo v cenz��e ji� je!");
						}
					} else if (args[0].equalsIgnoreCase("del")) {
						List<String> b = plugin.config.getStringList("Cenzura.Slova");
						if (b.contains(args[1])) {
							b.remove(args[1]);
							plugin.config.set("Cenzura.Slova", b);
							odesilatel.sendMessage(args[1] + " bylo odstran�no z cenzury");
							plugin.saveConfig(plugin.config, plugin.configFile);
						} else {
							odesilatel.sendMessage(ChatColor.RED + "Toto slovo nen� v cenzu�e!");
						}
					} else {
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Cenzura") + " [Slovo]  " + ChatColor.GOLD + "P��d� sprost� slovo do listu.");
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Cenzura") + " [Slovo]  " + ChatColor.GOLD + "Odstran� sprost� slovo z listu.");
					}
				} else {
					odesilatel.sendMessage(plugin.nahradit(plugin.config.getString("Ostatni.KdyzNemaOpravneni"), jmenoHrace));
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