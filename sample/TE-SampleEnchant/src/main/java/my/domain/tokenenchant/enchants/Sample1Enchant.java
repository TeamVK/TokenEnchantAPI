package my.domain.tokenenchant.potions;

/*
  This file is part of TE-Sample2Enchant

  Sample2Enchant is an subclass of EnchantHandler, which provide Custom Enchantment function.
  TE will register this CE as a Bukkit's Enchantment, and it will work seamlessly on different
  org.bukkit.enchantments.Enchantment system unlike other custom enchantment plguiuns.
  Enchantment class of pre 1.13 and post 1.13 have different constructor, and many other custom enchantment plugins
  simply subclassing Enchantment class, and that wont work across those different servers!
*/

import java.util.*;

import org.bukkit.*;
import org.bukkit.FireworkEffect.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.bukkit.metadata.*;
import org.bukkit.scheduler.*;

import com.vk2gpz.tokenenchant.api.EnchantHandler;

public class Sample1Enchant extends EnchantHandler {
	/**
	 * This is used to set the velosity of the firework
	 */
	private int speed;
	
	/**
	 * This is the firework effect being used.
	 */
	private FireworkEffect effect;


	/**
	 * @param plugin TokenEnchant plugin, which wil be automatically provided by the TokenEnchant
	 */
	public Sample1Enchant(ITokenEnchant plugin) throws InvalidTokenEnchantException {
		this(plugin, null, null);
	}

	/**
	 * @param plugin TokenEnchant plugin, which wil be automatically provided by the TokenEnchant
	 * @param name name the name of the custom enchant.
	 * @param config FileConfiguration object pointing to this CE's config file.
	 */
	public Sample1Enchant(ITokenEnchant plugin, String name, FileConfiguration config) throws InvalidTokenEnchantException {
		super(plugin, name, config);
		loadConfig(); // don't forget to invoke loadConfig() !!
	}

	
	/**
	 * If your custom enchant require configuration,
	 * you can read those parameter in this method.
	 */
	public void loadConfig() {
		// just in case super class (PotionHandler) has some config to read
		// it is strongly recommended to do this!
		super.loadConfig();
		
		// read speed data.
		this.speed = this.config.getInt("Enchants.Sample.speed", 1);
		
		// building the firework effect
		FireworkEffect.Builder builder = FireworkEffect.builder();
		Boolean flicker = this.config.getBoolean("Enchants.Sample.flicker", true);
		if (flicker)
			builder = builder.withFlicker();
		
		Boolean trail = this.config.getBoolean("Enchants.Sample.trail", true);
		if (trail)
			builder = builder.withTrail();
		
		String type = this.config.getString("Enchants.Sample.effect", "STAR");
		builder = builder.with(FireworkEffect.Type.valueOf(type));
		
		Color color = getColor(this.config.getInt("Enchants.Sample.color", 1));
		builder = builder.withColor(color);
		
		this.effect = builder.build();
	}
	
	/**
	 * if your custom enchantment provides any special command, you can
	 * write your command process here.
	 * The command will be "/te <yourcommand> <arguments...>"
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
	
	/* ******** */
	/* Listener */
	/* ******** */
	
	/**
	 * This method should return the name of your custom enchantment.
	 */
	public String getName() {
		return "Sample1";
	}
	
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	
	
	/**
	 * You can write your own event listener to implement your
	 * custom enchant effect, if it should be trigered by one of events.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		final Block broken = e.getBlock();

		// You can check item in hand and all armors like
		// int level = getCELevel(p);
		// or specify the item you wish to inspect.
		int level = getCELevel(p.getItemInHand());
		
		// if ei object is null or cannot be executed based on the information in ei...
		if (!canExecute(level) || !checkCooldown(p) || !isValid(broken.getLocation())) {
			return;
		}

		launchFirework(broken.getLocation(), level, this.speed, this.effect);
	}
	
	private void launchFirework(Location l, int level, int speed, FireworkEffect effect) {
		l.add(0.5, 0.5, 0.5);
		Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta meta = fw.getFireworkMeta();
		meta.addEffect(effect);
		meta.setPower(level);
		fw.setFireworkMeta(meta);
		
		//use meta to customize the firework or add parameters to the method
		fw.setVelocity(l.getDirection().multiply(speed));
		//speed is how fast the firework flies
	}
	
	private Color getColor(int c) {
		switch (c) {
			case 1:
			default:
				return Color.AQUA;
			case 2:
				return Color.BLACK;
			case 3:
				return Color.BLUE;
			case 4:
				return Color.FUCHSIA;
			case 5:
				return Color.GRAY;
			case 6:
				return Color.GREEN;
			case 7:
				return Color.LIME;
			case 8:
				return Color.MAROON;
			case 9:
				return Color.NAVY;
			case 10:
				return Color.OLIVE;
			case 11:
				return Color.ORANGE;
			case 12:
				return Color.PURPLE;
			case 13:
				return Color.RED;
			case 14:
				return Color.SILVER;
			case 15:
				return Color.TEAL;
			case 16:
				return Color.WHITE;
			case 17:
				return Color.YELLOW;
		}
	}
}
