package com.goober;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class GooberAddon extends MeteorAddon {
    public static Category CATEGORY;

    @Override
    public void onInitialize() {
        CATEGORY = new Category("Goober v1");
        Modules.registerCategory(CATEGORY);
        Modules.get().add(new RejoinModule());
        Modules.get().add(new AdminDetectorModule());
    }

    @Override
    public String getPackage() {
        return "com.goober";
    }
}
