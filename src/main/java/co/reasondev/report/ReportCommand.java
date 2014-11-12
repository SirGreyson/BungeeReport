package co.reasondev.report;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

    private BungeeReport plugin;

    public ReportCommand(BungeeReport plugin) {
        super("report");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new ComponentBuilder(
                    "Sytax error! Try ").color(ChatColor.RED)
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

        sender.sendMessage(new ComponentBuilder("[REPORT]").color(ChatColor.RED)
                .append(" You have succesfully reported ").color(ChatColor.YELLOW)
                .append(plugin.getProxy().getPlayer(args[0]).getName()).color(ChatColor.GOLD).create());

        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("report.notify")) {
                player.sendMessage(report.create());
            }
        }
    }
}
