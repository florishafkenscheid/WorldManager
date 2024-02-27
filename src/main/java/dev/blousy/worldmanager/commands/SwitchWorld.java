package dev.blousy.worldmanager.commands;

import dev.blousy.worldmanager.WorldManager;
import dev.blousy.worldmanager.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static dev.blousy.worldmanager.methods.Utils.msgPlayer;

public class SwitchWorld extends Command {
    public SwitchWorld(WorldManager main) {
        super(main, "switchworld");
    }
    dev.blousy.worldmanager.commands.World allWorlds = new dev.blousy.worldmanager.commands.World(main);

    @Override
    protected void execute(Player player, String[] args) {
        msgPlayer(player, "works?");
        if (args.length == 0) {
            msgPlayer(player, "&cPlease specify a world to switch to.");
        }
        // check world
        if (isPlayerInWorldAlready(player, args[0])) {
            msgPlayer(player, "&cYou are already in the specified world!");
        }
        // check destination
        Location destination = DatabaseManager.getCoordinates(player.getUniqueId(), args[0]);
        // send m
        if (destination != null) {
            player.teleport(destination);
        }
        if (allWorlds.getWorldIsClosedList().contains(Bukkit.getWorld(args[0]))) {
            msgPlayer(player, "&cThis world is closed.");
        }
        else {
            World destinationWorld = Bukkit.getWorld(args[0]);
            if (destinationWorld != null) {
                Location worldSpawn = destinationWorld.getSpawnLocation();
                player.teleport(worldSpawn);
            }
            else {
                msgPlayer(player, "&cSpecified world does not exist.");
            }
        }


    }

    public boolean isPlayerInWorldAlready(Player player, String world) {
        return player.getWorld().getName().equalsIgnoreCase(world);
    }
}
