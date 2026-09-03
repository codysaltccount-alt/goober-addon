package com.goober;

import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_3222;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_3222;
import net.minecraft.class_746;
import net.minecraft.class_757;
import net.minecraft.class_793;
import net.minecraft.class_915;

public class RejoinModule extends Module {
    private final Setting<Integer> delay = settings.getDefaultGroup().add(new IntSetting.Builder()
        .name("delay")
        .description("Delay in seconds before reconnecting")
        .defaultValue(3)
        .min(1)
        .max(30)
        .build()
    );

    private boolean reconnecting = false;
    private long reconnectTime = 0;
    private String lastAddress = null;

    public RejoinModule() {
        super(GooberAddon.CATEGORY, "Rejoin", "Disconnect and reconnect to the server.");
    }

    @Override
    public void onActivate() {
        class_310 client = class_310.method_1551();
        if (client.field_1724 == null || client.field_1724.field_3944 == null) {
            error("Not connected to a server!");
            toggle();
            return;
        }

        lastAddress = client.field_1724.field_3944.method_44770().toString();
        reconnecting = true;
        reconnectTime = System.currentTimeMillis() + (delay.get() * 1000L);
        client.field_1724.field_3944.method_44769(new class_2680("Reconnecting..."));
    }

    @Override
    public void onTick() {
        if (!reconnecting) return;

        class_310 client = class_310.method_1551();
        if (client.field_1724 == null && System.currentTimeMillis() >= reconnectTime && lastAddress != null) {
            try {
                client.field_1687 = null;
                client.method_1572(null);
                client.method_1634(new class_793(lastAddress, lastAddress, false));
                info("Reconnected!");
            } catch (Exception e) {
                error("Failed to reconnect: " + e.getMessage());
            }
            reconnecting = false;
            toggle();
        }
    }
}
