package dev.blousy.worldmanager;

import dev.blousy.worldmanager.commands.SwitchWorld;
import dev.blousy.worldmanager.commands.World;
import dev.blousy.worldmanager.listeners.LobbyListener;
import dev.blousy.worldmanager.managers.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldManager extends JavaPlugin {
    private World world;
    private SwitchWorld switchWorld;

    @Override
    public void onEnable() {
        world = new World(this);
        switchWorld = new SwitchWorld(this);

        getServer().getPluginManager().registerEvents(new LobbyListener(this), this);
        DatabaseManager.initialize();
    }

    public World getWorld() {
        return world;
    }
    public SwitchWorld getSwitchWorld() {
        return switchWorld;
    }
}
