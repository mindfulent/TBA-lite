# TBA-Lite: Product Requirements Document

**Version:** Draft v0.2
**Date:** 2026-02-16
**Author:** Slash + Claude

## 1. Overview

TBA-Lite is a lightweight variant of The Block Academy (TBA) Fabric 1.21.1 modpack, designed for players with resource-constrained machines. It connects to the same TBA server running the full 190-mod pack — players just get a leaner client experience with certain features unavailable.

### 1.1 Goals

- **Lower system requirements** — reduce RAM, GPU, and disk usage significantly
- **Full server compatibility** — lite players connect to the same server as full players, no server changes needed
- **No missing-texture issues** — all content mods (blocks, items, entities) stay so the world looks correct
- **Graceful feature degradation** — features tied to removed mods silently don't work; no errors or crashes
- **Simple maintenance** — easy to keep in sync with TBA releases

### 1.2 Non-Goals

- Replacing TBA as the primary modpack (TBA-Lite is an alternative, not a replacement)
- Matching TBA's feature set (some features are intentionally sacrificed for performance)
- Supporting different Minecraft or Fabric versions (always matches TBA exactly)

## 2. Technical Feasibility

### 2.1 Server Compatibility Model

The TBA server runs ALL 190 mods. TBA-Lite clients must connect without errors. The key constraints:

1. **Fabric enforces payload type registration.** In Fabric 1.21.1, mods register custom network payload types via `PayloadTypeRegistry.playC2S()` and `playS2C()`. During connection, both client and server must agree on registered payload types. If the server has payload types the client doesn't recognize, **the connection is rejected**. This has been confirmed by testing: removing any custom mod JAR from the client prevents login.

2. **Content mods (blocks/items) must stay on the client.** Mods that add blocks, items, or entities to the game registry must remain — removing them causes missing textures (purple/black checkerboard) for blocks placed in the world.

3. **All four custom mods need stub JARs.** StreamCraft, SynthCraft, SceneCraft, and CoreCurriculum all register custom payload types. Even though none use login-phase or configuration-phase networking (`ServerLoginNetworking`/`ServerConfigurationNetworking`), Fabric's payload type registry sync still requires the client to have matching registrations. A **unified stub JAR** that registers all payload types (with no-op handlers) and StreamCraft's block/item/entity entries solves this.

4. **Third-party mods with only client-side features can be removed.** Pure visual, audio, and utility mods that don't register custom payload types or registry entries are safe to remove.

### 2.2 Custom Mod Payload Inventory

All payload types that the stub must register:

| Mod | Namespace | C2S Payloads | S2C Payloads | Block Registry |
|-----|-----------|-------------|-------------|----------------|
| **StreamCraft** | `streamcraft-server` | 5 (`handshake`, `request_token`, `stream_update`, `display_appearance`, `display_streaming`) | 4 (`handshake_response`, `token_response`, `visibility`, `viewers`) | `display_block` (Block + Item + BlockEntity) |
| **SynthCraft** | `synthcraft` | 4 (`handshake`, `generate_request`, `broadcast_request`, `playback_action`) | 8 (`handshake_response`, `generation_status`, `generation_complete`, `broadcast_start`, `broadcast_stop`, `broadcast_queue_update`, `vanilla_music_control`, `now_playing`) | None |
| **SceneCraft** | `scenecraft` | 4 (`handshake`, `export_request`, `export_complete`, `report_sessions`) | 3 (`handshake_response`, `export_permission`, `report_sessions_ack`) | None |
| **CoreCurriculum** | `corecurriculum` | 1 (`submit_build`) | 5 (`title_sync`, `open_gui`, `notification`, `submissions`, `recognitions`) | None |
| **Total** | — | **14** | **20** | **1 block** |

### 2.3 What Can Be Safely Removed

| Category | Why Safe to Remove | Impact |
|----------|-------------------|--------|
| Client-only visual mods | No payload types, no registry entries | Visual fidelity reduced |
| Client-only audio mods | No payload types, no registry entries | Sound effects reduced |
| Client-only utility mods | No payload types, no registry entries | Convenience features lost |
| Shader packs (selectively) | Asset files, loaded on demand | Fewer shader options |

