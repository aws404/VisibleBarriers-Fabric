package com.aws404.visiblebarriers.screens;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
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
        super(new TranslatableText("menu.device.title", new Object[0]));
    }

    protected void init() {
        for (int i = 0; i < FISH.size(); i++) {
            String name = FISH.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 210, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.getDevice(name);
            }));
        }

        for (int i = 0; i < ORES.size(); i++) {
            String name = ORES.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 125, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.chooseType(name);
            }));
        }

        for (int i = 0; i < LOGS.size(); i++) {
            String name = LOGS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 40, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.chooseSize(name);
            }));
        }

        for (int i = 0; i < CROPS.size(); i++) {
            String name = CROPS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 + 45, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.getDevice(name);
            }));
        }

        for (int i = 0; i < STATIONS.size(); i++) {
            String name = STATIONS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 + 130, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.getCrafter(name);
            }));
        }

        this.addButton(new ButtonWidget(this.width / 2 + 130, 345, 80, 20, new LiteralText("Guild Banner"), (buttonWidget) -> {
            this.getGuildBanner();
        }));

    }

    private void getDevice(String type) {
        ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock("device:gather:" + type, "SAVE"));
        this.client.openScreen((Screen) null);
    }

    private void getCrafter(String type) {
        this.client.openScreen((Screen) new RotationSelectorGUI("craft:" + type));
    }

    private void getGuildBanner() {
        ItemStack stack = new ItemStack(Items.ARMOR_STAND, 1);

        CompoundTag parent = new CompoundTag();

        parent.putBoolean("NoGravity", true);
        parent.putBoolean("ShowArms", true);
        parent.putBoolean("Invisible", true);
        parent.putBoolean("isDevice", true);
        parent.putBoolean("NoBasePlate", true);
        parent.putString("deviceName", "guildBanner");

        stack.putSubTag("parentB", parent);

        stack.getTag().putBoolean("PlacerTool", true);
        stack.getTag().putInt("otherEntities", 0);

        stack.setCustomName(new LiteralText("Guild Banner Placer"));

        ItemUtils.givePlayerItemStack(stack);
        onClose();
    }

    private void chooseType(String name) {
        this.client.openScreen(new OreTypeGUI(name));
    }

    private void chooseSize(String name) {
        this.client.openScreen(new SizeSelectorGUI(name));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 30, 16777215);

        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Fish"), this.width / 2 - 170, 50, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Ores"), this.width / 2 - 85, 50, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Logs"), this.width / 2, 50, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Crops"), this.width / 2 + 85, 50, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Stations"), this.width / 2 + 170, 50, 16777215);

        DiffuseLighting.enableGuiDepthLighting();
        super.render(matrices, mouseX, mouseY, delta);
    }

}
