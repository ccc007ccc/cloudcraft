# CloudCraft Texture Project

Editable AgentPaint sources for CloudCraft hand-authored textures.

This folder is tracked with the repository so APX sources, previews, references, and exported game textures remain traceable.

## Source layout

- `source/item/`: 16x16 inventory item icons.
- `source/block/cloud_block/`: 16x16 seamless-ish cloud block textures.
- `source/block/gas_state_converter/`: model part textures imported from the original tracked PNGs.
- `source/mob_effect/`: 18x18 status effect icons.
- `reference/original/`: original tracked PNGs used as conversion/import references.
- `reference/unregistered_armor/`: cumulus armor art kept as reference only until the armor line is implemented.

## Export targets

Rendered PNG files are exported to:

- `common/src/main/resources/assets/cloudcraft/textures/item/`
- `common/src/main/resources/assets/cloudcraft/textures/block/cloud_block/`
- `common/src/main/resources/assets/cloudcraft/textures/block/gas_state_converter/`
- `common/src/main/resources/assets/cloudcraft/textures/mob_effect/`

Supersampled review previews live under `preview/` and are not game assets.

Unregistered reference art must stay under `reference/` and must not be exported to `common/src/main/resources` until the matching gameplay registration, language keys, recipes, and tests exist.

## Style

- Minecraft-readable pixel art.
- Clean icon silhouettes for items.
- Reusable tile texture for cloud blocks.
- White, cyan, silver, light gray, gold, and storm-purple accents.
- Transparent background for item icons and effect icons.
