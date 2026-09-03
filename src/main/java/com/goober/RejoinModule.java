package com.goober;

import meteordevelopment.meteorclient.systems.modules.Module;

public class RejoinModule extends Module {
    public RejoinModule() {
        super(GooberAddon.CATEGORY, "Rejoin", "Reconnects to the server.");
    }

    @Override
    public void onActivate() {
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().getConnection().disconnect(new net.minecraft.text.Text("Reconnecting") {});
            toggle();
        }
    }
}
