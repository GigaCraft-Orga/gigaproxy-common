package dev.grafjojo.gigaproxycommon.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.grafjojo.gigaproxycommon.utils.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

public record PlayCommand(ProxyServer proxy) implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) return;
        player.getCurrentServer();
        Optional<RegisteredServer> server = proxy.getServer("play");

        if (server.isEmpty()) {
            player.sendMessage(Settings.PREFIX
                    .append(Component.text("Der Server ist nicht verfügbar.", Settings.SPICY_RED)
                            .decoration(TextDecoration.BOLD, false)));
            return;
        }

        player.createConnectionRequest(server.get()).fireAndForget();
    }
}
