package me.notenoughskill.auroralabs.database;

import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerVials {
    private String uuid;

    private int vials = 0;

    public PlayerVials(String uuid) {
        this.uuid = uuid;
    }

    public PlayerVials(String uuid, int money) {
        this.uuid = uuid;
        this.vials = money;
    }

    public int getVials() {
        return this.vials;
    }

    public String getUUID() {
        return this.uuid;
    }

    public void setVials(int value) {
        this.vials = value;
    }

    public void reduceVials(int value) {
        setVials(getVials() - value);
    }

    public void addVials(int value) {
        setVials(getVials() + value);
    }

    public String getPlayerName() {
        return Bukkit.getOfflinePlayer(UUID.fromString(this.uuid)).getName();
    }
}
