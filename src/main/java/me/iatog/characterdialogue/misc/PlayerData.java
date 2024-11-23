package me.iatog.characterdialogue.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final List<String> readedDialogs;

    private boolean removeEffect;
    private double lastSpeed;

    public PlayerData(UUID uuid, List<String> readedDialogs, boolean removeEffect, double lastSpeed) {
        this.uuid = uuid;
        this.readedDialogs = new ArrayList<>();
        this.removeEffect = removeEffect;
        this.lastSpeed = lastSpeed;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean getRemoveEffect() {
        return removeEffect;
    }

    public double getLastSpeed() {
        return lastSpeed;
    }

    public List<String> getReadedDialogs() {
        return readedDialogs;
    }

    public void setRemoveEffect(boolean remove) {
        this.removeEffect = remove;
    }

    public void setLastSpeed(double speed) {
        this.lastSpeed = speed;
    }

    public void addDialog(String dialog) {
        this.readedDialogs.add(dialog);
    }
}