### 2.4 What CANNOT Be Removed

| Category | Why Required | Consequence of Removal |
|----------|-------------|----------------------|
| Content mods (add blocks/items) | Server world contains these blocks | Missing textures, broken world visuals |
| Mods with custom payload types | Fabric rejects connection if payload types don't match | Cannot login to server |
| Both-side mechanic mods | Server expects client-side behavior | Desyncs, glitches, or kicks |
| Performance mods | Essential for playability | Worse FPS than vanilla |
| Library/API mods | Dependencies of kept mods | Crash on startup |

## 3. Mod Audit: Full Classification

TBA v0.9.96 contains 190 mods. Below is a complete classification.

### 3.1 REMOVE — Client-Only Visual/Cosmetic (24 mods)

These mods are purely client-side rendering enhancements. They add no registry entries and no custom payload types. Removing them improves FPS and reduces memory.

| Mod | What It Does | Estimated Savings |
|-----|-------------|-------------------|
| `ambientsounds` | Ambient environmental audio | Minor RAM |
| `auto-hud` | Auto-hides HUD elements | Negligible |
| `betterf3` | Enhanced F3 debug screen | Negligible |
| `blur-fabric` | GUI background blur | Minor GPU |
| `cameraoverhaul` | Camera tilt/sway effects | Minor CPU |
| `cool-rain` | Enhanced rain visuals | Minor GPU |
| `do-a-barrel-roll` | Elytra camera rolls | Negligible |
| `eating-animation-fabric` | Eating animations | Negligible |
| `entity-model-features` | Custom entity models | Moderate GPU/RAM |
| `entity-texture-features-fabric` | Custom entity textures | Moderate GPU/RAM |
| `falling-leaves-fabric` | Leaf particles from trees | Minor GPU |
| `first-person-model` | Full body in first person | Minor GPU |
| `inventory-particles` | Particles when using items | Negligible |
| `nords-leaf-me-alone` | Disables leaf particles | Negligible |
| `not-enough-animations` | Extra player animations | Minor GPU |
| `particle-rain` | 3D rain/snow particles | Moderate GPU |
| `presence-footsteps` | Footstep sound effects | Minor CPU/RAM |
| `skin-layers-3d` | 3D skin layer rendering | Minor GPU |
| `sound-physics-remastered` | Reverb and sound occlusion | Moderate CPU |
| `stable-cam` | Camera stabilization | Negligible |
| `subtle-effects` | Screen shake, hit effects | Negligible |
| `visuality` | Extra ambient particles | Minor GPU |
| `watervision` | Clear underwater vision | Negligible |
| `playeranimator` | Animation framework (only used by cosmetic mods) | Minor |

**Also remove these libraries** that are only needed by the above:
- `midnightlib` (dep of ETF)

### 3.2 REMOVE — Client-Only Utility (13 mods)

These provide client-side convenience features. No server interaction, no custom payload types.

| Mod | What It Does | Estimated Savings |
|-----|-------------|-------------------|
| `controlling` | Keybind conflict search | Negligible |
| `itemswapper` | Quick item swap radial menu | Negligible |
| `litematica` | Schematic display/building | Moderate RAM |
| `malilib` | Library for Litematica | (dep of litematica) |
| `modmenu` | Mod list/config screen | Negligible |
| `mouse-wheelie` | Scroll-based inventory mgmt | Negligible |
| `paginated-advancements` | Better advancement screen | Negligible |
| `roughly-enough-items` | Recipe browser (REI) | Moderate RAM |
| `simple-discord-rpc` | Discord Rich Presence | Negligible |
| `smooth-swapping` | Item move animation | Negligible |
| `status-effect-bars` | Effect bar on HUD | Negligible |
| `wthit` | Block tooltip overlay (WAILA) | Minor RAM |
| `searchables` | Search library (dep of REI/controlling) | (dep) |

### 3.3 REPLACE WITH STUB — Custom TBA Mods (4 mods)

All four custom mods register custom `PayloadTypeRegistry` payload types. Fabric's payload sync requires the client to have matching registrations — removing any of these JARs prevents login to the server. **Confirmed by testing.**

