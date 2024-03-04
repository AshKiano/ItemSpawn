package com.ashkiano.itemspawn;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ItemSpawnLoader {

    private final ItemSpawn plugin;
    private final Random random = new Random();

    public ItemSpawnLoader(ItemSpawn plugin) {
        this.plugin = plugin;
    }

    public void loadAllSpawns() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("spawns")) {
            for (String key : config.getConfigurationSection("spawns").getKeys(false)) {
                Location location = (Location) config.get("spawns." + key + ".location");
                ItemStack item = config.getItemStack("spawns." + key + ".item");
                int minDelay = config.getInt("spawns." + key + ".minDelay");
                int maxDelay = config.getInt("spawns." + key + ".maxDelay");

                new BukkitRunnable() {
                    public void run() {
                        location.getWorld().dropItemNaturally(location, item);
                    }
                }.runTaskTimer(plugin, 0, minDelay + random.nextInt(maxDelay - minDelay + 1));
            }
        }
    }
}
