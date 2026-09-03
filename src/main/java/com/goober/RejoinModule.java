package com.goober;

import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            error("Not connected to a server!");
            toggle();
            return;
        }

        lastAddress = client.getNetworkHandler().getConnection().getAddress().toString();
        reconnecting = true;
        reconnectTime = System.currentTimeMillis() + (delay.get() * 1000L);
        client.getNetworkHandler().getConnection().disconnect(Text.literal("Reconnecting..."));
    }

    @Override
    public void onTick() {
        if (!reconnecting) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null && System.currentTimeMillis() >= reconnectTime && lastAddress != null) {
            try {
                client.world = null;
                client.joinWorld(null);
                client.method_27227(new ServerInfo(lastAddress, lastAddress, false));
                info("Reconnected!");
            } catch (Exception e) {
                error("Failed to reconnect: " + e.getMessage());
            }
            reconnecting = false;
            toggle();
        }
    }
}