These mods are replaced by a single **unified stub JAR** (`tba-lite-stubs`) that registers all 34 payload types with no-op handlers, plus StreamCraft's block/item/entity entries. See Section 4 for full design.

| Mod | Payload Types | Block Registry | Full JAR Size | Stub Replaces |
|-----|--------------|----------------|--------------|---------------|
| `streamcraft-live` | 9 (5 C2S + 4 S2C) | `display_block` (Block + Item + BlockEntity) | ~100+ MB (bundles MCEF/Chromium) | Yes |
| `synthcraft-live` | 12 (4 C2S + 8 S2C) | None | ~2 MB | Yes |
| `scenecraft` | 7 (4 C2S + 3 S2C) | None | ~2 MB | Yes |
| `core-curriculum` | 6 (1 C2S + 5 S2C) | None | ~200 KB | Yes |

**What lite players lose:**
- StreamCraft: No video conferencing. Display blocks render as placeholder cubes.
- SynthCraft: No music generation UI, no broadcast audio playback. Vanilla music plays normally.
- SceneCraft: No cinematic recording/replay/export.
- CoreCurriculum: No title catalog GUI, no build submissions. Server-side chat titles via LuckPerms still display.

### 3.4 REMOVE — Shader Packs (2 of 4)

Per requirements, keep BSL and Solas (default off). Remove the other two.

| Shader | Action | Size |
|--------|--------|------|
| `BSL_v10.0.zip` | **KEEP** (default off) | ~2 MB |
| `Solas_Shader_v3.1c.zip` | **KEEP** (default off) | ~1 MB |
| `ComplementaryReimagined_r5.6.1.zip` | **REMOVE** | ~3 MB |
| `photon_v1.2a.zip` | **REMOVE** | ~2 MB |

### 3.5 REMOVE — Client Recording (1 mod)

| Mod | What It Does | Notes |
|-----|-------------|-------|
| `replay-mod-fabric-and-forge` | Records gameplay for replay/rendering | Client-only recording tool. Server doesn't require it. |

### 3.6 KEEP — Performance/Optimization (12 mods)

These are essential for playability, especially on resource-constrained machines.

| Mod | What It Does |
|-----|-------------|
| `sodium` | Modern rendering engine (massive FPS boost) |
| `lithium` | Game logic optimization |
| `ferritecore-fabric` | Memory usage optimization |
| `modernfix` | Startup time + memory fixes |
| `badoptimizations` | Various performance fixes |
| `c2me` | Chunk loading optimization |
| `clumps` | Merges XP orbs (reduces entity count) |
| `entityculling` | Skips rendering hidden entities |
| `enhanced-block-entities` | Faster block entity rendering |
| `immediatelyfast` | Immediate rendering optimizations |
| `krypton` | Network stack optimization |
| `fast-item-frames` | Optimized item frame rendering |

### 3.7 KEEP — Rendering/Shaders (2 mods)

| Mod | What It Does | Notes |
|-----|-------------|-------|
| `irisshaders` | Shader loading engine | Needed for BSL/Solas |
| `distant-horizons` | LOD chunk rendering | Heavy but requested to stay |

### 3.8 KEEP — Content Mods (95+ mods)

All mods that add blocks, items, entities, or world content MUST stay. The server world contains these blocks — removing them breaks world visuals.

**Decoration/Building:**
`adorn`, `amendments`, `armor-statues`, `artsandcrafts`, `beautify-refabricated`, `chipped`, `clutter`, `connectiblechains`, `diagonal-fences`, `diagonal-walls`, `diagonal-windows`, `dusty-decorations`, `enchanted-vertical-slabs`, `excessive-building`, `fetzis-asian-decoration`, `handcrafted`, `immersive-paintings`, `joy-of-painting`, `little-joys`, `macaws-bridges`, `macaws-doors`, `macaws-fences-and-walls`, `macaws-furniture`, `macaws-holidays`, `macaws-lights-and-lamps`, `macaws-paintings`, `macaws-paths-and-pavings`, `macaws-roofs`, `macaws-trapdoors`, `macaws-windows`, `reframed`, `reintegrated-arts-and-crafts`, `sooty-chimneys`, `storage-drawers`, `verdantvibes-unofficial-port`, `windchime-unofficial-continued`

