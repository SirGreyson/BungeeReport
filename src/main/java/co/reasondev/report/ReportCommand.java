package co.reasondev.report;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;

public class ReportCommand extends Command implements TabExecutor {

    private BungeeReport plugin;
    private Map<UUID, Long> cooldownMap = new HashMap<>();

    public ReportCommand(BungeeReport plugin) {
        super("report");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new ComponentBuilder(
                    "Syntax error! Try ").color(ChatColor.RED)
                    .append("/report <player> <reason>").color(ChatColor.GOLD).create());
            return;
        }
        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(new ComponentBuilder(
                    "Error! You cannot report yourself!").color(ChatColor.RED).create());
            return;
        }
        if (plugin.getProxy().getPlayer(args[0]) == null) {
            sender.sendMessage(new ComponentBuilder(
                    "Error! There is no online Player with that name!").color(ChatColor.RED).create());
            return;
        }
        if (cooldownMap.containsKey(((ProxiedPlayer) sender).getUniqueId())) {
            if (System.currentTimeMillis() - cooldownMap.get(((ProxiedPlayer) sender).getUniqueId()) < 60000) {
                sender.sendMessage(new ComponentBuilder(
                        "Error! You must wait before you can use this command again!").color(ChatColor.RED).create());
                return;
            }
        }
        //Create Report Header
        ComponentBuilder report = new ComponentBuilder("[REPORT]").color(ChatColor.RED)
                .append(" " + sender.getName() + " ").color(ChatColor.YELLOW)
                .append("has reported a Player on").color(ChatColor.GOLD)
                .append(" " + ((ProxiedPlayer) sender).getServer().getInfo().getName() + " ").color(ChatColor.YELLOW);
        //Append Player Information
        report.append("\nPlayer: ").color(ChatColor.GOLD)
                .append(plugin.getProxy().getPlayer(args[0]).getName()).color(ChatColor.YELLOW);
        //Append Report Details
        report.append("\nDetails:").color(ChatColor.GOLD);
        for (int i = 1; i < args.length; i++) {
            report.append(" " + args[i]).color(ChatColor.YELLOW);
        }
        BaseComponent[] message = report.create();

        sender.sendMessage(new ComponentBuilder("[REPORT]").color(ChatColor.RED)
                .append(" You have successfully reported ").color(ChatColor.YELLOW)
                .append(plugin.getProxy().getPlayer(args[0]).getName()).color(ChatColor.GOLD).create());
        cooldownMap.put(((ProxiedPlayer) sender).getUniqueId(), System.currentTimeMillis());

        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("report.notify")) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return ImmutableSet.of();
        }
        Set<String> matches = new HashSet<>();

        String search = args[0].toLowerCase();
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.getName().toLowerCase().startsWith(search)) {
                matches.add(player.getName());
            }
        }
        return matches;
    }
}
