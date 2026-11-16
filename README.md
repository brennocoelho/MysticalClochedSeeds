# Mystical Cloched Seeds

[![Build](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml/badge.svg)](https://github.com/brennocoelho/MysticalClochedSeeds/actions/workflows/build.yml)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A)
![Mod Loader](https://img.shields.io/badge/Mod%20Loader-NeoForge-EA5B23)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Bring Mystical Agriculture crops to the Immersive Engineering Garden Cloche.**

Mystical Cloched Seeds is a compatibility mod for **Minecraft 1.21.1 (NeoForge)**. It adds Garden Cloche recipes for Mystical Agriculture seeds and supports vanilla soil and all five tiers of Essence Farmland, with optional support for Insanium Farmland (Tier 6) from Mystical Agradditions. Growth times and harvest rewards are calculated according to seed and soil tiers.

## Features

- **Flexible soil selection:** grow supported Mystical Agriculture seeds using **Dirt** (the Cloche's vanilla soil) or **Inferium, Prudentium, Tertium, Imperium, and Supremium Farmland**.
- **Tier-aware growth:** growth time changes based on the difference between the seed tier and the soil tier. Lower-tier soil is slower; matching or higher-tier Essence Farmland improves growth time.
- **Bonus seed drops:** Essence Farmland can produce an additional seed, with a higher chance when the farmland matches the crop's tier.
- **Fertilized Essence drops:** harvests have a chance to produce Fertilized Essence.
- **Inferium yield bonuses:** higher-tier farmland can increase Inferium Essence output.
- **Optional Mystical Agradditions compatibility (Alpha):** Insanium Farmland can be used with supported Tier 1–5 seeds, preserving tier-aware growth calculations and harvest rewards.

The compatibility recipes are generated from Mystical Agriculture's crop registry, using the mod's tier-based calculations.

## Requirements

| Component | Requirement                                          |
| --- |------------------------------------------------------|
| Minecraft | **1.21.1**                                           |
| NeoForge | **21.1.235+**                                        |
| Immersive Engineering | **12.4.2+**                                          |
| Mystical Agriculture | **8.0.16+**                                          |
| Cucumber Library | **8.0.12** *(development version)*                   |
| Mystical Agradditions | **8.0.14** *(optional, for Tier 6 farmland support)* |
| Java | **21**                                               |

> This is an **addon**, not a standalone mod. Both Immersive Engineering and Mystical Agriculture must be installed.

> **Alpha limitation:** Mystical Agradditions Tier 6 seeds are not currently supported in the Garden Cloche. These crops require their respective Crux blocks. Future updates will introduce specialized Cruxed Farmlands crafted through the Mystical Agriculture Infusion Altar.

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
- [x] **Original texture replacement** — Replaced all five Minecraft-derived farmland textures with original 16×16 pixel-art assets, tested successfully in the Garden Cloche.
- [ ] **Repository and release cleanup** — Review and remediate historical commits and distributed artifacts containing Minecraft-derived textures.
- [ ] **Dynamic farmland tinting** — Reproduce Mystical Agriculture's runtime tinting behavior using Minecraft's existing farmland texture, without redistributing it.
- [ ] **Temporary asset removal** — Remove the custom farmland PNGs once dynamic rendering is stable.

### Planned Mod Compatibility

- [x] **Mystical Agradditions — Initial Alpha compatibility** — Added optional Insanium Farmland support for Tier 1–5 crops.
- [x] **Insanium Farmland rendering** — Added an original 16×16 texture for the Garden Cloche.
- [ ] **Tier 6 crop compatibility** — Implement six specialized Cruxed Insanium Farmlands with Infusion Altar crafting recipes.
- [ ] **Custom infusion effects** — Introduce unique animations, particles, and sounds for Cruxed Farmland crafting.

### Farmland Texture Rendering

Mystical Cloched Seeds supports colored Essence Farmland rendering inside the Garden Cloche.

The current implementation uses five custom 16×16 pixel-art textures, independently created to represent the Inferium, Prudentium, Tertium, Imperium, and Supremium farmland tiers.

These textures replace the previous Minecraft-derived assets and preserve the visual characteristics of Mystical Agriculture's farmland without redistributing Mojang's original texture artwork.

**Future improvement:** The long-term goal is to reproduce Mystical Agriculture's dynamic tinting system by referencing Minecraft's existing farmland texture at runtime. Once implemented, this will eliminate the need for separate pre-colored PNG textures.

**Note:** Farmland rendering is purely visual and does not affect crop growth calculations, harvest rewards, or soil-tier compatibility.

## Contributing

Bug reports, feature requests, and contributions are welcome! You can [open an issue](https://github.com/brennocoelho/MysticalClochedSeeds/issues) or [submit a pull request](https://github.com/brennocoelho/MysticalClochedSeeds/pulls).

## Credits & Attribution

Mystical Cloched Seeds is based on **[Mystical Engineering](https://github.com/DragonBlak/Mystical-Engineering)** by **DragonBlak** ([CurseForge page](https://www.curseforge.com/minecraft/mc-mods/mystical-engineering)), which originally introduced Mystical Agriculture compatibility for the Immersive Engineering Garden Cloche.

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
