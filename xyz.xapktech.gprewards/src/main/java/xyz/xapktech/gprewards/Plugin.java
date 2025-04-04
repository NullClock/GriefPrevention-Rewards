package xyz.xapktech.gprewards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xapktech.gprewards.commands.Claim;
import java.io.File;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("xyz.xapktech.gprewards");

    @Override
    public void onEnable() {
        ensureDataFolder();
        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        // Integer dailyBlocks = config.getInt("daily-claims");

        // Register the Claim command executor
        this.getCommand("redeem").setExecutor(new Claim(this, config));

        LOGGER.info("GriefProc. Rewards Loaded!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("GriefProc. Rewards Unloaded.");
    }

    private void ensureDataFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }
}