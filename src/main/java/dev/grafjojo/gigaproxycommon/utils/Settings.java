package dev.grafjojo.gigaproxycommon.utils;

import dev.grafjojo.gigacraftcore.color.Coloration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Settings {
    // {"version":4,"colors":[{"hex":"#DE3B54","pos":0.8605072463768116},{"hex":"#DE67BC","pos":100}],"text":"GigaProxy","bold":true}
    // DE3B54
    // DE67BC

    public static final Component PREFIX = Coloration.gradient("GigaProxy",
            TextColor.fromHexString("#DE3B54"),
            TextColor.fromHexString("#DE67BC"))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .decorate(TextDecoration.BOLD);

    public static final Style LIME = Style.style(TextColor.color(37, 218, 0))
            .decoration(TextDecoration.BOLD, false);
    public static final Style SPICY_RED = Style.style(TextColor.color(242, 26, 0))
            .decoration(TextDecoration.BOLD, false);
}
