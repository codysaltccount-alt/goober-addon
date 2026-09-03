package com.goober;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class AdminDetectorModule extends Module {
    private final Setting<Boolean> notify = settings.getDefaultGroup().add(new BoolSetting.Builder()
        .name("notify")
        .defaultValue(true)
        .build()
    );

    private final Set<String> detected = new HashSet<>();

    public AdminDetectorModule() {
        super(GooberAddon.CATEGORY, "Admin Detector", "Detects potential staff/admin players.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.getNetworkHandler() == null) return;

        for (var entry : mc.getNetworkHandler().getPlayerList()) {
            String name = entry.getProfile().getName();
            if (name == null || detected.contains(name)) continue;
            
            String display = entry.getDisplayName() != null ? entry.getDisplayName().getString() : "";
            
            if (display.toLowerCase().contains("admin") || 
                display.toLowerCase().contains("mod") || 
                display.toLowerCase().contains("staff") || 
                display.toLowerCase().contains("helper") ||
                display.toLowerCase().contains("owner") ||
                display.toLowerCase().contains("operator")) {
                
                detected.add(name);
                if (notify.get()) {
                    ChatUtils.info("Potential staff detected: " + name);
                }
            }
        }
    }

    @Override
    public void onDeactivate() {
        detected.clear();
    }
}
