package com.vk2gpz.tokenenchant.addon;

import com.vk2gpz.tokenenchant.api.CEHandler;
import com.vk2gpz.tokenenchant.api.ITokenEnchant;
import com.vk2gpz.vklib.mc.enchantment.EnchantUtil;
import com.vk2gpz.vklib.mc.text.TextUtil;
import com.vk2gpz.vklib.mc.version.VersionUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleLoreAddon extends EnchantLoreHandler {
    private boolean customDisplay;

    public SimpleLoreAddon(Plugin plugin, Object teconfig) {
        super(plugin, teconfig);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        load(getConfig());
    }

    @Override
    protected String getName() {
        return "SimpleLore";
    }

    void load(FileConfiguration config) {
        this.customDisplay = this.config.getBoolean("Addons." + getName() + ".custom_display", true);
    }

    @Override
    public ItemStack processLores(ItemStack is, boolean honorgreater) {
        if (is == null || !is.hasItemMeta())
            return is;

        if (!customDisplay)
            return is;


        // get a list of enchantments on the item
        Map<CEHandler, Integer> enchants = ITokenEnchant.getInstance().getEnchantments(is);

        ItemMeta im = is.getItemMeta();
        LinkedList<String> lore = new LinkedList<>((im.hasLore()) ? im.getLore() : new ArrayList<>());

        Map<String, Integer> uglylore = new HashMap<>();
        Map<String, Integer> goodlore = new HashMap<>();

        for (Map.Entry<CEHandler, Integer> entry : enchants.entrySet()) {
            if (!entry.getKey().canEnchantItem(is))
                continue;

            String displayname = entry.getKey().getAlias();
            goodlore.put(displayname, entry.getValue());
            uglylore.put(entry.getKey().getEnchantment().getKey().getKey(), entry.getValue());
        }

        if (VersionUtil.geqVersion(1, 8)) {
            if (enchants.size() < 1) {
                EnchantUtil.showEnchants(im, is.getType());
            } else {
                EnchantUtil.hideEnchants(im, is.getType());
            }
        }

        String pre = ChatColor.stripColor(ITokenEnchant.getInstance().getEnchantmentLorePrefix());
        for (int i = 0; i < lore.size(); i++) {
            String l = lore.get(i);
            String tmpl = l.substring(0, l.lastIndexOf(" "));
            if (goodlore.containsKey(tmpl)) {
                lore.set(i, tmpl + " " + goodlore.get(tmpl));
            } else if (uglylore.containsKey(tmpl)) {
                lore.set(i, tmpl + " " + uglylore.get(tmpl));
            }
        }
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }
}
