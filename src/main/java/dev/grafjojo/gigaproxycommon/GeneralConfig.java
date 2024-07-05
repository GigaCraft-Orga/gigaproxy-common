package dev.grafjojo.gigaproxycommon;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;
import dev.grafjojo.gigacraftcore.database.DatabaseCredentials;

public class GeneralConfig {

    public final ConfigEntry<Boolean> maintenance;
    public final ConfigEntry<String> maintenanceMessage;

    private final ConfigEntry<String> postgresHost;
    private final ConfigEntry<Integer> port;
    private final ConfigEntry<String> database;
    private final ConfigEntry<String> user;
    private final ConfigEntry<String> password;

    public GeneralConfig(ConfigBuilder builder) {
        maintenance = builder.booleanEntry("maintenance", true);
        maintenanceMessage = builder.stringEntry("maintenanceMessage", "<red>Server is currently unavailable due to maintenance!</red> " +
                "\n Please checkout the GigaCraft Discord for more information");

        postgresHost = builder.stringEntry("postgresHost", "localhost", "Postgres credentials");
        port = builder.integerEntry("port", 5432);
        database = builder.stringEntry("database", "postgres");
        user = builder.stringEntry("user", "postgres");
        password = builder.stringEntry("password", "123");
    }

    public DatabaseCredentials getPostgresCredentials() {
        return new DatabaseCredentials(postgresHost.get(), port.get(), database.get(), user.get(), password.get());
    }
}
