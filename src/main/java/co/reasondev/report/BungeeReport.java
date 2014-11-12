package co.reasondev.report;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeReport extends Plugin {

    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));
    }

    public void onDisable() {
    }
}
