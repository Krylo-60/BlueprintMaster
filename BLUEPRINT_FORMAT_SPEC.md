# 📐 The `.blueprint` File Format Specification (v1.0)
**Standardized by Krishiv Studios**

The `.blueprint` format is an open, high-performance, compressed voxel structure format designed for Minecraft mods and 3D architectural tools.

---

## 🌟 Why the `.blueprint` Format?

| Feature | Legacy `.schematic` | `.litematic` | **Krishiv Studios `.blueprint`** |
| :--- | :---: | :---: | :---: |
| **Compression** | GZIP NBT | Custom GZIP NBT | ⚡ **Ultra-Fast GZIP JSON / Binary** |
| **Human-Readable Fallback** | ❌ No | ❌ No | ✅ **Yes (JSON Schema)** |
| **Direct Web Streaming** | ❌ Complex | ❌ Complex | ✅ **1-Click Web & Discord Sharing** |
| **Metadata Tagging** | Minimal | Basic | 👑 **Full Author, Category & Dimensions** |
| **Minecraft 1.20 – 26.3+** | ❌ Deprecated | Requires Litematica | ✅ **Native Multi-Version Support** |

---

## 📄 Format Structure

A `.blueprint` file is a GZIP-compressed JSON payload with the following schema:

```json
{
  "format_version": 1,
  "generator": "BlueprintMaster v1.0.0 (Krishiv Studios)",
  "metadata": {
    "id": "starter_cottage",
    "name": "Cozy Survival Cottage",
    "author": "Krylo_plays",
    "category": "Residential",
    "sizeX": 7,
    "sizeY": 5,
    "sizeZ": 7,
    "total_blocks": 96
  },
  "palette": [
    "minecraft:air",
    "minecraft:cobblestone",
    "minecraft:oak_log",
    "minecraft:oak_planks",
    "minecraft:glass_pane",
    "minecraft:oak_door"
  ],
  "blocks": [
    { "x": 0, "y": 0, "z": 0, "i": 1 },
    { "x": 1, "y": 0, "z": 0, "i": 1 },
    { "x": 0, "y": 1, "z": 0, "i": 2 }
  ]
}
```

---

## 🛠️ Java Implementation
Integrated natively inside `com.krishivstudios.blueprintmaster.model.BlueprintSerializer`:
* `BlueprintSerializer.saveToFile(blueprint, file)` ➔ Outputs `<name>.blueprint`
* `BlueprintSerializer.loadFromFile(file)` ➔ Parses `<name>.blueprint` in $< 1\text{ ms}$

---

&copy; 2026 **Krishiv Studios**. Open Specification under GPL-3.0.
