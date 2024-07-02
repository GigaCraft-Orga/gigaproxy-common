package dev.grafjojo.gigaproxycommon.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.grafjojo.gigaproxycommon.GigaProxy;
import dev.grafjojo.gigaproxycommon.manager.MaintenanceManager;
import dev.grafjojo.gigaproxycommon.utils.Settings;
import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;


public record MaintenanceCommand(ProxyServer proxy) implements SimpleCommand {

    private static final MaintenanceManager MAINTENANCE = GigaProxy.get().getMaintenanceManager();

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission("gigaproxy.maintenance")) {
            source.sendMessage(Settings.PREFIX.append(Component.text("Du hast keine Berechtigung, diesen Befehl auszuführen.",
                    NamedTextColor.RED).decoration(TextDecoration.BOLD, false)));
            return;
        }

        if (args.length >= 1) {
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }
            MAINTENANCE.setMaintenanceReason(message.toString().trim());
        } else {
            MAINTENANCE.setMaintenanceReason(null);
        }

        if (MAINTENANCE.isMaintenance()) {
            MAINTENANCE.setMaintenance(false);
        } else {
            MAINTENANCE.setMaintenance(true);
            proxy.getAllPlayers().forEach(player -> {
                if (player.hasPermission("gigaproxy.maintenance.bypass")) return;

                player.disconnect(MiniMessage.miniMessage().deserialize(MAINTENANCE.getMaintenanceReason()));

            });
        }

        Component maintenanceState = MAINTENANCE.isMaintenance() ?
                Component.text("aktiviert", Settings.LIME) :
                Component.text("deaktiviert", Settings.SPICY_RED);

        source.sendMessage(Settings.PREFIX.append(Component.text("Der Wartungsmodus wurde ",
                        Style.style(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
                .append(maintenanceState).append(Component.text("."))));
    }
}
