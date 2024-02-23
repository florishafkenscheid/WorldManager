package dev.blousy.worldmanager.commands;

import dev.blousy.worldmanager.WorldManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static dev.blousy.worldmanager.methods.Utils.msgPlayer;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.bukkit.Bukkit.getWorlds;

public class World extends Command {
    public World(WorldManager main) {
        super(main, "world");
    }

    @Override
    protected void execute(Player player, String[] args) {
        org.bukkit.World world;

        if (args.length == 0) {
            msgPlayer(player, "&cToo few command arguments! Type /world help for correct usages of the command.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                msgPlayer(player,
                        "&9---- &2World Manager &9----",
                        "",
                        "&8/world &7create &d<World Name>",
                        "&8/world &7tp &d<World Name> [x,y,z]",
                        "&8/world &7remove &d<World Name>",
                        "&8/world &7list",
                        ""
                );
                return;
            case "create":
                WorldCreator worldCreator = new WorldCreator(args[1]);
                worldCreator.environment(org.bukkit.World.Environment.NORMAL);
                worldCreator.type(WorldType.NORMAL);

                worldCreator.createWorld();
                msgPlayer(player, format("&aA new world called &5%s &ahas been created.", args[1]));
                return;
            case "tp":
                world = Bukkit.getWorld(valueOf(args[1]));
                if (world == null || !doesWorldExist(world)) {
                    msgPlayer(player, "&cThe world you have chosen does not exist.");
                    return;
                }

                if (args.length < 3) {
                    Location spawn = world.getSpawnLocation();
                    player.teleport(spawn);
                    msgPlayer(player, format("&aSuccessfully teleported to %s", args[1]));
                } else {
                    // parse coordinates
                    String[] coordStrings = args[2].split(",");
                    double[] coordinates = new double[3];

                    for (int i = 0; i < coordStrings.length && i < coordinates.length; i++) {
                        coordinates[i] = Double.parseDouble(coordStrings[i].trim());
                    }

                    Location destination = new Location(world, coordinates[0], coordinates[1], coordinates[2]);

                    player.teleport(destination);
                    msgPlayer(player, "&aSuccessfully teleported to %s at %s", args[1], args[2]);
                }
                return;
            case "remove":
                world = Bukkit.getWorld(valueOf(args[1]));
                if (world == null || !doesWorldExist(world)) {
                    msgPlayer(player, "&cThe world you have chosen does not exist.");
                    return;
                }

                for (Player players : world.getPlayers()) {
                    // just make sure there's a lobby world.
                    Bukkit.dispatchCommand(players, "lobby");
                }

                Bukkit.unloadWorld(world, false);

                try {
                    FileUtils.deleteDirectory(new File(world.getName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                msgPlayer(player, format("&aThe world %s has been successfully &cdeleted.", args[1]));
                return;
            case "list":
                List<org.bukkit.World> worldList = Bukkit.getWorlds();
                StringBuilder newString = new StringBuilder();
                for (org.bukkit.World worldInList : worldList) {
                    newString.append(worldInList.getName()).append(", ");
                }

                if (newString.length() > 2) {
                    newString.setLength(newString.length() - 2);
                }

                msgPlayer(player, newString.toString());
                return;
            default:
                msgPlayer(player, "&cUnknown command argument &f" + args[0] + "&c!");
                break;
        }
    }

    public boolean doesWorldExist(org.bukkit.World world) {
        return getWorlds().contains(world);
    }
}
