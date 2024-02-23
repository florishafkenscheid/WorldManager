package dev.blousy.worldmanager.methods;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Utils {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String decolor(String string) {
        return ChatColor.stripColor(color(string));
    }

    public static void msgPlayer(Player player, String... strings) {
        for (String string : strings) {
            player.sendMessage(color(string));
        }
    }

    public static void titlePlayer(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(color(title), color(subtitle), fadeIn, stay, fadeOut);
    }

    public static void soundPlayer(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}