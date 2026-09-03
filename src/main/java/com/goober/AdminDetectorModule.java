package com.goober;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;

public class AdminDetectorModule extends Module {
    public AdminDetectorModule() {
        super(GooberAddon.CATEGORY, "Admin Detector", "Detects staff.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.getNetworkHandler() == null) return;
        
        for (var entry : mc.getNetworkHandler().getPlayerList()) {
            String display = entry.getDisplayName() != null ? entry.getDisplayName().getString() : "";
            if (display.toLowerCase().contains("admin") || display.toLowerCase().contains("mod")) {
                ChatUtils.info("Staff: " + entry.getProfile().getName());
            }
        }
    }
}
