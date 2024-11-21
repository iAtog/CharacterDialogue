package me.iatog.characterdialogue.libraries;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.iatog.characterdialogue.util.TextUtils.colorize;

public class ItemStackBuilder {
	private ItemStack item;
	private String name;
	private List<String> lore;
	private List<ItemFlag> flags;
	private Map<Enchantment, Integer> enchantments;

	public ItemStackBuilder(ItemStack item) {
		this.item = item;
		this.enchantments = new HashMap<>();
		this.lore = new ArrayList<>();
		this.flags = new ArrayList<>();
	}

	@SuppressWarnings("deprecation")
	public ItemStackBuilder(Material mat, int amount, short data) {
		this(new ItemStack(mat, amount, data));
	}

	public ItemStackBuilder(Material mat, int amount) {
		this(mat, amount, (short) 0);
	}

	public ItemStackBuilder(Material mat) {
		this(mat, 1, (short) 0);
	}

	public ItemStackBuilder(Material mat, short data) {
		this(mat, 1, data);
	}

	public Material getMaterial() {
		return this.item.getType();
	}

	public int getAmount() {
		return this.item.getAmount();
	}

	public String getName() {
		return this.name;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public List<ItemFlag> getItemFlags() {
		return this.flags;
	}

	public Map<Enchantment, Integer> getEnchants() {
		return this.enchantments;
	}

	public ItemStack getItemStack() {
		return item;
	}

	public ItemStackBuilder setName(String text) {
		this.name = colorize(text);
		return this;
	}

	public ItemStackBuilder setLore(List<String> lore) {
		lore.forEach((line) -> {
			this.lore.add(colorize("&f" + line));
		});
		return this;
	}

	public ItemStackBuilder setLore(String... lore) {
		this.lore.clear();
		for (String line : lore) {
			this.lore.add(colorize("&f" + line));
		}
		return this;
	}

	public ItemStackBuilder addEnchant(Enchantment enchant, int level) {
		enchantments.put(enchant, level);
		return this;
	}

	public ItemStackBuilder addFlag(ItemFlag flag) {
		this.flags.add(flag);
		return this;
	}

	public ItemStackBuilder addFlags(ItemFlag[] flags) {
		for (int i = 0; i < flags.length; i++) {
			this.flags.add(flags[i]);
		}
		
		return this;
	}
	
	public ItemStack build() {
		return buildItem();
	}

	protected ItemStack buildItem() {
		ItemStack item = this.item;
		ItemMeta meta = item.getItemMeta();
		if (name != null) {
			meta.setDisplayName(name);
		}

		if (!lore.isEmpty()) {
			meta.setLore(lore);
		}

			enchantments.forEach((enchant, level) -> {
				meta.addEnchant(enchant, level, true);
			});

		this.flags.forEach(meta::addItemFlags);
		

		item.setItemMeta(meta);
		return item;
	}
}
