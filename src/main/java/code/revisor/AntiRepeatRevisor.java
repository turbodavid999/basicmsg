package code.revisor;

import code.BasicMsg;
import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AntiRepeatRevisor {

    private Manager manager;

    private static BasicMsg plugin;
    private static CacheManager cache;
    private static PlayerMessage playersender;
    private static Configuration utils;

    public AntiRepeatRevisor(Manager manager){
        this.manager = manager;

        plugin = manager.getPlugin();
        utils = manager.getFiles().getBasicUtils();
        cache = manager.getCache();
        playersender = manager.getPlayerMethods().getSender();

    }

    public static boolean isSpamming(UUID uuid){

        if (!(utils.getBoolean("utils.chat.security.anti-repeat.enabled"))){
            return false;
        }

        UserCache playerCooldown = cache.getPlayerUUID().get(uuid);

        if (playerCooldown.isCooldownMode()) {
            Player player = Bukkit.getPlayer(uuid);
            playersender.sendMessage(player, utils.getString("utils.chat.security.anti-repeat.message")
                    .replace("%seconds%", utils.getString("utils.chat.security.anti-repeat.seconds")));
            return true;
        }

        playerCooldown.setCooldownMode(true);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                playerCooldown.setCooldownMode(false);
            }
        },20L * utils.getInt("utils.chat.security.anti-repeat.seconds"));

        return false;
    }

}

