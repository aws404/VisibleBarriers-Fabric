package com.aws404.visiblebarriers.devices;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.List;

public class WynnDeviceGUI extends Screen {

    private final List<String> FISH = Arrays.asList("Gudgeon", "Trout", "Salmon", "Carp", "Icefish", "Piranha", "Koi", "GyliaFish", "Bass", "MoltenEel", "Starfish", "DernicFish");
    private final List<String> ORES = Arrays.asList("Copper", "Granite", "Gold", "Sandstone", "Iron", "Silver", "Cobalt", "Kanderstone", "Diamond", "Molten", "Voidstone", "DernicStone");
    private final List<String> LOGS = Arrays.asList("Oak", "Birch", "Willow", "Acacia", "Spruce", "Jungle", "Dark", "Light", "Pine", "Avo", "Sky", "Dernic");
    private final List<String> CROPS = Arrays.asList("Wheat", "Barley", "Oats", "Malt", "Hops", "Rye", "Millet", "DecayRoots", "Rice", "Sorghum", "Hemp", "Dernic");
    private final List<String> STATIONS = Arrays.asList("Jeweling", "Armoring", "Woodworking", "Weaponsmithing", "Tailoring", "Alchemism", "Scribing", "Cooking");

    public WynnDeviceGUI() {
        super(new TranslatableText("menu.device.title"));
    }

    protected void init() {
        for (int i = 0; i < FISH.size(); i++) {
            String name = FISH.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 210, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                getPlacer("Fish", name);
            }));
        }

        for (int i = 0; i < ORES.size(); i++) {
            String name = ORES.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 125, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                getPlacer("Ore", name);
            }));
        }

        for (int i = 0; i < LOGS.size(); i++) {
            String name = LOGS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 40, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                minecraft.openScreen(new SizeSelectorGUI(name));
            }));
        }

        for (int i = 0; i < CROPS.size(); i++) {
            String name = CROPS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 + 45, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock("device:gather:" + name, "SAVE"));
                onClose();
            }));
        }

        for (int i = 0; i < STATIONS.size(); i++) {
            String name = STATIONS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 + 130, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                minecraft.openScreen(new RotationSelectorGUI("craft:" + name));
            }));
        }

        this.addButton(new ButtonWidget(this.width / 2 + 130, 345, 80, 20, "Guild Banner", (buttonWidget) -> {
            ItemUtils.givePlayerItemStack(ItemUtils.getArmorStandDevice("Guild Banner Placer", "guildBanner"));
            onClose();
        }));

    }

    private void getPlacer(String type, String name) {
        ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock(name + " " + type + " Placer", "device:gather:" + type, "SAVE"));
        onClose();
    }

    public void render(int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground();
        drawCenteredString(font, this.title.asString(), this.width / 2, 30, 16777215);

        drawCenteredString(font, "Fish", this.width / 2 - 170, 50, 16777215);
        drawCenteredString(font, "Ores", this.width / 2 - 85, 50, 16777215);
        drawCenteredString(font, "Logs", this.width / 2, 50, 16777215);
        drawCenteredString(font, "Crops", this.width / 2 + 85, 50, 16777215);
        drawCenteredString(font, "Stations", this.width / 2 + 170, 50, 16777215);

        DiffuseLighting.enableGuiDepthLighting();
        super.render(mouseX, mouseY, delta);
    }

}
