package dev.grafjojo.gigaproxycommon.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.grafjojo.gigaproxycommon.utils.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public record MessageCommand(ProxyServer proxy) implements SimpleCommand {

    public static final Map<UUID, UUID> REPLY = new HashMap<>();

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            String[] args = invocation.arguments();

            if (args.length >= 2) {

                Optional<Player> target = proxy.getPlayer(args[0]);

                if (target.isEmpty()) {
                    player.sendMessage(Settings.PREFIX.append(Component.text("Der Spieler wurde nicht gefunden!", NamedTextColor.RED)
                            .decoration(TextDecoration.BOLD, false)));
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }

                sendMessage(player, target.get(), builder.toString().trim());

                REPLY.put(player.getUniqueId(), target.get().getUniqueId());
                REPLY.put(target.get().getUniqueId(), player.getUniqueId());

            } else {
                player.sendMessage(Settings.PREFIX.append(Component.text("Verwende: ", NamedTextColor.GRAY)
                        .append(Component.text("/msg <SPIELER> <NACHRICHT>", NamedTextColor.BLUE))
                        .decoration(TextDecoration.BOLD, false)));
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (invocation.arguments().length == 0) {
            return CompletableFuture.completedFuture(proxy.getAllPlayers().stream().map(Player::getUsername).toList());
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public static void sendMessage(Player sender, Player receiver, String message) {
        // Send the message to the sender
        sender.sendMessage(Component.text("Du", NamedTextColor.DARK_AQUA)
                .append(Component.text(" -> ", NamedTextColor.DARK_GRAY))
                .append(Component.text(receiver.getUsername(), NamedTextColor.AQUA))
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(MiniMessage.miniMessage().deserialize(message)));

        // Send the message to the receiver
        receiver.sendMessage(Component.text(sender.getUsername(), NamedTextColor.AQUA)
                .append(Component.text(" -> ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Dir", NamedTextColor.DARK_AQUA)
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(MiniMessage.miniMessage().deserialize(message))));

//        receiver.playSound(Sound.sound(
//                Key.key("entity.experience_orb.pickup"),
//                Sound.Source.PLAYER,
//                1.0f, 1.0f));
    }
}