**Food/Farming:**
`farmers-delight-refabricated`, `more-delight-fabric`, `lets-do-bakery-farm-charm-compat`, `lets-do-brewery-farm-charm-compat`, `lets-do-farm-charm`, `lets-do-meadow`, `lets-do-vinery`, `lets-do-beachparty`

**Gameplay/Mechanics:**
`accurate-block-placement-reborn`, `actually-harvest`, `better-climbing`, `block-runner`, `bridging-mod`, `carry-on`, `effortless`, `fabric-seasons`, `fabric-seasons-delight-compat`, `fabric-seasons-extras`, `falling-tree`, `flan`, `friends-and-foes`, `illager-invasion`, `leaves-be-gone`, `lootr-fabric`, `naturally-trimmed`, `niftycarts`, `sawmill`, `sit-fabric`, `supplementaries`, `trade-cycling`, `travelers-backpack-fabric`, `trimmable-tools`, `trinkets`, `universal-bone-meal`

**Music/Instruments:**
`even-more-instruments`, `genshin-instruments`

**Multimedia:**
`waterframes`, `watermedia`, `watermedia-yt-plugin`, `vinurl`

**Utility (both-sides):**
`appleskin`, `camerapture`, `continuity`, `easy-magic`, `invmove`, `stack-to-nearby-chests`

**Voice Chat:**
`simple-voice-chat`, `voice-chat-interaction`

**Maps/Info:**
`bluemap`, `journeymap`

**Misc:**
`connectivity`, `minescript`, `sound`, `spark`, `better-third-person`, `camera-utils`

### 3.9 KEEP — Server-Only (Already Not in Client)

These are in the TBA mrpack for server deployment but aren't installed on clients:
`advanced-backups`, `autowhitelist`, `better-sleep`, `fabricord`, `ledger`, `luckperms`, `luckperms-placeholders`, `chunky-pregenerator`, `essential-permissions`, `gamemode-unrestrictor`, `styled-chat`, `text-placeholder-api`

### 3.10 KEEP — Library/API Mods

All library mods required by kept mods must stay. Remove only libraries whose sole dependents were removed.

**Must keep** (used by content/gameplay mods):
`architectury-api`, `athena`, `axiomtool`, `badpackets`, `balm-fabric`, `cardinal-components-api`, `cicada`, `cloth-config`, `craterlib`, `creativecore`, `cupboard`, `fabric-api`, `fabric-language-kotlin`, `forge-config-api-port`, `fzzy-config`, `geckolib`, `jinxedlib`, `konkrete-fabric`, `lithostitched`, `mossylib`, `mru`, `owo-lib`, `polymer`, `puzzles-lib`, `resourceful-lib`, `selene`, `yacl`

**Can remove** (only needed by removed mods):
- `malilib` — only dependency of Litematica (removed)
- `searchables` — only dependency of Controlling (removed)
- `midnightlib` — only dependency of ETF (removed)

## 4. TBA-Lite Stubs — Unified Compatibility JAR

### 4.1 Approach: One Stub to Rule Them All

Rather than creating 4 separate stub JARs (one per custom mod), we build a **single unified mod** (`tba-lite-stubs`) that registers everything the server expects from all four mods. This is cleaner to maintain, distribute, and version.

The stub JAR replaces all four custom mod JARs in the lite modpack.

### 4.2 What the Stub Contains

