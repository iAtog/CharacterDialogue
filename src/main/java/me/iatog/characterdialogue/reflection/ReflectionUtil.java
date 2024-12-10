package me.iatog.characterdialogue.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtil {
	
	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getBukkitVersion().split("-")[0].replace(".", "_");
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static String getNMSVersion() {
		String version = Bukkit.getBukkitVersion();

		return null;
	}
}
