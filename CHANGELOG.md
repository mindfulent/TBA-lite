# TBA-Lite Changelog

## v1.0.1 — 2026-04-19

Compatibility release — restores server connection after the full TBA modpack added ShapeCraft.

**151 mods** (unchanged)

### Fixed
- **Server connection rejected** with `Received 129 registry entries that are unknown to this client` when joining the TBA server. Caused by ShapeCraft being added to TBA (v1.0.0+), which registers 64 custom blocks, 64 items, and a block entity type that TBA-Lite clients don't have. Connection now succeeds — ShapeCraft blocks placed by full-TBA players render as missing-texture (purple/black) but no longer block gameplay.

### Changed
- **tba-lite-stubs** 0.1.0 → 0.2.0 — Now also stubs ShapeCraft's registry surface: 64 pool blocks, 64 block items, `pool_block_entity`, and 9 network payloads (3 C2S + 6 S2C). Added `shapecraft` to the `provides` list.
- **Distant Horizons** 2.4.5-b → 3.0.1-b — Synced from full TBA.

### Notes
TBA-Lite remains feature-parity with its scope: same server as full TBA players, but cannot author ShapeCraft blocks, generate music (SynthCraft), stream video (StreamCraft), record highlights (SceneCraft), or submit builds (CoreCurriculum). Install full TBA for those features.

## v0.9.99 — 2026-03-02

Synced with TBA v0.9.99. Added 4 new mods.

**151 mods** (up from 147)

### Added
- **Mighty Mail** 1.1.4 — In-game mailbox system for sending items and letters between players (+ Framework 0.13.11 library)
- **Cubes Without Borders** 3.0.0 — Borderless fullscreen windowed mode
- **Immersive Aircraft** 1.4.2 — Craftable biplanes, airships, and gyrodyne helicopters

### Updated
- **Connectible Chains** 2.5.5 → 2.5.7

## v0.9.98 — 2026-02-16

Synced with TBA v0.9.98. Removed two mods flagged by Modrinth for redistribution policy.

**147 mods** (down from 149)

### Removed
- **Cupboard** — Library mod (CurseForge-only, not permitted as Modrinth override)
- **Connectivity** — Network connection fixes (CurseForge-only, not permitted as Modrinth override)

Both mods were flagged by Modrinth's AutoMod as copyrighted content that cannot be redistributed as overrides. Neither is available on Modrinth. CurseForge submission was unaffected.

## v0.9.96 — 2026-02-16

Initial release of TBA-Lite, a lightweight variant of the TBA modpack for players with lower-end machines. Connects to the same TBA server as the full modpack.

**149 mods** (down from 190 in full TBA)

### How It Works
- **tba-lite-stubs v0.1.0** — A ~90 KB stub mod that registers all 34 custom payload types and the StreamCraft display block, replacing StreamCraft, SynthCraft, SceneCraft, and CoreCurriculum
- Players on TBA-Lite can join the same server as full TBA players
- Custom mod features (video streaming, music generation, cinematic recording, build submissions) are unavailable but don't block gameplay

### Mods Removed (42)

**Visual/Cosmetic (23):**
AmbientSounds, Auto HUD, BetterF3, Blur, Camera Overhaul, Cool Rain, Do a Barrel Roll, Eating Animation, Entity Model Features, Entity Texture Features, Falling Leaves, First Person Model, Inventory Particles, Leaf Me Alone, Not Enough Animations, Particle Rain, Presence Footsteps, Skin Layers 3D, Sound Physics Remastered, Stable Cam, Visuality, WaterVision, PlayerAnimator

**Utility (13):**
Controlling, ItemSwapper, Litematica, MaLiLib, Mod Menu, Mouse Wheelie, Paginated Advancements, Roughly Enough Items, Simple Discord RPC, Smooth Swapping, Status Effect Bars, WTHIT, Searchables

**Recording (1):**
Replay Mod

**Library (1):**
MidnightLib

**Custom Mods → Stub (4):**
StreamCraft, SynthCraft, SceneCraft, CoreCurriculum

### Shaders Removed (2)
Complementary Reimagined, Photon (kept BSL + Solas)
