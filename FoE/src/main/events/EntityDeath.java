package main.events;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import main.FoE;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {
	public FoE	p;
	
	public EntityDeath(FoE plugin) {
		this.p = plugin;
	}
	
	public void addMob(String name) {
		p.uzivatel(name);
		p.uziv.set("ZabitoMobu", p.uziv.getInt("ZabitoMobu") + 1);
		p.saveConfig(p.uziv, p.uzivFile);
		p.aktualizovatGUI(name);
	}
	
	public void addKill(String name) {
		p.uzivatel(name);
		p.uziv.set("ZabitoHracu", p.uziv.getInt("ZabitoHracu") + 1);
		p.aktualizovatGUI(name);
		p.saveConfig(p.uziv, p.uzivFile);
		p.aktualizovatGUI(name);
	}
	
	public void addDeath(String name) {
		p.uzivatel(name);
		p.uziv.set("PocetSmrti", p.uziv.getInt("PocetSmrti") + 1);
		p.saveConfig(p.uziv, p.uzivFile);
		p.aktualizovatGUI(name);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		try {
			Entity entity = event.getEntity();
			if (p.umrtiZpravyPovolit) {
				if (entity != null) { // Entity neni null
					if (entity instanceof Monster) { // Je monster
						Monster monster = (Monster) entity; // Ziskam monstra
						if (monster.getKiller() instanceof Player) { // Ten kdo zabil monstra jestli je hrac
							String killerName = monster.getKiller().getName(); // Ziskat toho kdo zabil monstra jmeno
							addMob(killerName); // Pridat jako mobkill
						}
					} else if (entity instanceof Player) { // Je hrac
						Player player = (Player) entity; // Ziskam hrace
						String playerName = player.getName(); // Ziskam hracovo jmeno
						EntityDamageEvent EDevent = player.getLastDamageCause(); // Vezmu entitu co mu dala posledni poskozeni
						Entity killer = null; // Entity = null
						if (EDevent instanceof EntityDamageByEntityEvent) {
							killer = ((EntityDamageByEntityEvent) EDevent).getDamager(); // Ziskam killera
						}
						if (killer != null) { // Jestli nekdo zabil hrace
							if (killer.getType() != null) { // Typ kterej zabil hrace jestli existuje
								if (!(killer instanceof Animals) && (!(killer instanceof Player))) {
									String targetName = killer.getType().getName(); // Mob kterej zabil hrace
									addDeath(targetName); // Pridam umrti hracovy
									poslatZpravuMonster(targetName, playerName); // Napisu kterej mob zabil hrace.
								} else if (killer instanceof Player) { // Jestli zabijak je hrac
									Player target = (Player) killer; // Pretypuju zabijaka na hrace
									if (target != null) { // Jestli zabijak neni null
										String targetName = target.getName(); // Vezmu zabijakovo jmeno
										addKill(targetName); // Pridam zabijakovy Kill
										addDeath(playerName); // Pridam mrtvemu umrti
										poslatZpravu(getItemName(target), playerName, targetName); // Napisu zpravu ze zabijak zabil hrace s predmetem
									}
								}
							}
						} else { // Killer je null takze se zabil sam
							poslatZpravuSebevrazda(EDevent.getCause().toString(), playerName); // Napisu zpravu jak se zabil
						}
					}
				}
			}
		} catch (Exception e) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			p.Error(writer.toString());
		}
	}
	
	public void poslatZpravuSebevrazda(String cause, String playerName) {
		if (!p.umrtiZpravy.contains("Sebevrazda." + cause)) {
			p.umrtiZpravy.set("Sebevrazda." + cause, "&4{JMENO} &8byl zabit &4" + cause + "&8.");
			p.saveConfig(p.umrtiZpravy, p.umrtiZpravyFile);
		}
		Bukkit.broadcastMessage(replaceColorsAndPlayer(p.umrtiZpravy.getString("Sebevrazda." + cause), playerName));
	}
	
	public void poslatZpravuMonster(String targetName, String playerName) {
		if (!p.umrtiZpravy.contains("Monsters." + targetName)) {
			p.umrtiZpravy.set("Monsters." + targetName, "&4{JMENO} &8byl zabit &4{MOB}&8.");
			p.saveConfig(p.umrtiZpravy, p.umrtiZpravyFile);
		}
		Bukkit.broadcastMessage(replace(p.umrtiZpravy.getString("Monsters." + targetName), playerName, targetName));
	}
	
	public void poslatZpravu(String itemName, String playerName, String targetName) {
		if (p.debug)
			Bukkit.broadcastMessage(itemName + " / " + playerName + " / " + targetName);
		if (!p.umrtiZpravy.contains(itemName)) {
			p.umrtiZpravy.set(itemName, "&4{TARGET} &8byl zabit &4{JMENO} &8s p�edm�tem &4{ITEM}&8.");
			p.saveConfig(p.umrtiZpravy, p.umrtiZpravyFile);
		}
		Bukkit.broadcastMessage(replacePredmet(p.umrtiZpravy.getString(itemName), playerName, targetName, itemName));
	}
	
	public String replaceColorsAndPlayer(String message, String playerName) {
		if (message.matches(".*\\{JMENO}.*"))
			message = message.replaceAll("\\{JMENO}", playerName);
		message = message.replaceAll("(&([a-fk-or0-9]))", "�$2");
		return message;
	}
	
	public String getItemName(Player target) {
		return target.getItemInHand().getType().toString();
	}
	
	public String replacePredmet(String message, String playerName, String targetName, String itemName) {
		if (message.matches(".*\\{JMENO}.*"))
			message = message.replaceAll("\\{JMENO}", playerName);
		if (message.matches(".*\\{TARGET}.*"))
			message = message.replaceAll("\\{TARGET}", targetName);
		if (message.matches(".*\\{ITEM}.*"))
			message = message.replaceAll("\\{ITEM}", itemName);
		
		message = message.replaceAll("(&([a-fk-or0-9]))", "�$2");
		return message;
	}
	
	public String replace(String message, String playerName, String targetName) {
		if (message.matches(".*\\{JMENO}.*"))
			message = message.replaceAll("\\{JMENO}", playerName);
		if (message.matches(".*\\{MOB}.*"))
			message = message.replaceAll("\\{MOB}", targetName);
		message = message.replaceAll("(&([a-fk-or0-9]))", "�$2");
		return message;
		
	}
}
