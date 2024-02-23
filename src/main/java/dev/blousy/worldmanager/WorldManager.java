package dev.blousy.worldmanager;

import dev.blousy.worldmanager.commands.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldManager extends JavaPlugin {
    private World world;

    @Override
    public void onEnable() {
        world = new World(this);
    }

    public World getWorld() {
        return world;
    }
}