```
tba-lite-stubs-X.Y.Z.jar
├── fabric.mod.json                    (mod ID: "tba-lite-stubs")
│                                      (provides: ["streamcraft", "synthcraft",
│                                                   "scenecraft", "corecurriculum"])
├── assets/
│   └── streamcraft-server/
│       ├── blockstates/display_block.json
│       ├── models/block/display_block.json    (simple cube model)
│       ├── models/item/display_block.json
│       └── textures/block/display_block.png   (placeholder texture)
│
└── src (compiled classes):
    └── com/tba/lite/stubs/
        ├── TbaLiteStubs.java               (main entrypoint)
        │   └── onInitialize():
        │       - Registers StreamCraft display_block (Block + Item + BlockEntity)
        │       - Registers all 34 payload types from all 4 mods
        │
        ├── TbaLiteStubsClient.java         (client entrypoint — no-op)
        │
        ├── block/
        │   ├── StubDisplayBlock.java        (minimal Block subclass, no behavior)
        │   ├── StubDisplayBlockEntity.java  (minimal BlockEntity, no MCEF)
        │   └── StubBlockRegistration.java   (registers with identical IDs)
        │
        └── network/
            ├── StreamCraftPayloads.java     (9 payload record types + codecs)
            ├── SynthCraftPayloads.java      (12 payload record types + codecs)
            ├── SceneCraftPayloads.java      (7 payload record types + codecs)
            └── CoreCurriculumPayloads.java  (6 payload record types + codecs)
```

### 4.3 Key Design Decisions

**fabric.mod.json `provides` field:**
Fabric supports a `provides` array in `fabric.mod.json` that declares "this mod satisfies the dependency for mod X." By declaring `provides: ["streamcraft", "synthcraft", "scenecraft", "corecurriculum"]`, the stub prevents Fabric from complaining about missing dependencies if any other mod soft-depends on them.

**Payload type registration — must match exactly:**
Each payload type must be registered with the **exact same `CustomPayload.Type` ID and `StreamCodec`** as the real mod. The codec defines the wire format — if the server sends an S2C payload, the client must be able to decode it (even if it discards the result). This means the stub must replicate each payload's record fields and codec.

**Note on CoreCurriculum:** CoreCurriculum uses an older Fabric networking API (`PacketCodec`, `Identifier`, `Id<>`) compared to the other three mods (`StreamCodec`, `ResourceLocation`, `Type<>`). The stub must use the same API version for CoreCurriculum's payloads to ensure wire-format compatibility.

**Block/item registration — StreamCraft only:**
Only StreamCraft registers custom blocks. The stub registers:
- `streamcraft-server:display_block` (Block) — simple opaque cube, no special behavior
- `streamcraft-server:display_block` (Item) — standard BlockItem
- `streamcraft-server:display_block` (BlockEntityType) — minimal BlockEntity that does nothing

**No mixins required:**
The stub does not need any mixins. All four mods' mixins are for client-side rendering, audio, recording, or UI features that the lite pack doesn't provide.

**No heavy dependencies:**
The stub does NOT include or depend on:
- MCEF (Chromium browser) — StreamCraft's biggest dependency (~100+ MB)
- JNA (native screen/webcam capture)
- LiveKit client (WebRTC)
- OpenAL extensions (SynthCraft audio)
- FFmpeg (SceneCraft rendering)

### 4.4 Estimated Stub Size

~50-150 KB. Compare to the full JARs it replaces:
- StreamCraft: ~100+ MB (MCEF alone is ~100 MB)
- SynthCraft: ~2 MB
- SceneCraft: ~2 MB
- CoreCurriculum: ~200 KB
- **Total replaced: ~105 MB → ~100 KB**

### 4.5 Build Strategy

**Recommendation: Standalone Gradle project in `TBA-lite/stubs/`**

The stub needs to reference the exact payload record types and codecs from each mod. Two approaches:

**Option A: Copy payload source files into stub project**
- Copy only the payload record classes (e.g., `HandshakeC2S.java`, `GenerationStatusS2C.java`) from each mod
- Pro: Self-contained, no cross-project dependency
- Con: Must manually sync when payload types change in any mod

**Option B: Gradle multi-project dependency**
- Stub project depends on StreamCraft/SynthCraft/SceneCraft/CoreCurriculum as `compileOnly`
- Directly references the real payload classes
- Pro: Always in sync — build breaks immediately if payloads change
- Con: Requires all 4 mod projects to be available at build time

**Option C: Redefine payload types independently in the stub**
- Write new record classes with identical field names, codecs, and `CustomPayload.Type` IDs
- No source dependency on the real mods at all
- Pro: Completely self-contained, simple build
- Con: Must manually keep codec wire format in sync

**Recommendation: Option C** for initial implementation. The payload types are simple records with straightforward codecs (strings, ints, booleans, UUIDs). They rarely change. If they do change, the TBA version bump process already requires testing — the stub would be tested at the same time.

