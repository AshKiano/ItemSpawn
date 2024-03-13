package com.ashkiano.itemspawn;

import org.bukkit.plugin.java.JavaPlugin;

//TODO přidat permisi na příkaz
public final class ItemSpawn extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig(); // Zajistí, že máme config.yml
        getCommand("spawnitem").setExecutor(new ItemSpawnCommandExecutor(this));
        new ItemSpawnLoader(this).loadAllSpawns(); // Načte a aktivuje všechny uložené spawn pointy
        this.getLogger().info("Thank you for using the ItemSpawn plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
        Metrics metrics = new Metrics(this, 21215);
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemSpawnPlugin has been disabled.");
    }
}
