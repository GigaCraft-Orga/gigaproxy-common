package dev.grafjojo.gigaproxycommon;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.maxhenkel.configbuilder.ConfigBuilder;
import dev.grafjojo.gigacraftcore.database.Database;
import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;
import dev.grafjojo.gigacraftcore.database.postgres.PostgresDatabase;
import dev.grafjojo.gigaproxycommon.command.*;
import dev.grafjojo.gigaproxycommon.listener.LoginListener;
import dev.grafjojo.gigaproxycommon.manager.MaintenanceManager;
import dev.grafjojo.gigaproxycommon.registration.RegistrationManager;
import dev.grafjojo.gigaproxycommon.registration.RegistrationStorage;
import dev.grafjojo.gigaproxycommon.utils.ThreadExecutor;
import dev.grafjojo.gigaproxycommon.whitelist.WhitelistManager;
import dev.grafjojo.gigaproxycommon.whitelist.WhitelistStorage;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "gigaproxy-common",
        name = "gigaproxy-common"
)
public class GigaProxy {

    private static GigaProxy instance;
    private Database database;
    private final Logger logger;
    private final ProxyServer server;
    private final GeneralConfig config;
    private final MaintenanceManager maintenanceManager;
    private final WhitelistManager whitelistManager;
    private final RegistrationManager registrationManager;

    @Inject
    public GigaProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.logger = logger;
        this.server = server;
        this.config = ConfigBuilder.builder(GeneralConfig::new)
                .path(dataDirectory.resolve("config.yml"))
                .keepOrder(true)
                .saveAfterBuild(true)
                .build();
        initializeDatabase();

        this.whitelistManager = new WhitelistManager();
        this.maintenanceManager = new MaintenanceManager();
        this.registrationManager = new RegistrationManager();

        logger.info("GigaProxyCommon has been enabled!");
    }

    private void initializeDatabase() {
        database = new PostgresDatabase(getConfig().getPostgresCredentials());
        DatabaseRegistry.registerStorage(new WhitelistStorage((PostgresDatabase) database));
        DatabaseRegistry.registerStorage(new RegistrationStorage((PostgresDatabase) database));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager manager = server.getCommandManager();

        manager.register(manager.metaBuilder("eurocup").plugin(this).build(), new EuroCupCommand(server));
        manager.register(manager.metaBuilder("play").plugin(this).build(), new PlayCommand(server));
        manager.register(manager.metaBuilder("maintenance").plugin(this).build(), new MaintenanceCommand(server));
        manager.register(manager.metaBuilder("message")
                .aliases("msg", "w", "tell", "whisper")
                .plugin(this).build(), new MessageCommand(server));
        manager.register(manager.metaBuilder("reply").aliases("r").plugin(this).build(), new ReplyCommand(server));


        server.getEventManager().register(this, new LoginListener());
    }

    @Subscribe
    public void handleShutdown(ProxyShutdownEvent event) {
        ThreadExecutor.shutdown();
        database.disconnect();
    }


    public static GigaProxy get() {
        return instance;
    }

    public GeneralConfig getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }
}
