package dev.grafjojo.gigaproxycommon.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.grafjojo.gigaproxycommon.GigaProxy;
import dev.grafjojo.gigaproxycommon.manager.MaintenanceManager;
import dev.grafjojo.gigaproxycommon.registration.RegistrationManager;
import dev.grafjojo.gigaproxycommon.whitelist.WhitelistManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LoginListener {

    private static final MaintenanceManager MAINTENANCE = GigaProxy.get().getMaintenanceManager();
    private static final WhitelistManager WHITELIST = GigaProxy.get().getWhitelistManager();
    private static final RegistrationManager REGISTRATION = GigaProxy.get().getRegistrationManager();

    @Subscribe(order = PostOrder.FIRST)
    public void handleLogin(PostLoginEvent event) {
        Player player = event.getPlayer();

        try {

            // Check if the proxy is in maintenance mode and the player has no bypass permission
            if (MAINTENANCE.isMaintenance() && !player.hasPermission("gigaproxy.maintenance.bypass")) {
                player.disconnect(MiniMessage.miniMessage().deserialize(MAINTENANCE.getMaintenanceReason()));
                return;
            }

            // Check if the player is whitelisted if yes, returning
            if (WHITELIST.isWhitelisted(player.getUniqueId()).join()) return;

            // check if the player is registered if yes, whitelisting!
            if (REGISTRATION.isProcessed(player.getUniqueId()).join()) {
                WHITELIST.whitelist(player.getUniqueId());
                return;
            }

            player.disconnect(Component.text("Du stehst nicht auf der Whitelist", NamedTextColor.RED));
        } catch (Exception e) {
            player.disconnect(Component.text("Ein Fehler ist aufgetreten", NamedTextColor.RED));
            throw new RuntimeException(e);
        }

    }
}
