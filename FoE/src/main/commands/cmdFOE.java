package main.commands;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;

import main.ConfigManager;
import main.FoE;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class cmdFOE implements CommandExecutor {
	public FoE				plugin;
	public ConfigManager	cm	= new ConfigManager();
	
	public cmdFOE(FoE plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((cmd.getName().equalsIgnoreCase("FoE")) && (args.length == 0)) {
			try {
				sender.sendMessage("------ " + ChatColor.GOLD + plugin.getDescription().getVersion() + ChatColor.WHITE + " ------");
				sender.sendMessage(cm.config.getString("Prikazy.AdminChat") + " [TEXT]  " + ChatColor.GOLD + "Psan� do adminchatu.");
				sender.sendMessage(cm.config.getString("Prikazy.Manager.Ban") + " [HRAC] [DUVOD]  " + ChatColor.GOLD + "Zak�e hr��ovy p��stup na server.");
				sender.sendMessage(cm.config.getString("Prikazy.Manager.Unban") + "  [HRAC] [DUVOD]  " + ChatColor.GOLD + "Povol� hr��ovy p��stup na server.");
				sender.sendMessage(cm.config.getString("Prikazy.Manager.Kick") + " [HRAC] [DUVOD]  " + ChatColor.GOLD + "Vyhod� hr��e ze serveru.");
				sender.sendMessage(cm.config.getString("Prikazy.Cenzura") + " ADD [SLOVO]  " + ChatColor.GOLD + "P�id� slovo do cenzury.");
				sender.sendMessage(cm.config.getString("Prikazy.Cenzura") + " DEL [SLOVO]  " + ChatColor.GOLD + "Odstran� slovo z cenzury.");
				sender.sendMessage(cm.config.getString("Prikazy.Gramatika") + " ADD [CELE nebo VSUDE] [SLOVO] [SLOVO] [D�VOD]  " + ChatColor.GOLD + "P�id� slovo do gramatiky.");
				sender.sendMessage(cm.config.getString("Prikazy.Gramatika") + " DEL [CELE nebo VSUDE] [SLOVO] [SLOVO]  " + ChatColor.GOLD + "Odstran� slovo z gramatiky.");
				sender.sendMessage(cm.config.getString("Prikazy.Help") + " ZPR�VA]  " + ChatColor.GOLD + "Nap�e zpr�vu admin�m.");
				sender.sendMessage(cm.config.getString("Prikazy.VypnoutChat") + "   " + ChatColor.GOLD + "Vypne a Zapne chat.");
				sender.sendMessage(cm.config.getString("Prikazy.Nahranost") + " [HR��]  " + ChatColor.GOLD + "Zobraz� jak dlouho hr�� hraje na serveru.");
				sender.sendMessage(cm.config.getString("Prikazy.Inventar") + " [HR��]  " + ChatColor.GOLD + "Zobraz� co m� hr�� v invent��i.");
				sender.sendMessage(cm.config.getString("Prikazy.Teleport") + " [HR��]  " + ChatColor.GOLD + "Teleportuje na hr��e kter� je online.");
				sender.sendMessage(cm.config.getString("Prikazy.Oznameni") + " [TEXT]  " + ChatColor.GOLD + "Nap�e ozn�men� s nastaven�m v configu.");
				sender.sendMessage(cm.config.getString("Prikazy.Reload") + " [TEXT]  " + ChatColor.GOLD + "Znovuna�te nastaven� configu..");
				sender.sendMessage("/foe mysqlupdate  " + ChatColor.GOLD + "Aktualizuje kompletn� datab�zi MySQL FoE.");
			} catch (Exception e) {
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				e.printStackTrace(printWriter);
				plugin.Error(writer.toString());
			}
		}
		if ((cmd.getName().equalsIgnoreCase("FoE")) && args.length >= 1 && (args[0].equalsIgnoreCase("mysqlupdate")) && sender.isOp()) {
			try {
				if (plugin.mysqlPovolit) {
					if (plugin.mysql.isOpen()) {
						File a = new File("plugins/FoE/uzivatele");
						File[] soubory = a.listFiles();
						for (File f : soubory) {
							YamlConfiguration aa = YamlConfiguration.loadConfiguration(f);
							String jmeno = odstranitYML(f.getName());
							long nahranost = aa.getLong("Nahrano");
							ResultSet rs = plugin.mysql.query("SELECT `hrac` FROM `FoE_Uzivatele` WHERE `hrac` = '" + jmeno + "'");
							if (rs.next()) {
								plugin.mysql.query("UPDATE `FoE_Uzivatele` SET `nahranost` = '" + nahranost + "' WHERE `hrac` = '" + jmeno + "'");
							} else {
								plugin.mysql.query("INSERT INTO `FoE_Uzivatele` (hrac,nahranost) VALUES ('" + jmeno + "', '" + nahranost + "') ON DUPLICATE KEY UPDATE `nahranost` = '" + nahranost + "'");
							}
						}
					}
					sender.sendMessage(ChatColor.GREEN + "Datab�ze byla �sp�n� aktualizov�na.");
				} else {
					sender.sendMessage("MySQL nen� povoleno!");
				}
			} catch (Exception e) {
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				e.printStackTrace(printWriter);
				plugin.Error(writer.toString());
			}
		}
		
		if ((cmd.getName().equalsIgnoreCase("FoE")) && args.length >= 1 && (args[0].equalsIgnoreCase("reload")) && sender.isOp()) {
			try {
				cm.configFile = new File("");
				cm.config = YamlConfiguration.loadConfiguration(cm.configFile);
				
			} catch (Exception e) {
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				e.printStackTrace(printWriter);
				plugin.Error(writer.toString());
			}
		}
		
		return false;
	}
	
	public String odstranitYML(String zCeho) {
		return zCeho = zCeho.replaceAll(".yml", "");
	}
}