package me.lucyn.fourthrealm;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public final class FourthRealmCore extends JavaPlugin {


    public static Map<Player, RealmPlayer> playerData;

    private PlayerDataHandler playerDataHandler;

    @Override
    public void onEnable() {
        playerData = new HashMap<>();

        this.playerDataHandler = new PlayerDataHandler(this);

        getServer().getPluginManager().registerEvents(playerDataHandler, this);

        // Plugin startup logic

        if(!getServer().getOnlinePlayers().isEmpty()) { // load player data of all online players in event of plugin reload
            for(Player player : getServer().getOnlinePlayers()) {
                playerDataHandler.loadPlayerData(player);
            }
        }


    }

    @Override
    public void onDisable() {
        playerDataHandler.saveAllPlayerData();

        // Plugin shutdown logic


    }

    public RealmPlayer getPlayerData(Player player) {
        return playerData.get(player);
    }

    public void setPlayerData(RealmPlayer player) {
        playerData.put(player.getPlayer(), player);

    }
}
