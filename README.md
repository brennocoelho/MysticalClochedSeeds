# Mystical Cloched Seeds

A compatibility mod that integrates **Mystical Agriculture** seeds and farmlands
with the **Immersive Engineering** Garden Cloche for Minecraft 1.21.1 (NeoForge).

## What it does

Out of the box, the Immersive Engineering Garden Cloche has no compatibility with
Mystical Agriculture — it accepts none of the mod's seeds or farmlands.

This mod is a fork of **Mystical Engineering** by DragonBlak, which added Cloche
support for Mystical Agriculture but limited each seed to the Essence Farmland of
its exact tier. This fork lifts that restriction and reproduces the mechanics
described in the Mystical Agriculture wiki:

- **Any farmland works.** A seed can be grown in the Cloche on vanilla farmland
  or on any Essence Farmland, regardless of tier.
- **Growth time scales with the tier difference.** Using an Essence Farmland of
  equal or higher tier than the seed reduces growth time; using a lower-tier soil
  applies a penalty. Vanilla farmland is the baseline.
- **Second-seed chance.** Essence Farmland grants a chance to drop a second seed
  on harvest (higher when the farmland matches the seed's tier).
- **Fertilized Essence.** Every harvest has a chance to drop Fertilized Essence.
- **Inferium output bonus.** Inferium yields scale up when grown on higher-tier
  farmland, following the wiki's per-tier bonus.

## Requirements

This mod does nothing on its own. It requires the following mods to be installed:

- [Immersive Engineering](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering)
- [Mystical Agriculture](https://www.curseforge.com/minecraft/mc-mods/mystical-agriculture)
- Cucumber Library (dependency of Mystical Agriculture)

The mod loader will refuse to start and show a clear error if Immersive
Engineering or Mystical Agriculture are missing.

## Credits & Attribution

This project is a **fork** of **Mystical Engineering** by **DragonBlak**, which
laid the groundwork for Garden Cloche / Mystical Agriculture compatibility.

- Original project (GitHub): https://github.com/DragonBlak/Mystical-Engineering
- Original project (CurseForge): https://www.curseforge.com/minecraft/mc-mods/mystical-engineering

All original work is credited to DragonBlak under the MIT License. This fork
expands the compatibility logic and reworks the recipe generation.

## License

Released under the [MIT License](LICENSE). Copyright (c) 2023 DragonBlak and
(c) 2026 DonCoelho.
