package me.lucyn.fourthrealm;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
        playerData.set("PurgatoryRespawn", realmPlayer.purgatoryRespawn);

        playerData.set("XP", realmPlayer.XP);
        playerData.set("Level", realmPlayer.level);

        playerData.set("EquippedAbilities.0", realmPlayer.equippedAbilities[0]);
        playerData.set("EquippedAbilities.1", realmPlayer.equippedAbilities[1]);
        playerData.set("EquippedAbilities.2", realmPlayer.equippedAbilities[2]);

        playerData.set("UnlockedAbilities", realmPlayer.unlockedAbilities);


        for(World world : realmPlayer.beds.keySet()) {
            playerData.set(world.getName(), realmPlayer.beds.get(world));
        }
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
            realmPlayer.beds = new HashMap<>();
            realmPlayer.purgatoryRespawn = playerData.getBoolean("PurgatoryRespawn");

            realmPlayer.XP = playerData.getLong("XP");
            realmPlayer.level = playerData.getInt("Level");

            realmPlayer.equippedAbilities[0] = playerData.getString("EquippedAbilities.0");
            realmPlayer.equippedAbilities[1] = playerData.getString("EquippedAbilities.1");
            realmPlayer.equippedAbilities[2] = playerData.getString("EquippedAbilities.2");

            realmPlayer.unlockedAbilities = new HashSet<>();
            List<String> unlockedAbilities = playerData.getStringList("UnlockedAbilities");
            realmPlayer.unlockedAbilities.addAll(unlockedAbilities);





            List<World> worlds = new ArrayList<>();

            List<World> allWorlds = plugin.getServer().getWorlds();


            for(World world : allWorlds) {
                if(world.getEnvironment().equals(World.Environment.NORMAL)) {
                    worlds.add(world);
                }
            }

            for(World world : worlds) {

                if(playerData.contains(world.getName())) {
                    realmPlayer.beds.put(world, playerData.getLocation(world.getName()));
                }
            }

        }
        else {
            realmPlayer.currentLivingWorld = player.getWorld();
            realmPlayer.beds = new HashMap<>();

        }
        FourthRealmCore.playerData.put(player, realmPlayer);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
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