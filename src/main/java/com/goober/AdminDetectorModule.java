package com.goober;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.notifications.Notification;
import meteordevelopment.meteorclient.systems.notifications.Notifications;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class AdminDetectorModule extends Module {
    private final Setting<Boolean> notifyOnJoin = settings.getDefaultGroup().add(new BoolSetting.Builder()
        .name("notify")
        .description("Show notification when potential admin is detected")
        .defaultValue(true)
        .build()
    );

    private final Set<String> detectedPlayers = new HashSet<>();

    public AdminDetectorModule() {
        super(GooberAddon.CATEGORY, "Admin Detector", "Detects potential staff/admin players.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;

        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            String name = entry.getProfile().getName();
            if (name == null || detectedPlayers.contains(name)) continue;

            String displayName = entry.getDisplayName() != null ? entry.getDisplayName().getString() : "";

            if (isPotentialStaff(displayName, entry)) {
                detectedPlayers.add(name);
                if (notifyOnJoin.get()) {
                    String msg = "§6[Goober] §rPotential staff detected: §c" + name;
                    Notifications.get().add(new Notification(Text.literal(msg), 5000));
                }
            }
        }
    }

    private boolean isPotentialStaff(String display, PlayerListEntry entry) {
        String lower = display.toLowerCase();
        return lower.contains("admin") ||
               lower.contains("mod") ||
               lower.contains("staff") ||
               lower.contains("helper") ||
               lower.contains("owner") ||
               lower.contains("operator") ||
               lower.contains("developer") ||
               (entry.getGameMode() != null && entry.getGameMode().isCreative());
    }

    @Override
    public void onDeactivate() {
        detectedPlayers.clear();
    }
}
