package me.lucyn.fourthrealm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

public class RealmPlayer {

    private final Player player;
    public World currentLivingWorld;
    public int blessingID;

    public Map<World, Location> beds;



    public RealmPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }



}
