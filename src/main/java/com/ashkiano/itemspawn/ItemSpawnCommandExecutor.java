package com.ashkiano.itemspawn;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class ItemSpawnCommandExecutor implements CommandExecutor {
    private final ItemSpawn plugin;
    private final Random random = new Random();

    public ItemSpawnCommandExecutor(ItemSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand == null || itemInHand.getType().isAir()) {
            sender.sendMessage("You must hold an item in your hand to use this command.");
            return true;
        }

        int minDelay = args.length > 0 ? Integer.parseInt(args[0]) * 20 : 100; // Default 5 seconds, converted to ticks
        int maxDelay = args.length > 1 ? Integer.parseInt(args[1]) * 20 : minDelay;

        // Generování unikátního ID pro spawn
        String spawnId = UUID.randomUUID().toString();

        // Uložení informací o spawnu do config.yml
        plugin.getConfig().set("spawns." + spawnId + ".location", player.getLocation());
        plugin.getConfig().set("spawns." + spawnId + ".item", itemInHand);
        plugin.getConfig().set("spawns." + spawnId + ".minDelay", minDelay);
        plugin.getConfig().set("spawns." + spawnId + ".maxDelay", maxDelay);
        plugin.saveConfig(); // Uložení změn v config.yml

        Location location = player.getLocation();

        new BukkitRunnable() {
            public void run() {
                player.getWorld().dropItemNaturally(location, itemInHand.clone());
            }
        }.runTaskTimer(plugin, 0, minDelay + random.nextInt(maxDelay - minDelay + 1));

        sender.sendMessage("Item spawn created at your location with ID: " + spawnId);
        return true;
    }
}