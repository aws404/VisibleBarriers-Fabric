package com.aws404.visiblebarriers.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;

public class VersionUtils {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void sendMessage(String message) {
        client.inGameHud.addChatMessage(MessageType.CHAT, new LiteralText(message));
    }
}
