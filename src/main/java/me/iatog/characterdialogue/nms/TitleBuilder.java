package me.iatog.characterdialogue.nms;

import me.iatog.characterdialogue.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleBuilder {
	
	private final Player player;
	private String title;
	private String subtitle;
	
	private int fadeIn;
	private int stay;
	private int fadeOut;
	
	public TitleBuilder(Player player) {
		this.player = player;
	}
	
	public TitleBuilder setText(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
		return this;
	}
	
	public TitleBuilder setTimings(int fadeIn, int stay, int fadeOut) {
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
		return this;
	}
	
	public TitleBuilder send() {
		Object enumTitle;
		try {
			enumTitle = ReflectionUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
			Object titleChat = ReflectionUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\":\"" + title + "\"}");

			Object enumSubtitle = ReflectionUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Object subtitleChat = ReflectionUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\":\"" + subtitle + "\"}");

			Constructor<?> titleConstructor = ReflectionUtil.getNMSClass("PacketPlayOutTitle").getConstructor(
					ReflectionUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], ReflectionUtil.getNMSClass("IChatBaseComponent"), int.class,
					int.class, int.class);
			Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, fadeIn, stay, fadeOut);
			Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, fadeIn, stay, fadeOut);

			ReflectionUtil.sendPacket(player, titlePacket);
			ReflectionUtil.sendPacket(player, subtitlePacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