Long-term, if payload types change frequently, migrate to Option B.

### 4.6 What Lite Players Experience

| In-World Element | Full Client | Lite Client |
|-----------------|-------------|-------------|
| StreamCraft display blocks | Renders video/webcam feed | Renders as solid placeholder cube |
| SynthCraft music broadcast | Hears generated music, sees now-playing HUD | Nothing (vanilla music continues) |
| SceneCraft recording | Can record, replay, export clips | Nothing (feature invisible) |
| CoreCurriculum `/titles` | Opens title catalog GUI | Command does nothing / error message |
| CoreCurriculum chat titles | Shows titles (LuckPerms) | Shows titles (LuckPerms — server-side, still works) |
| All other blocks/items | Normal | Normal |
| Voice chat | Works | Works |
| Seasons | Works | Works |

## 5. Estimated Impact

### 5.1 Mod Count

| | TBA (Full) | TBA-Lite |
|---|---|---|
| Total mods | 190 | ~149 (145 real + 1 stub replacing 4) |
| Removed entirely | — | ~42 (visual, utility, recording, libraries) |
| Replaced by stub | — | 4 (StreamCraft, SynthCraft, SceneCraft, CoreCurriculum) |

### 5.2 Performance Impact (Estimated)

| Metric | TBA (Full) | TBA-Lite (Est.) | Improvement |
|--------|-----------|-----------------|-------------|
| RAM usage | ~4-6 GB | ~3-4 GB | -20-30% |
| Startup time | ~60-90s | ~40-60s | -30% |
| Disk size | ~500 MB | ~350 MB | -30% |
| Base FPS (no shaders) | Baseline | +15-25% | Fewer particles, animations, entity rendering |
| GPU VRAM usage | Baseline | -15-20% | Fewer textures, models, particle systems |

*Biggest wins: StreamCraft removal (~100+ MB disk, significant RAM from MCEF/Chromium), EMF/ETF removal (entity rendering overhead), particle/animation mods, sound physics, REI, Litematica.*

### 5.3 Feature Comparison

| Feature | TBA (Full) | TBA-Lite |
|---------|-----------|----------|
| Video conferencing (StreamCraft) | Yes | No — placeholder blocks |
| AI music generation (SynthCraft) | Yes | No |
| Cinematic recording (SceneCraft) | Yes | No |
| Title catalog GUI (CoreCurriculum) | Yes | No (chat titles still show) |
| Recipe browser (REI) | Yes | No |
| Schematic building (Litematica) | Yes | No |
| Replay recording | Yes | No |
| Custom entity models/textures | Yes | No — vanilla models |
| 3D rain/snow particles | Yes | No — vanilla weather |
| Sound reverb/physics | Yes | No — vanilla sound |
| Complementary/Photon shaders | Yes | No |
| BSL/Solas shaders | Yes (default on) | Yes (default off) |
| Distant Horizons | Yes | Yes |
| All building/decoration blocks | Yes | Yes |
| Voice chat | Yes | Yes |
| Seasons | Yes | Yes |
| All gameplay mechanics | Yes | Yes |
| All food/farming content | Yes | Yes |
| All instruments | Yes | Yes |

## 6. Implementation Plan

### Phase 1: Build the Stub JAR

1. **Create stub Gradle project** at `TBA-lite/stubs/`
   - Fabric Loom setup targeting MC 1.21.1
   - Replicate all 34 payload types with matching codecs and IDs
   - Register StreamCraft's `display_block` with placeholder model/texture
   - `fabric.mod.json` with `provides: ["streamcraft", "synthcraft", "scenecraft", "corecurriculum"]`
   - Build and verify JAR is <200 KB

2. **Test stub in isolation**
   - Add only the stub JAR to a vanilla Fabric 1.21.1 client (no other custom mods)
   - Verify game launches without errors
   - Verify display blocks render as placeholder cubes in singleplayer

### Phase 2: Create the Lite Packwiz Project

