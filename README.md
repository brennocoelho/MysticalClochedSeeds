# Mystical Cloched Seeds

[![Build](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml/badge.svg)](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A)
![Mod Loader](https://img.shields.io/badge/Mod%20Loader-NeoForge-EA5B23)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Bring Mystical Agriculture crops to the Immersive Engineering Garden Cloche.**

Mystical Cloched Seeds is a compatibility mod for **Minecraft 1.21.1 (NeoForge)**. It adds Garden Cloche recipes for Mystical Agriculture seeds and supports vanilla soil and all five tiers of Essence Farmland, with tier-aware growth times and harvest rewards.

## Features

- **Flexible soil selection:** grow supported Mystical Agriculture seeds using **Dirt** (the Cloche's vanilla soil) or **Inferium, Prudentium, Tertium, Imperium, and Supremium Farmland**.
- **Tier-aware growth:** growth time changes based on the difference between the seed tier and the soil tier. Lower-tier soil is slower; matching or higher-tier Essence Farmland improves growth time.
- **Bonus seed drops:** Essence Farmland can produce an additional seed, with a higher chance when the farmland matches the crop's tier.
- **Fertilized Essence drops:** harvests have a chance to produce Fertilized Essence.
- **Inferium yield bonuses:** higher-tier farmland can increase Inferium Essence output.

The compatibility recipes are generated from Mystical Agriculture's crop registry, using the mod's tier-based calculations.

## Requirements

| Component | Requirement |
| --- | --- |
| Minecraft | **1.21.1** |
| NeoForge | **21.1.235+** |
| Immersive Engineering | **12.4.2+** |
| Mystical Agriculture | **8.0.16+** |
| Cucumber Library | **8.0.12 *(development version)*** |
| Java | **21** |

> This is an **addon**, not a standalone mod. Both Immersive Engineering and Mystical Agriculture must be installed.

> **Compatibility warning:** Mystical Cloched Seeds is incompatible with **Mystical Engineering**. Both mods generate Garden Cloche recipes for Mystical Agriculture crops, which may result in conflicting recipes. Install only one of them.

## Installation

1. Set up **Minecraft 1.21.1** with **NeoForge**.
2. Install **[Immersive Engineering](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering)**, **[Mystical Agriculture](https://www.curseforge.com/minecraft/mc-mods/mystical-agriculture)**, and its required dependency, **[Cucumber Library](https://www.curseforge.com/minecraft/mc-mods/cucumber)**.
3. Place the **Mystical Cloched Seeds** `.jar` file in your Minecraft `mods` folder.
4. Launch the game and use Mystical Agriculture seeds with supported soil in the **Garden Cloche**.

### Building from source

This project uses **Java 21** and the Gradle wrapper.

```bash
git clone https://github.com/brennocoelho/MysticalClochedSeeds.git
cd MysticalClochedSeeds
./gradlew build
```

On Windows, run `gradlew.bat build` instead. The resulting mod JAR will be in `build/libs/`.

## Roadmap / To-Do

### Texture Rendering & Asset Replacement

- [x] **Initial Essence Farmland rendering** — Implement colored farmland textures in the Garden Cloche using temporary PNG assets.
- [ ] **Original texture replacement** — Replace the five Minecraft-derived farmland textures with independently created artwork.
- [ ] **Repository and release cleanup** — Review and remediate historical commits and distributed artifacts containing Minecraft-derived textures.
- [ ] **Dynamic farmland tinting** — Reproduce Mystical Agriculture's runtime tinting behavior using Minecraft's existing farmland texture, without redistributing it.
- [ ] **Temporary asset removal** — Remove the custom farmland PNGs once dynamic rendering is stable.

### Planned Mod Compatibility

- [ ] **Mystical Agradditions compatibility** — Add Garden Cloche support for Mystical Agradditions seeds and farmlands. Requested in [Issue #1](https://github.com/brennocoelho/MysticalClochedSeeds/issues/1).
- [ ] **Mystical Agradditions farmland rendering** — Extend the farmland rendering system to support Mystical Agradditions soil colors.

### Farmland Texture Rendering

As of version **1.0.2**, Mystical Cloched Seeds supports colored Essence Farmland rendering inside the Garden Cloche.

The current implementation uses five pre-colored PNG textures derived from Minecraft's vanilla `farmland_moist` texture. These assets are temporary and are scheduled to be replaced with independently created artwork.

The long-term goal is to reproduce Mystical Agriculture's dynamic tinting system, referencing Minecraft's existing farmland texture at runtime without redistributing it.

**Note:** Farmland rendering is purely visual and does not affect crop growth calculations, harvest rewards, or soil-tier compatibility.

## Contributing

Bug reports, feature requests, and contributions are welcome! You can [open an issue](https://github.com/brennocoelho/MysticalClochedSeeds/issues) or [submit a pull request](https://github.com/brennocoelho/MysticalClochedSeeds/pulls).

## Credits & Attribution

Mystical Cloched Seeds is based on **[Mystical Engineering](https://github.com/DragonBlak/Mystical-Engineering)** by **DragonBlak** ([CurseForge page](https://www.curseforge.com/minecraft/mc-mods/mystical-engineering)), which originally introduced Mystical Agriculture compatibility for the Immersive Engineering Garden Cloche.

This project builds on that foundation by expanding farmland compatibility and reworking recipe generation and tier-based behavior. Credit for the original work remains with DragonBlak.

### Minecraft Texture Attribution

The five Essence Farmland textures currently included in version **1.0.2** are recolored derivatives of Minecraft's original `farmland_moist` texture, created by **Mojang Studios**.

These textures were temporarily adapted to reproduce Mystical Agriculture's farmland colors inside the Garden Cloche. They are scheduled to be replaced with independently created artwork, followed by an implementation of dynamic texture tinting.

Minecraft and its original assets remain the intellectual property of Mojang Studios. Mystical Cloched Seeds is an independent community project and is not affiliated with, endorsed by, or approved by Mojang Studios or Microsoft.

## License

The project's source code is distributed under the [MIT License](LICENSE), retaining the original attribution and copyright notices.

Copyright © 2025 DragonBlak and © 2026 Brenno Coelho (DonCoelho).

**Third-party assets:** Minecraft-derived textures are not offered under this project's MIT License. Their inclusion does not grant any additional rights to use or redistribute Mojang's intellectual property. Attribution does not constitute permission from the original copyright holder.

For additional information, see the [Minecraft EULA](https://www.minecraft.net/en-us/eula) and [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/usage-guidelines).
