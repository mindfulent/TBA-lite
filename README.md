# TBA-Lite v0.9.96

A lightweight variant of the [TBA](https://github.com/mindfulent/TBA) modpack for The Block Academy CMP. Same server, fewer mods, lower system requirements.

**149 mods** (down from 190 in full TBA) — connects to the same Fabric 1.21.1 server as full TBA players.

## What's Different?

TBA-Lite removes 42 heavy visual, utility, and recording mods while keeping all gameplay content and server compatibility. A tiny stub mod (~90 KB) replaces the 4 custom mods (StreamCraft, SynthCraft, SceneCraft, CoreCurriculum) with no-op payload handlers so Fabric doesn't reject the connection.

| | TBA (Full) | TBA-Lite |
|---|---|---|
| **Mod count** | 190 | 149 |
| **Custom mods** | StreamCraft, SynthCraft, SceneCraft, CoreCurriculum | tba-lite-stubs (compatibility only) |
| **Shaders** | BSL, Complementary, Photon, Solas | BSL, Solas |
| **Visual effects** | Full (particles, animations, sound physics) | Reduced |
| **Utility mods** | REI, Litematica, WTHIT, Mod Menu, etc. | Removed |
| **Recording** | ReplayMod | Removed |
| **RAM usage** | 4-6 GB recommended | Lower |
| **Video streaming** | Full webcam/screenshare via LiveKit | Not available |
| **AI music** | Full generation and broadcasting | Not available |
| **Cinematic recording** | Full highlight detection and export | Not available |
| **Build submissions** | Full screenshot capture and review | Not available |

### What's Removed

**Visual/Cosmetic (23):** AmbientSounds, Auto HUD, BetterF3, Blur, Camera Overhaul, Cool Rain, Do a Barrel Roll, Eating Animation, Entity Model Features, Entity Texture Features, Falling Leaves, First Person Model, Inventory Particles, Leaf Me Alone, Not Enough Animations, Particle Rain, Presence Footsteps, Skin Layers 3D, Sound Physics Remastered, Stable Cam, Visuality, WaterVision, PlayerAnimator

**Utility (13):** Controlling, ItemSwapper, Litematica, MaLiLib, Mod Menu, Mouse Wheelie, Paginated Advancements, Roughly Enough Items, Simple Discord RPC, Smooth Swapping, Status Effect Bars, WTHIT, Searchables

**Recording (1):** Replay Mod

**Library (1):** MidnightLib

**Shaders (2):** Complementary Reimagined, Photon

### What's Kept

All building, decoration, content, farming, multiplayer, performance, and server-side mods remain — including Macaw's suite, Chipped, Handcrafted, Farmer's Delight, Let's Do series, Fabric Seasons, Simple Voice Chat, JourneyMap, Distant Horizons, Sodium, Lithium, and more.

## Quick Start

### CurseForge App
1. Download the latest `.zip` from [Releases](https://github.com/mindfulent/TBA-lite/releases)
2. In CurseForge: **Create Custom Profile** → **Import** → Select the `.zip`
3. Allocate 4-6 GB RAM
4. Connect to the server

### Prism Launcher
1. Download the latest `.mrpack` from [Releases](https://github.com/mindfulent/TBA-lite/releases)
2. In Prism: **Add Instance** → **Import** → Select the `.mrpack`
3. Allocate 4-6 GB RAM
4. Connect to the server

### Get Whitelisted
Use `/register <your-minecraft-username>` in our Discord server.

## Features

### Performance Optimizations
- **Sodium** - Modern rendering engine
- **Lithium** - Game logic optimizations
- **FerriteCore** - Memory usage reduction
- **ModernFix** - Faster loading, dynamic resource management
- **Krypton** - Network stack optimization
- **Iris Shaders** - OptiFine shader compatibility
- **Distant Horizons** - LOD rendering for extended view distances
- **C2ME** - Multi-core chunk generation
- **ImmediatelyFast** - Immediate mode rendering optimization
- **BadOptimizations** - Micro-optimizations
- **Enhanced Block Entities** - Faster block entity rendering

### Building & Decoration
- **Axiom** - All-in-one building/editing tool (Right-Shift for Editor Mode)
- **Effortless Structure** - Mirrors, arrays, build modes
- **Chipped** - 11,000+ decorative block variants
- **Handcrafted** - 250+ furniture pieces
- **Macaw's Suite** - Doors, Windows, Bridges, Roofs, Fences, Trapdoors, Furniture, Lights
- **Excessive Building** - 100+ vanilla-style blocks
- **Adorn** - Chairs, sofas, tables, shelves
- **Supplementaries** - Jars, signs, clocks, pulleys, cages
- **ReFramed** - Copy block appearances onto stairs, slabs, fences
- **Arts & Crafts** - Dyeable decorated pots, chalk, terracotta
- And many more (Clutter, Beautify, Sooty Chimneys, Little Joys, etc.)

### Content
- **Farmer's Delight** + More Delight - Cooking and farming expansion
- **[Let's Do] Bakery, Vinery, Brewery, Meadow, Beach Party** - Themed content packs
- **Fabric Seasons** - Four seasons with visual and gameplay changes
- **Joy of Painting** - Paint custom pictures on canvases
- **Camerapture** - In-game photos as wall pictures
- **WaterFrames** - Display images/videos on in-game frames
- **Friends and Foes** - Mob vote mobs
- **Lootr** - Per-player loot in chests

### Quality of Life
- **JourneyMap** - World map with waypoints
- **AppleSkin** - Food/hunger visualization
- **Carry On** - Pick up and carry blocks and entities
- **Sit** - Right-click stairs to sit
- **Actually Harvest** - Right-click crops to harvest
- **Easy Magic** - Enchanting table QoL
- **Bridging Mod** - Bedrock-style placement
- **Stack to Nearby Chests** - Deposit into nearby chests
- **Gamemode Unrestrictor** - F3+F4 gamemode menu

### Multiplayer
- **Simple Voice Chat** - Proximity-based voice communication
- **Voice Chat Interaction** - Sounds play through voice chat proximity
- **Flan** - Land claiming and protection
- **Better Sleep** - Sleep voting
- **LuckPerms** - Permissions system

### Audio
- **Subtle Effects** - Ambient environmental effects
- **Sounds** - 170+ UI and interaction sounds
- **Even More Instruments** - Keyboard, violin, guitar, saxophone with MIDI support
- **Genshin Instruments** - Additional instrument selection

### Web Map
- **BlueMap** - Live 3D web map of the server world

### Bundled Shaders
- **BSL v10.0** - High visual quality
- **Solas Shader v3.1c** - Fantasy stylized with colored lighting

## tba-lite-stubs

The `stubs/` directory contains the source code for the compatibility stub mod. It registers:

- **34 payload types** across 4 namespaces (streamcraft-server, synthcraft, scenecraft, corecurriculum)
- **StreamCraft display block** (block, item, block entity) so existing display blocks in the world render correctly

The stub mod uses `"provides"` in fabric.mod.json to satisfy mod dependency checks for all 4 custom mods.

### Building the Stub

```bash
cd stubs
JAVA_HOME="/path/to/java21" ./gradlew build
# Output: build/libs/tba-lite-stubs-0.1.0.jar (~90 KB)
```

## Project Structure

```
TBA-lite/
├── pack.toml                 # Pack metadata (name, version, MC version, loader)
├── index.toml                # File index with hashes
├── options.txt               # Default game options
├── CHANGELOG.md              # Version history
├── mods/                     # Mod metadata files (.pw.toml) + stub JAR
├── config/                   # Mod configurations
├── shaderpacks/              # Bundled shaders (BSL, Solas)
├── defaultconfigs/           # Default mod configs
└── stubs/                    # tba-lite-stubs Gradle project
    ├── build.gradle
    ├── gradle.properties
    └── src/main/java/com/tba/lite/stubs/
        ├── TbaLiteStubs.java           # Main entrypoint
        ├── TbaLiteStubsClient.java     # Client entrypoint (empty)
        ├── block/                      # Display block stub
        └── network/                    # Payload registrations (4 files)
```

## For Development

```bash
# Clone the repository
git clone https://github.com/mindfulent/TBA-lite.git
cd TBA-lite

# Download Packwiz
# Get from: https://github.com/packwiz/packwiz/releases

# Refresh index after changes
./packwiz.exe refresh

# Export for distribution
./packwiz.exe modrinth export      # .mrpack for Prism Launcher
./packwiz.exe curseforge export    # .zip for CurseForge App
```

## Keeping TBA-Lite in Sync

When the full TBA modpack updates, TBA-Lite needs to be synced:

1. Copy updated/new `.pw.toml` files from `TBA/mods/` (excluding removed mods)
2. Copy updated `config/` and `defaultconfigs/`
3. If custom mods add new payload types, update the stub source and rebuild
4. Bump version in `pack.toml`
5. Run `./packwiz.exe refresh` and export

## Requirements

- **Minecraft:** 1.21.1
- **Mod Loader:** Fabric 0.18.3+
- **Java:** 21
- **RAM:** 4 GB minimum, 6 GB recommended

## License

This modpack configuration is open source. Individual mods retain their respective licenses. The tba-lite-stubs mod is All Rights Reserved.

## Links

- [Full TBA Modpack](https://github.com/mindfulent/TBA) - The complete 190-mod experience
- [The Block Academy](https://theblockacademy.com) - Community website
- [Prism Launcher](https://prismlauncher.org/) - Recommended launcher
- [Packwiz Documentation](https://packwiz.infra.link/) - Modpack tooling
