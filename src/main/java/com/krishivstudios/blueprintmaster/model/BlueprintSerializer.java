package com.krishivstudios.blueprintmaster.model;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Official Krishiv Studios `.blueprint` File Format Specification (v1.0)
 * Fast, lightweight, compressed 3D structure data format for Minecraft.
 */
public class BlueprintSerializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static byte[] serialize(Blueprint blueprint) throws IOException {
        JsonObject root = new JsonObject();
        root.addProperty("format_version", 1);
        root.addProperty("generator", "BlueprintMaster v1.0.0 (Krishiv Studios)");

        // 1. Metadata
        JsonObject meta = new JsonObject();
        meta.addProperty("id", blueprint.getId());
        meta.addProperty("name", blueprint.getDisplayName());
        meta.addProperty("author", blueprint.getAuthor());
        meta.addProperty("category", blueprint.getCategory());
        meta.addProperty("sizeX", blueprint.getSizeX());
        meta.addProperty("sizeY", blueprint.getSizeY());
        meta.addProperty("sizeZ", blueprint.getSizeZ());
        meta.addProperty("total_blocks", blueprint.getTotalBlocks());
        root.add("metadata", meta);

        // 2. Palette
        List<String> paletteList = new ArrayList<>();
        Map<String, Integer> paletteIndexMap = new HashMap<>();

        paletteList.add("minecraft:air");
        paletteIndexMap.put("minecraft:air", 0);

        for (BlockState state : blueprint.getBlocks().values()) {
            Identifier id = Registries.BLOCK.getId(state.getBlock());
            String blockId = id.toString();
            if (!paletteIndexMap.containsKey(blockId)) {
                paletteIndexMap.put(blockId, paletteList.size());
                paletteList.add(blockId);
            }
        }

        JsonArray paletteArray = new JsonArray();
        for (String blockId : paletteList) {
            paletteArray.add(blockId);
        }
        root.add("palette", paletteArray);

        // 3. Block Coordinate Matrix
        JsonArray blocksArray = new JsonArray();
        for (Map.Entry<BlockPos, BlockState> entry : blueprint.getBlocks().entrySet()) {
            BlockPos pos = entry.getKey();
            Identifier id = Registries.BLOCK.getId(entry.getValue().getBlock());
            int paletteIndex = paletteIndexMap.getOrDefault(id.toString(), 0);
            if (paletteIndex != 0) {
                JsonObject blockObj = new JsonObject();
                blockObj.addProperty("x", pos.getX());
                blockObj.addProperty("y", pos.getY());
                blockObj.addProperty("z", pos.getZ());
                blockObj.addProperty("i", paletteIndex);
                blocksArray.add(blockObj);
            }
        }
        root.add("blocks", blocksArray);

        // 4. Compress to GZIP .blueprint bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(root.toString().getBytes(StandardCharsets.UTF_8));
        }
        return baos.toByteArray();
    }

    public static Blueprint deserialize(byte[] data) throws IOException {
        String jsonString;
        try (GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(data));
             InputStreamReader reader = new InputStreamReader(gzis, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            jsonString = sb.toString();
        } catch (Exception e) {
            // Fallback for uncompressed JSON
            jsonString = new String(data, StandardCharsets.UTF_8);
        }

        JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject meta = root.getAsJsonObject("metadata");

        String id = meta.get("id").getAsString();
        String name = meta.get("name").getAsString();
        String author = meta.get("author").getAsString();
        String category = meta.get("category").getAsString();
        int sx = meta.get("sizeX").getAsInt();
        int sy = meta.get("sizeY").getAsInt();
        int sz = meta.get("sizeZ").getAsInt();

        Blueprint blueprint = new Blueprint(id, name, author, category, sx, sy, sz);

        // Read Palette
        JsonArray paletteArray = root.getAsJsonArray("palette");
        List<Block> palette = new ArrayList<>();
        for (JsonElement elem : paletteArray) {
            Identifier blockId = Identifier.of(elem.getAsString());
            Block block = Registries.BLOCK.get(blockId);
            palette.add(block != null ? block : Blocks.AIR);
        }

        // Read Blocks
        JsonArray blocksArray = root.getAsJsonArray("blocks");
        for (JsonElement elem : blocksArray) {
            JsonObject b = elem.getAsJsonObject();
            int x = b.get("x").getAsInt();
            int y = b.get("y").getAsInt();
            int z = b.get("z").getAsInt();
            int pIndex = b.get("i").getAsInt();

            if (pIndex > 0 && pIndex < palette.size()) {
                blueprint.setBlock(x, y, z, palette.get(pIndex));
            }
        }

        return blueprint;
    }

    public static void saveToFile(Blueprint blueprint, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        byte[] bytes = serialize(blueprint);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    public static Blueprint loadFromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            return deserialize(bytes);
        }
    }
}
