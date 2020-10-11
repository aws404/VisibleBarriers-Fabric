package com.aws404.visiblebarriers.lootchests;

import com.aws404.visiblebarriers.VisibleBarriers;
import com.aws404.visiblebarriers.config.categories.LootChestConfigCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LootChestManager {
    public static File LOOT_CHEST_FILE = new File(LootChestConfigCategory.FILE_PATH.getValue());
    public static final ArrayList<LootChest> LOOT_CHEST_LOCATIONS = new ArrayList<>();
    private static final Pattern LOC_PATTERN = Pattern.compile("([^#])( )*-( )*loc:( )*(?<coords>-?\\d*,-?\\d*,-?\\d*)");
    private static final Pattern LEVEL_PATTERN = Pattern.compile("grade(?<level>\\d):");

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void reloadLootChests() throws IOException {
        if (!LOOT_CHEST_FILE.exists() || !LOOT_CHEST_FILE.canRead() || !LOOT_CHEST_FILE.isFile()) {
            return;
        }

        LOOT_CHEST_LOCATIONS.clear();

        FileReader fileReader = new FileReader(LOOT_CHEST_FILE);
        BufferedReader reader = new BufferedReader(fileReader);

        int currentLevel = 1;
        int lineNo = 1;
        String line;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = LOC_PATTERN.matcher(line);
            if (matcher.find()) {
                String[] coords = matcher.group("coords").split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);

                LOOT_CHEST_LOCATIONS.add(new LootChest(currentLevel, lineNo, new BlockPos(x, y, z)));
            } else {
                Matcher levelMatcher = LEVEL_PATTERN.matcher(line);

                if (levelMatcher.find()) {
                    currentLevel = Integer.parseInt(levelMatcher.group("level"));
                }
            }
            lineNo++;
        }

        reader.close();
        fileReader.close();

        VisibleBarriers.LOGGER.info("Successfully loaded '{}' chests from the loot chest file.", LOOT_CHEST_LOCATIONS.size());
    }

    public static List<LootChest> getRelevantLocations(int distance) {
        BlockPos playerLocation = client.player.getBlockPos();
        return LOOT_CHEST_LOCATIONS.stream().filter(lootChest -> lootChest.pos.isWithinDistance(playerLocation, distance)).sorted((o1, o2) -> {
            double distance1 = o1.pos.getSquaredDistance(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), true);
            double distance2 = o2.pos.getSquaredDistance(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), true);
            return Double.compare(distance1, distance2);
        }).collect(Collectors.toList());
    }

    public static class LootChest {
        public final int level;
        public final int line;
        public final BlockPos pos;

        public LootChest(int level, int line, BlockPos pos) {
            this.level = level;
            this.line = line;
            this.pos = pos;
        }

    }
}
