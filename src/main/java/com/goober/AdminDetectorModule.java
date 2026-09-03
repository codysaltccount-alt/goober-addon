package com.goober;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.notifications.Notification;
import meteordevelopment.meteorclient.systems.notifications.Notifications;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_310;
import net.minecraft.class_3222;
import net.minecraft.class_746;
import net.minecraft.class_2680;

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
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null || client.field_1724.field_3944 == null) return;

        for (class_3222 entry : client.field_1724.field_3944.method_45527()) {
            String name = entry.method_5477().getName();
            if (name == null || detectedPlayers.contains(name)) continue;

            String displayName = entry.method_5477().getName();

            if (isPotentialStaff(displayName)) {
                detectedPlayers.add(name);
                if (notifyOnJoin.get()) {
                    String msg = "§6[Goober] §rPotential staff detected: §c" + name;
                    Notifications.get().add(new Notification(new class_2680(msg), 5000));
                }
            }
        }
    }

    private boolean isPotentialStaff(String display) {
        String lower = display.toLowerCase();
        return lower.contains("admin") ||
               lower.contains("mod") ||
               lower.contains("staff") ||
               lower.contains("helper") ||
               lower.contains("owner") ||
               lower.contains("operator") ||
               lower.contains("developer");
    }

    @Override
    public void onDeactivate() {
        detectedPlayers.clear();
    }
}
