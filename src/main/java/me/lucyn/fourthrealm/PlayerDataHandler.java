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

        playerData.set("EquippedAbilities", realmPlayer.equippedAbilities);

        playerData.set("UnlockedAbilities", realmPlayer.unlockedAbilities.toArray(new String[0]));


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

            realmPlayer.equippedAbilities = (String[]) playerData.get("EquippedAbilities");

            realmPlayer.unlockedAbilities = new HashSet<>();
            String[] unlockedAbilities = (String[]) playerData.get("UnlockedAbilities");
            assert unlockedAbilities != null : "UnlockedAbilities is null";
            realmPlayer.unlockedAbilities.addAll(Arrays.asList(unlockedAbilities));





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