3. **Initialize TBA-Lite Packwiz project** in `TBA-lite/`
   - Copy `pack.toml` from TBA, rename to `"TBA-Lite"`, set version
   - Copy all mod `.pw.toml` files from TBA
   - Remove the ~42 mods identified in Sections 3.1, 3.2, 3.5
   - Remove the 4 custom mod `.pw.toml` files (replaced by stub)
   - Add stub JAR as a local override or hosted file
   - Remove orphaned library mods (malilib, searchables, midnightlib)
   - Remove 2 shader packs (Complementary, Photon)
   - Configure shader defaults (BSL/Solas off by default)

4. **Export test build**
   - `./packwiz.exe modrinth export` → `TBA-Lite-X.Y.Z.mrpack`

### Phase 3: Server Compatibility Testing

5. **Test against full TBA server on LocalServer**
   - Start LocalServer with full TBA modpack
   - Connect with TBA-Lite client
   - Verify: successful login, no disconnects, no payload errors
   - Walk through areas with display blocks — verify placeholder rendering
   - Verify voice chat works
   - Verify Fabric Seasons sync works
   - Verify all content mod blocks render correctly
   - Check server logs for any warnings/errors from missing mod features

6. **Test edge cases**
   - Another player using full TBA streams on a display block — verify lite client doesn't crash
   - SynthCraft broadcast from full client — verify lite client doesn't crash
   - CoreCurriculum commands from lite client — verify graceful failure

### Phase 4: Performance Benchmarking

7. **Compare TBA vs TBA-Lite**
   - RAM usage (F3 screen) in identical location
   - FPS in identical location (no shaders)
   - Startup time (cold start)
   - Disk size of installed instance

### Phase 5: Release

8. **Documentation**
   - TBA-Lite README with feature comparison table
   - Installation guide for Prism Launcher
   - Clear messaging: "Same server, lighter client"

9. **Distribution**
   - Create GitHub repo `mindfulent/TBA-Lite`
   - GitHub releases with `.mrpack` and CurseForge `.zip`
   - Discord announcement in #server-releases

### Phase 6: Maintenance

10. **Sync workflow** — when TBA updates:
    - If new content/gameplay mod added → add to TBA-Lite too
    - If new visual/cosmetic mod added → skip for TBA-Lite
    - If custom mod payload types change → rebuild stub
    - If new custom mod added → add payloads to stub
    - Version TBA-Lite to match TBA (e.g., TBA v0.9.97 → TBA-Lite v0.9.97)
    - Always test lite client against updated server before releasing

## 7. Risks & Mitigations

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Payload codec mismatch between stub and real mod | Medium | High (login fails) | Test every release; consider Option B build (compile-time dependency) if this becomes frequent |
| Custom mod adds new payload type without updating stub | Medium | High (login fails) | Include stub rebuild in TBA release checklist |
| Third-party mod update adds new payload types | Low | High (login fails) | Test lite client after every TBA update |
| StreamCraft adds new block/item to registry | Low | Medium (missing texture) | Stub build includes all StreamCraft registry entries |
| Mod removed from "safe list" actually needed on both sides | Medium | Medium (desyncs) | Test each removal individually during Phase 3 |
| Players confused about missing features | Medium | Low | Clear documentation, different modpack name, Discord FAQ |
| Library dependency missed (crash on startup) | Medium | High | Test fresh install; Fabric logs missing deps clearly |
| `provides` field doesn't prevent all dependency checks | Low | Medium | Test with mods that soft-depend on custom mods |

## 8. Open Questions

1. **JourneyMap — keep or remove?** It's a moderate resource user (minimap rendering, chunk caching) but very useful for navigation. Could be replaced with a lighter alternative (Xaero's Minimap). **Recommend: keep for now**, revisit if RAM is still tight.

2. **Distant Horizons tuning** — DH is one of the heaviest mods. Could ship TBA-Lite with lower default DH settings (render distance 64 instead of 128+, lower quality). **Recommend: ship with conservative defaults**.

3. **Should third-party mods in the "keep" list be verified for payload types?** Some mods (Waterframes, Simple Voice Chat, Fabric Seasons) likely register custom payloads too, but since they're kept in TBA-Lite this isn't an issue. However, if we ever want to remove one of them, we'd need a similar stub approach. **Recommend: document which kept mods have payloads, for future reference**.

