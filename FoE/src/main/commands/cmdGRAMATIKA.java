package main.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import main.FoE;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class cmdGRAMATIKA implements CommandExecutor {
	public FoE	plugin;
	
	public cmdGRAMATIKA(FoE plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender odesilatel, Command prikaz, String label, String[] args) {
		if (prikaz.getName().equalsIgnoreCase("gramatikacmd")) {
			try {
				String jmenoHrace = odesilatel.getName();
				if ((odesilatel.isOp()) || (odesilatel.hasPermission("FoE.Gramatika"))) {
					if (args.length == 0) {
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " add cele [Slovo] [Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo] [Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
						odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo] [Slovo]  " + ChatColor.GOLD + "Odstran� slovo z listu.");
					} else if (args[0].equalsIgnoreCase("add")) {
						String duvod = "";
						for (int i = 4; i < args.length; i++) {
							duvod = duvod + (i > 0 ? " " : "") + args[i];
						}
						if (args[1].equalsIgnoreCase("Vsude")) {
							List<String> b = plugin.config.getStringList("Gramatika.Vsude");
							if (args.length < 5) {
								odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo],[Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
								odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo],[Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
							} else {
								Iterator<String> it = b.iterator();
								if (it.hasNext()) {
									if (!b.contains(args[2])) {
										b.add(args[2] + "," + args[3]);
										plugin.config.set("Gramatika.Vsude", b);
										plugin.config.set("Gramatika.Duvody." + args[2], duvod);
										odesilatel.sendMessage(args[2] + "," + args[3] + " bylo p�id�no do v�ude s d�vodem" + duvod);
										plugin.saveConfig(plugin.config, plugin.configFile);
									} else {
										odesilatel.sendMessage(args[2] + " ji� je v seznamu!");
									}
								}
							}
						} else if (args[1].equalsIgnoreCase("Cele")) {
							List<String> b = plugin.config.getStringList("Gramatika.Cele");
							if (args.length < 5) {
								odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo],[Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
								odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo],[Slovo] [Od�vodn�n�]  " + ChatColor.GOLD + "P��d� slovo do listu.");
							} else {
								Iterator<String> it = b.iterator();
								if (it.hasNext()) {
									if (!b.contains(args[2])) {
										b.add(args[2] + "," + args[3]);
										plugin.config.set("Gramatika.Cele", b);
										plugin.config.set("Gramatika.Duvody." + args[2], duvod);
										odesilatel.sendMessage(args[2] + "," + args[3] + " bylo p�id�no do cele s d�vodem" + duvod);
										plugin.saveConfig(plugin.config, plugin.configFile);
									} else {
										odesilatel.sendMessage(args[2] + " ji� je v seznamu!");
									}
								}
							}
						} else {
							odesilatel.sendMessage("Dopl�te parameter! ' Cele, Vsude '");
						}
					} else if (args[0].equalsIgnoreCase("del")) {
						if (args.length == 1) {
							odesilatel.sendMessage("Dopl�te slovo!");
						} else {
							List<String> e = plugin.config.getStringList("Gramatika.Vsude");
							List<String> f = plugin.config.getStringList("Gramatika.Cele");
							Iterator<String> it = e.iterator();
							Iterator<String> it2 = f.iterator();
							if (args.length > 2) {
								if (it.hasNext()) {
									if (e.contains(args[1] + "," + args[2])) {
										e.remove(args[1] + "," + args[2]);
										plugin.config.set("Gramatika.Duvody." + args[1], null);
										odesilatel.sendMessage("Odstranil jste " + args[1] + "," + args[2] + " z V�ude");
									}
								}
								if (it2.hasNext()) {
									if (f.contains(args[1] + "," + args[2])) {
										f.remove(args[1] + "," + args[2]);
										plugin.config.set("Gramatika.Duvody." + args[1], null);
										odesilatel.sendMessage("Odstranil jste " + args[1] + "," + args[2] + " z Cel�");
									}
								}
							} else {
								odesilatel.sendMessage(plugin.config.getString("Prikazy.Gramatika") + " [Slovo] [Slovo]  " + ChatColor.GOLD + "Odstran� slovo z listu.");
							}
							plugin.config.set("Gramatika.Vsude", e);
							plugin.config.set("Gramatika.Cele", f);
							plugin.saveConfig(plugin.config, plugin.configFile);
						}
					}
				} else
					odesilatel.sendMessage(plugin.nahradit(plugin.config.getString("Ostatni.KdyzNemaOpravneni"), jmenoHrace));
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