package dev.grafjojo.gigaproxycommon.registration;

import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;
import dev.grafjojo.gigaproxycommon.utils.ThreadExecutor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RegistrationManager {

    private static final RegistrationStorage REGISTRATION = DatabaseRegistry.getStorage(RegistrationStorage.class);

    public CompletableFuture<Boolean> isProcessed(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> REGISTRATION.isProcessed(uuid), ThreadExecutor.getExecutor());
    }
}