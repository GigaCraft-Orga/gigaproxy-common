package dev.grafjojo.gigaproxycommon.whitelist;

import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;
import dev.grafjojo.gigaproxycommon.utils.ThreadExecutor;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;

public class WhitelistManager {

    private final WhitelistStorage WHITELIST_STORAGE = DatabaseRegistry.getStorage(WhitelistStorage.class);
    private final Set<UUID> WHITELIST_CACHE  = new ConcurrentSkipListSet<>();

    public CompletableFuture<Boolean> isWhitelisted(UUID uuid) {
        if (WHITELIST_CACHE.contains(uuid)) {
            return CompletableFuture.completedFuture(true);
        }

        return CompletableFuture.supplyAsync(() -> {
            if (WHITELIST_STORAGE.isWhitelisted(uuid)) {
                synchronized (WHITELIST_CACHE) {
                    WHITELIST_CACHE.add(uuid);
                }
                return true;
            }
            return false;
        }, ThreadExecutor.getExecutor());
    }

    public CompletableFuture<Void> whitelist(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            WHITELIST_STORAGE.whitelist(uuid);
            synchronized (WHITELIST_CACHE) {
                WHITELIST_CACHE.add(uuid);
            }
            return null;
        }, ThreadExecutor.getExecutor());
    }

    public CompletableFuture<Void> unWhitelist(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            WHITELIST_STORAGE.unWhitelist(uuid);
            synchronized (WHITELIST_CACHE) {
                WHITELIST_CACHE.remove(uuid);
            }
            return null;
        }, ThreadExecutor.getExecutor());
    }

    public Set<UUID> getCache() {
        return WHITELIST_CACHE;}
}