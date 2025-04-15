package me.lucyn.fourthrealm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RealmPlayer {

    private final Player player;

    public World currentLivingWorld;
    public boolean purgatoryRespawn; // used for persisting respawns on disconnect

    public long XP;
    public int level;

    public String[] equippedAbilities = new String[3];
    public Set<String> unlockedAbilities = new HashSet<>();

    public Map<World, Location> beds;



    public RealmPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public RealmPlayer setPurgatoryRespawn(boolean purgatoryRespawn) {
        this.purgatoryRespawn = purgatoryRespawn;
        return this;
    }

}
