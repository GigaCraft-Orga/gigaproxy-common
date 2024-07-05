package dev.grafjojo.gigaproxycommon.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.grafjojo.gigaproxycommon.GigaProxy;
import dev.grafjojo.gigaproxycommon.manager.MaintenanceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PingListener {

    private final MaintenanceManager MAINTENANCE = GigaProxy.get().getMaintenanceManager();

    @Subscribe
    public void handlePing(ProxyPingEvent event) {
        if (MAINTENANCE.isMaintenance()) {
            event.setPing(ServerPing.builder()
                            .description(Component.text("GigaCraft is currently unavailable due to Maintenance.", NamedTextColor.RED))
                    .build());
            return;
        }

        event.setPing(ServerPing.builder()
                .description(Component.text("GigaCraft 5!?!?!?", NamedTextColor.GREEN))
                .build());


    }
}
