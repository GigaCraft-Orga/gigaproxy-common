package dev.grafjojo.gigaproxycommon.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.grafjojo.gigaproxycommon.utils.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

public record ReplyCommand(ProxyServer proxy) implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            String[] args = invocation.arguments();

            if (!MessageCommand.REPLY.containsKey(player.getUniqueId())) {
                player.sendMessage(Settings.PREFIX.append(Component.text("Du hast keine Nachrichten zum Antworten!", NamedTextColor.RED)
                        .decoration(TextDecoration.BOLD, false)));
                return;
            }

            Optional<Player> target = proxy.getPlayer(MessageCommand.REPLY.get(player.getUniqueId()));

            if (target.isEmpty()) {
                player.sendMessage(Settings.PREFIX.append(Component.text("Der Spieler ist nicht mehr online!", NamedTextColor.RED)
                        .decoration(TextDecoration.BOLD, false)));
                return;
            }

            if (args.length >= 1) {

                StringBuilder builder = new StringBuilder();
                for (String arg : args) {
                    builder.append(arg).append(" ");
                }

                MessageCommand.sendMessage(player, target.get(), builder.toString().trim());

            } else {
                player.sendMessage(Settings.PREFIX.append(Component.text("Verwende: ", NamedTextColor.GRAY)
                        .append(Component.text("/r <NACHRICHT>", NamedTextColor.BLUE))
                        .decoration(TextDecoration.BOLD, false)));
            }
        }
    }
}
