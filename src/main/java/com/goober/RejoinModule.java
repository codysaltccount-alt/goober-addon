package com.goober;

import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

public class RejoinModule extends Module {
    private final Setting<Integer> delay = settings.getDefaultGroup().add(new IntSetting.Builder()
        .name("delay")
        .defaultValue(3)
        .min(1)
        .max(30)
        .build()
    );

    private boolean reconnecting = false;
    private long time = 0;

    public RejoinModule() {
        super(GooberAddon.CATEGORY, "Rejoin", "Reconnects to the server.");
    }

    @Override
    public void onActivate() {
        if (mc.getNetworkHandler() == null) {
            error("Not connected to a server!");
            toggle();
            return;
        }

        reconnecting = true;
        time = System.currentTimeMillis() + delay.get() * 1000L;
        mc.getNetworkHandler().getConnection().disconnect(new net.minecraft.text.Text("Reconnecting...") {});
        ChatUtils.info("Disconnected, reconnecting in " + delay.get() + "s");
    }

    @Override
    public void onTick() {
        if (!reconnecting) return;
        
        if (mc.getNetworkHandler() == null && System.currentTimeMillis() >= time) {
            ChatUtils.info("Reconnecting...");
            mc.world = null;
            mc.joinWorld(null);
            reconnecting = false;
            toggle();
        }
    }
}
