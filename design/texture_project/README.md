# CloudCraft Texture Project

Editable AgentPaint sources for CloudCraft hand-authored textures.

Note: the repository currently ignores `design/`, so this folder is a local art project workspace unless `.gitignore` is changed later.

## Source layout

- `source/item/`: 16x16 inventory item icons.
- `source/block/cloud_block/`: 16x16 seamless-ish cloud block textures.
- `source/block/gas_state_converter/`: model part textures imported from the original tracked PNGs.
- `source/mob_effect/`: 18x18 status effect icons.
- `reference/original/`: original tracked PNGs used as conversion/import references.

## Export targets

Rendered PNG files are exported to:

- `common/src/main/resources/assets/cloudcraft/textures/item/`
- `common/src/main/resources/assets/cloudcraft/textures/block/cloud_block/`
- `common/src/main/resources/assets/cloudcraft/textures/block/gas_state_converter/`
- `common/src/main/resources/assets/cloudcraft/textures/mob_effect/`

Supersampled review previews live under `preview/` and are not game assets.

## Style

- Minecraft-readable pixel art.
- Clean icon silhouettes for items.
- Reusable tile texture for cloud blocks.
- White, cyan, silver, light gray, gold, and storm-purple accents.
- Transparent background for item icons and effect icons.