4. **Polymer integration** — TBA includes Polymer (server-side block display for vanilla clients). Could Polymer handle StreamCraft's display blocks for lite clients instead of the stub? Worth investigating as a potential simplification. **Recommend: investigate after initial stub approach works**.

5. **Automated sync tooling** — Should we build a script that diffs TBA's mod list against TBA-Lite's and flags new mods that need categorization? **Recommend: yes, as part of Phase 6**.

## 9. Appendix: Payload Type Details

### StreamCraft Payloads (namespace: `streamcraft-server`)

**C2S (Client → Server):**
| Payload | ID | Fields |
|---------|----|--------|
| `HandshakeC2S` | `streamcraft-server:handshake` | `protocolVersion` (int) |
| `RequestTokenC2S` | `streamcraft-server:request_token` | `roomName` (string) |
| `StreamUpdateC2S` | `streamcraft-server:stream_update` | `streaming` (bool), `type` (string) |
| `DisplayAppearanceC2S` | `streamcraft-server:display_appearance` | Block pos + appearance data |
| `DisplayStreamingC2S` | `streamcraft-server:display_streaming` | Block pos + stream state |

**S2C (Server → Client):**
| Payload | ID | Fields |
|---------|----|--------|
| `HandshakeResponseS2C` | `streamcraft-server:handshake_response` | `success` (bool), `message` (string), `licenseValid` (bool) |
| `TokenResponseS2C` | `streamcraft-server:token_response` | `token` (string), `url` (string) |
| `VisibilityS2C` | `streamcraft-server:visibility` | Visibility state data |
| `ViewersS2C` | `streamcraft-server:viewers` | Viewer count/list |

### SynthCraft Payloads (namespace: `synthcraft`)

**C2S:**
| Payload | ID |
|---------|----|
| `HandshakeC2S` | `synthcraft:handshake` |
| `GenerateRequestC2S` | `synthcraft:generate_request` |
| `BroadcastRequestC2S` | `synthcraft:broadcast_request` |
| `PlaybackActionC2S` | `synthcraft:playback_action` |

**S2C:**
| Payload | ID |
|---------|----|
| `HandshakeResponseS2C` | `synthcraft:handshake_response` |
| `GenerationStatusS2C` | `synthcraft:generation_status` |
| `GenerationCompleteS2C` | `synthcraft:generation_complete` |
| `BroadcastStartS2C` | `synthcraft:broadcast_start` |
| `BroadcastStopS2C` | `synthcraft:broadcast_stop` |
| `BroadcastQueueUpdateS2C` | `synthcraft:broadcast_queue_update` |
| `VanillaMusicControlS2C` | `synthcraft:vanilla_music_control` |
| `NowPlayingS2C` | `synthcraft:now_playing` |

### SceneCraft Payloads (namespace: `scenecraft`)

**C2S:**
| Payload | ID |
|---------|----|
| `HandshakeC2S` | `scenecraft:handshake` |
| `ExportRequestC2S` | `scenecraft:export_request` |
| `ExportCompleteC2S` | `scenecraft:export_complete` |
| `ReportSessionsC2S` | `scenecraft:report_sessions` |

**S2C:**
| Payload | ID |
|---------|----|
| `HandshakeResponseS2C` | `scenecraft:handshake_response` |
| `ExportPermissionS2C` | `scenecraft:export_permission` |
| `ReportSessionsAckS2C` | `scenecraft:report_sessions_ack` |

### CoreCurriculum Payloads (namespace: `corecurriculum`)

**Note:** Uses older Fabric API (`PacketCodec`, `Identifier`, `Id<>`)

**C2S:**
| Payload | ID |
|---------|----|
| `SubmitBuildPayload` | `corecurriculum:submit_build` |

**S2C:**
| Payload | ID |
|---------|----|
| `TitleSyncPayload` | `corecurriculum:title_sync` |
| `OpenGuiPayload` | `corecurriculum:open_gui` |
| `NotificationPayload` | `corecurriculum:notification` |
| `SubmissionsPayload` | `corecurriculum:submissions` |
| `RecognitionsPayload` | `corecurriculum:recognitions` |
