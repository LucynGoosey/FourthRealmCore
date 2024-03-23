package me.lucyn.fourthrealm;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public final class FourthRealmCore extends JavaPlugin {


    public static Map<Player, RealmPlayer> playerData;

    @Override
    public void onEnable() {
        playerData = new HashMap<>();

        getServer().getPluginManager().registerEvents(new PlayerDataHandler(this), this);

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        PlayerDataHandler playerDataHandler = new PlayerDataHandler(this);
        playerDataHandler.saveAllPlayerData();

        // Plugin shutdown logic
    }
}
