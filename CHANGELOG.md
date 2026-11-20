# Changelog

Notable changes to Mystical Cloched Seeds are documented here.

## [1.1.0-beta.1] - Unreleased

**Tier 6 crops, Cruxed Farmlands, and fully optional Mystical Agradditions integration.**

### Added

- Six original **Cruxed Farmland** blocks: Nether Star, Dragon Egg, Gaia Spirit, Awakened Draconium, Neutronium, and Nitro Crystal.
- Six Garden Cloche recipes for the corresponding Tier 6 crops, each requiring its matching Cruxed Farmland.
- Awakening Altar recipes for all six Cruxed Farmlands, using the corresponding Crux, Supremium Farmland, and Insanium Essence.
- Support for growing matching Tier 6 crops naturally on Cruxed Farmland without a separate Crux block beneath them.
- Block models, textures, item representations, loot tables, and English/Portuguese translations for the new farmlands.
- Automated tests for conditional registration rules and optional recipe dependencies.

### Improved

- Mystical Agradditions is now **fully optional**: no Cruxed Farmland blocks, items, or Creative Tab are registered when it is absent.
- Individual optional integrations are activated only when their required mods are loaded: Botania, Draconic Evolution, Avaritia, and Powah.
- Awakening Altar and Garden Cloche recipes have matching mod-loading conditions.
- Centralized compatibility rules to reduce duplicate mappings and simplify future additions.
- Preserved original crop growth behavior when the matching Cruxed Farmland is not used.
- Added a Gradle development flag to run the client without Mystical Agradditions.

### Compatibility and testing

- Minecraft **1.21.1**, NeoForge **21.1.240+**.
- Immersive Engineering **12.4.2+** and Mystical Agriculture **8.0.16+** are required.
- Mystical Agradditions **8.0.14+** is optional.
- Validated builds and automated tests, including recipe integrity and conditional mod availability.
- Manually tested gameplay with and without Mystical Agradditions, including Tier 6 farmland functionality.

### Testing still recommended

- Dedicated-server loading and operation.
- Mystical Agriculture versions introducing `requiresEffectiveFarmland`, particularly when that setting is enabled.

This is a **beta pre-release**. Please report bugs and compatibility issues on the [issue tracker](https://github.com/brennocoelho/MysticalClochedSeeds/issues).

## [1.1.0-alpha.1] - 2026-11-16

**Initial optional Mystical Agradditions compatibility.**

### Added

- Optional Mystical Agradditions **8.0.14+** integration.
- **134 Garden Cloche recipes** supporting Insanium Farmland with existing Tier 1–5 Mystical Agriculture crops.
- Original 16×16 Insanium Farmland texture for Garden Cloche rendering.
- Tier-aware growth calculations extended to Tier 6 farmland.

### Improved

- Conditional recipe loading when Mystical Agradditions is absent.
- NeoForge dependency metadata and compatibility documentation.
- Generated recipe resources included in the repository for reproducible builds.

### Limitation at release

Tier 6 seeds did not yet have dedicated Garden Cloche recipes. This is addressed by `1.1.0-beta.1`.

[1.1.0-alpha.1]: https://github.com/brennocoelho/MysticalClochedSeeds/releases/tag/v1.1.0-alpha.1
