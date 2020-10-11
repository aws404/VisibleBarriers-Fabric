package com.aws404.visiblebarriers.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class VersionUtils {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void sendMessage(Text message) {
        client.player.sendMessage(message);
    }
}
