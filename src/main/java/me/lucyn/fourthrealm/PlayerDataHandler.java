package me.lucyn.fourthrealm;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerDataHandler implements Listener {

    private final FourthRealmCore plugin;

    public PlayerDataHandler(FourthRealmCore plugin) {
        this.plugin = plugin;
    }



    public void savePlayerData(RealmPlayer realmPlayer) {
        Player player = realmPlayer.getPlayer();
        File playerDataFile = new File(plugin.getDataFolder(), player.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);

        playerData.set("PlayerName", player.getName());
        playerData.set("CurrentLivingWorld", realmPlayer.currentLivingWorld.getName());
        playerData.set("BlessingID", realmPlayer.blessingID);
        playerData.set("beds", realmPlayer.beds);
        // Add more data as needed

        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not save player data", e);
        }
    }

    public void loadPlayerData(Player player) {
        File playerDataFile = new File(plugin.getDataFolder(), player.getUniqueId() + ".yml");
        RealmPlayer realmPlayer = new RealmPlayer(player);

        if(playerDataFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);


            realmPlayer.currentLivingWorld = plugin.getServer().getWorld(Objects.requireNonNull(playerData.getString("CurrentLivingWorld")));
            realmPlayer.blessingID = playerData.getInt("BlessingID");
            realmPlayer.beds = (Map<World, Location>) playerData.get("beds");

        }
        else {
            realmPlayer.currentLivingWorld = player.getWorld();
            realmPlayer.blessingID = -1;
            realmPlayer.beds = new HashMap<World, Location>();

        }
        FourthRealmCore.playerData.put(player, realmPlayer);
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        savePlayerData(FourthRealmCore.playerData.get(event.getPlayer()));
        FourthRealmCore.playerData.remove(event.getPlayer());
    }

    public void saveAllPlayerData() {
        for(Player player : FourthRealmCore.playerData.keySet()) {
            savePlayerData(FourthRealmCore.playerData.get(player));
        }
    }

}