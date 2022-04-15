package com.github.iatog.characterdialogue.method;

import org.bukkit.ChatColor;

import com.github.iatog.characterdialogue.api.method.AbstractMethod;

public class SendMethod extends AbstractMethod {

    public SendMethod() {
        super("send", (method) -> {
            method.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', method.getArgument()));
            return true;
        });
    }
}
