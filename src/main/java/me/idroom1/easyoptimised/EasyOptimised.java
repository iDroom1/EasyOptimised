package me.idroom1.easyoptimised;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyOptimised extends JavaPlugin implements Listener {

    private boolean delayChunkLoad;

    @Override
    public void onEnable() {
        // Save default config if not already created
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        delayChunkLoad = config.getBoolean("delay-chunk-load");

        // Register the plugin to listen to events
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("EasyOptimised has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyOptimised has been disabled.");
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (delayChunkLoad) {
            Chunk chunk = event.getChunk();
            // Unload the chunk immediately after loading to simulate delayed loading
            Bukkit.getScheduler().runTaskLater(this, () -> {
                if (chunk.isLoaded()) {
                    chunk.unload();
                }
            }, 20L); // 20L represents a delay of 1 second (20 ticks)
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("killall")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("entities")) {
                for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                    if (entity.getType() != EntityType.PLAYER) {
                        entity.remove();
                    }
                }
                sender.sendMessage("All entities have been killed.");
                return true;
            }
        }
        return false;
    }
}
