package dev.grafjojo.gigaproxycommon.manager;

import dev.grafjojo.gigaproxycommon.GigaProxy;

public class MaintenanceManager {

    public void setMaintenance(boolean maintenance) {
        GigaProxy.get().getConfig().maintenance.set(maintenance).save();
    }

    public void setMaintenanceReason(String message) {
        GigaProxy.get().getConfig().maintenanceMessage.set(message).save();
    }

    public String getMaintenanceReason() {
        return GigaProxy.get().getConfig().maintenanceMessage.get();
    }

    public boolean isMaintenance() {
        return GigaProxy.get().getConfig().maintenance.get();
    }
}
