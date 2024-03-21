package me.lucyn.fourthrealm;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class RealmPlayer {

    private final Player player;
    public World currentLivingWorld;
    public int blessingID;

    public RealmPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
