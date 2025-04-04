package xyz.xapktech.gprewards.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.entity.Player;
import java.time.LocalDate;
import java.time.ZoneId;

public class Claim implements CommandExecutor {
    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public Claim(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        Long lastUsedts = this.config.getLong("data." + player.getName());
        LocalDate currentDate = LocalDate.now();
        Integer dailyClaims = this.config.getInt("daily-claims");
        long currentTimestamp = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (lastUsedts == 0 || currentTimestamp > lastUsedts) {
            if (dailyClaims > 0) {
                giveClaimBlocks(player, dailyClaims);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou've received &1&l" + dailyClaims + "&r&a Grief Protection claim blocks!"));
                this.config.set("data." + player.getName(), currentTimestamp);
                this.plugin.saveConfig();
            } else {
                player.sendMessage("Daily claims are not available at the moment.");
            }
        } else {
            player.sendMessage("You have already claimed your blocks for today.");
        }

        return true;
    }

    public void giveClaimBlocks(Player player, int amount) {
        GriefPrevention instance = GriefPrevention.instance;
        PlayerData playerData = instance.dataStore.getPlayerData(player.getUniqueId());
        playerData.setAccruedClaimBlocks(playerData.getAccruedClaimBlocks() + amount);
    }
}