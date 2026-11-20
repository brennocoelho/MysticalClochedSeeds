# Mystical Cloched Seeds

[![Build](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml/badge.svg)](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A)
![Mod Loader](https://img.shields.io/badge/Mod%20Loader-NeoForge-EA5B23)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Grow Mystical Agriculture crops in the Immersive Engineering Garden Cloche — now with optional Tier 6 compatibility.**

Mystical Cloched Seeds is a compatibility addon for **Minecraft 1.21.1 (NeoForge)**. It adds Garden Cloche recipes for Mystical Agriculture seeds, supports multiple farmland tiers, and adjusts growth times and harvest rewards according to seed and soil tiers.

With **Mystical Agradditions** installed, the addon also supports **Insanium Farmland** for Tier 1–5 crops and introduces six specialized **Cruxed Farmlands** for Tier 6 crops. Mystical Agradditions is fully optional: the base Garden Cloche recipes continue to work without it.

## Features

### Garden Cloche compatibility

- **Flexible soil selection:** grow supported Mystical Agriculture seeds using **Dirt** or **Inferium, Prudentium, Tertium, Imperium, and Supremium Farmland**.
- **Tier-aware growth:** growth time changes according to the difference between seed tier and soil tier. Lower-tier soil is slower; matching or higher-tier Essence Farmland improves growth time.
- **Bonus seed drops:** Essence Farmland can produce an additional seed, with a higher chance when farmland and crop tiers match. Tier 6 crops do not receive secondary seed drops.
- **Fertilized Essence:** compatible crops have a chance to produce Fertilized Essence.
- **Inferium yield bonuses:** higher-tier farmland can increase Inferium Essence output.
- **Insanium Farmland (optional):** when Mystical Agradditions is installed, Tier 1–5 crops gain Garden Cloche recipes using Insanium Farmland.

Garden Cloche compatibility recipes are generated from Mystical Agriculture's crop registry and use the addon's tier-based calculations.

### Tier 6 crops and Cruxed Farmland (optional)

Six specialized Cruxed Farmlands allow their corresponding Tier 6 crops to grow in the Garden Cloche and in the world **without requiring a separate Crux block underneath the farmland**.

| Cruxed Farmland | Additional mod required |
| --- | --- |
| Nether Star | None beyond Mystical Agradditions |
| Dragon Egg | None beyond Mystical Agradditions |
| Gaia Spirit | Botania |
| Awakened Draconium | Draconic Evolution |
| Neutronium | Avaritia |
| Nitro Crystal | Powah |

**Mystical Agradditions is required for all six integrations.** Additional farmlands, items, Creative Tab entries, and recipes are enabled only when their relevant mods are installed. Without Mystical Agradditions, none of the six Cruxed Farmlands is registered.

Each Cruxed Farmland:

- Is crafted through the **Mystical Agriculture Awakening Altar**, using the corresponding Crux, four Supremium Farmlands, and Insanium Essence in four Essence Vessels (16 per vessel).
- Acts as the matching Tier 6 crop's Garden Cloche soil.
- Supports natural crop growth without the separate Crux-block requirement; the crop's other native growth checks remain in effect.
- Has its own block and item assets.

The special farmland must match the crop. An unrelated Cruxed Farmland does not satisfy the crop's requirement. The original farmland-and-Crux arrangement remains supported.

## Requirements

| Component | Requirement |
| --- | --- |
| Minecraft | **1.21.1** |
| Mod loader | **NeoForge 21.1.240+** |
| Immersive Engineering | **12.4.2+** |
| Mystical Agriculture | **8.0.16+** |
| Cucumber Library | Required by Mystical Agriculture; use a compatible version *(8.0.12 in the current development setup)* |
| Mystical Agradditions | **8.0.14+** *(optional)* |
| Java | **21** |

> This is an **addon**, not a standalone mod. Immersive Engineering and Mystical Agriculture are required.

> **Compatibility warning:** Mystical Cloched Seeds is incompatible with **Mystical Engineering**. Both mods provide Garden Cloche recipes for Mystical Agriculture crops, which may conflict. Install only one of them.

> **Beta testing note:** The project has been tested with and without Mystical Agradditions using the dependency versions listed in `gradle.properties`. Compatibility with Mystical Agriculture's newer `requiresEffectiveFarmland = true` setting has not yet been verified.

## Installation

1. Install **Minecraft 1.21.1** with a compatible version of **NeoForge**.
2. Install [Immersive Engineering](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering), [Mystical Agriculture](https://www.curseforge.com/minecraft/mc-mods/mystical-agriculture), and its required [Cucumber Library](https://www.curseforge.com/minecraft/mc-mods/cucumber).
3. Optionally install [Mystical Agradditions](https://www.curseforge.com/minecraft/mc-mods/mystical-agradditions) and any additional integration mods you want to use.
4. Place the **Mystical Cloched Seeds** `.jar` in your Minecraft `mods` folder.
5. Launch the game and use compatible seeds and farmland in the **Garden Cloche**.

### Building from source

This project uses **Java 21** and the Gradle wrapper.

```bash
git clone https://github.com/brennocoelho/MysticalClochedSeeds.git
cd MysticalClochedSeeds
./gradlew build
```

On Windows, use `gradlew.bat build`. The resulting JAR is placed in `build/libs/`.

To launch a development client **without** Mystical Agradditions:

```powershell
.\gradlew.bat runClient -PwithoutAgradditions=true
```

This flag affects the local development runtime only; it does not change the published JAR.

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for release history, including the Tier 6 beta changes.

## Roadmap / To-Do

### Texture Rendering & Asset Replacement

- [x] **Initial Essence Farmland rendering** — Added colored farmland textures for Garden Cloches.
- [x] **Original texture replacement** — Replaced the five Minecraft-derived farmland textures with original 16×16 pixel-art assets.
- [ ] **Repository and release cleanup** — Review and remediate historical commits and distributed artifacts containing Minecraft-derived textures.
- [ ] **Dynamic farmland tinting** — Reproduce Mystical Agriculture's runtime tinting using existing farmland textures without redistributing them.
- [ ] **Temporary asset removal** — Remove separately colored farmland PNGs once dynamic rendering is stable.

### Mod Compatibility

- [x] **Initial Mystical Agradditions support** — Insanium Farmland recipes for Tier 1–5 crops.
- [x] **Insanium Farmland rendering** — Added an original 16×16 Garden Cloche texture.
- [x] **Tier 6 crop compatibility** — Added six Cruxed Farmlands, Garden Cloche recipes, and Awakening Altar crafting recipes.
- [x] **Optional-content registration** — Register Cruxed Farmlands only when the required mods are loaded.
- [ ] **Custom crafting effects** — Consider unique animations, particles, and sounds for Cruxed Farmland crafting.
- [ ] **Expanded compatibility testing** — Validate newer Mystical Agriculture settings and dedicated-server environments.

### Farmland Texture Rendering

The addon includes original 16×16 pixel-art textures representing the Essence Farmland tiers. These replace earlier Minecraft-derived farmland assets while preserving a look consistent with Mystical Agriculture.

A future improvement is to reproduce Mystical Agriculture's dynamic tinting system by referencing existing farmland textures at runtime, reducing the need to ship separate pre-colored PNG files. Farmland rendering is visual only and does not affect recipe balance or crop growth calculations.

## Contributing

Bug reports, feature requests, and contributions are welcome! [Open an issue](https://github.com/brennocoelho/MysticalClochedSeeds/issues) or [submit a pull request](https://github.com/brennocoelho/MysticalClochedSeeds/pulls).

## Credits & Attribution

Mystical Cloched Seeds is based on [Mystical Engineering](https://github.com/DragonBlak/Mystical-Engineering) by **DragonBlak** ([CurseForge page](https://www.curseforge.com/minecraft/mc-mods/mystical-engineering)), which originally introduced Mystical Agriculture compatibility for the Immersive Engineering Garden Cloche.

This project builds on that foundation by expanding farmland compatibility and reworking recipe generation and tier-based behavior. Credit for the original work remains with DragonBlak.

### Minecraft Attribution

Minecraft and its original assets are the intellectual property of Mojang Studios and Microsoft.

Mystical Cloched Seeds is an independent, unofficial community project and is not affiliated with, endorsed by, or approved by Mojang Studios or Microsoft.

The custom Essence Farmland textures included in this project were independently created and do not incorporate Minecraft's original texture artwork.

## License

The project's source code is distributed under the [MIT License](LICENSE), retaining the original attribution and copyright notices.

Copyright © 2025 DragonBlak and © 2026 Brenno Coelho (DonCoelho).

Third-party intellectual property remains subject to the rights and terms of its respective owners.

For additional information, see the [Minecraft EULA](https://www.minecraft.net/en-us/eula) and [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/usage-guidelines).
