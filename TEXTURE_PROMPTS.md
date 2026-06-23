# CloudCraft 贴图生成 Prompt

这份文件只整理**当前代码实际需要**或已经接入资源管线的贴图 prompt，方便直接丢给图片生成工具。

## 1. 统一风格基线

所有贴图都尽量遵守这组总提示词，再叠加各自的单独 prompt。

### 全局风格 Prompt

```text
Minecraft mod pixel art texture, clean readable silhouette, high contrast, soft cloud-tech fantasy style, bright sky palette, white/cyan/silver/light gray with small gold or storm-purple accents when needed, whimsical but functional, no background, centered composition, game-ready item icon or seamless block texture, avoid blurry details, avoid photorealism, avoid text, avoid watermark, readable at small size.
```

### 输出规格

- 普通 item / block 贴图：`16x16`
- `gas_state_converter` 各分件贴图：建议先做 `32x32`
- mob effect 图标：`18x18` 或先做 `32x32` 再缩
- 透明背景
- 尽量单物体居中，不要复杂场景背景

---

## 2. 当前 mod 真正需要的贴图清单

### A. 消耗品与基础材料

#### `textures/item/cloud.png`
用途：可食用云朵

```text
Minecraft item texture, fluffy edible cloud puff, soft white cloud bun, slightly glossy and airy, rounded silhouette, subtle pale blue shadowing underneath, cute and simple, no container, transparent background, 16x16 pixel art.
```

#### `textures/item/cumulus_cloud_fragment.png`
用途：基础云碎片

```text
Minecraft item texture, small broken chunk of cumulus cloud, bright white soft fragment with faint sky-blue rim light, looks lightweight and slightly crystalline, compact readable silhouette, transparent background, 16x16 pixel art.
```

#### `textures/item/compressed_canister.png`
用途：压缩空气核心材料

```text
Minecraft item texture, compact compressed air canister, small metallic cylinder with silver body, cyan pressure gauge glow, cloud-tech fantasy design, readable top cap and valve, transparent background, 16x16 pixel art.
```

#### `textures/item/cirrus_filament.png`
用途：高机动材料

```text
Minecraft item texture, cirrus filament, thin airy fiber bundle made of icy-white cloud strands, slight cyan shimmer, elegant lightweight shape, delicate but readable at tiny size, transparent background, 16x16 pixel art.
```

#### `textures/item/storm_core.png`
用途：高压材料

```text
Minecraft item texture, storm core, dense floating energy orb with white center, storm-purple and electric-blue shell, tiny lightning accents inside, powerful unstable cloud-tech material, transparent background, 16x16 pixel art.
```

---

### B. 喷气背包升级件

#### `textures/item/basic_jetpack_frame.png`
用途：基础喷气背包框架

```text
Minecraft item texture, basic jetpack frame, lightweight metallic harness frame, silver and white structure with simple straps and cloud-tech tubing, unfinished equipment component, transparent background, 16x16 pixel art.
```

#### `textures/item/stabilized_nozzle.png`
用途：稳定喷口升级件

```text
Minecraft item texture, stabilized nozzle, precision air-thrust nozzle component, silver metal with cyan vent lines, aerodynamic shape, compact upgrade part, transparent background, 16x16 pixel art.
```

#### `textures/item/high_pressure_chamber.png`
用途：高压腔体升级件

```text
Minecraft item texture, high pressure chamber, reinforced pressure tank module, thick metallic shell, white-silver body with gold clamps and storm-blue energy seam, heavy advanced upgrade component, transparent background, 16x16 pixel art.
```

---

### C. 三档喷气背包

#### `textures/item/cloud_jetpack.png`
用途：基础喷气背包

```text
Minecraft item texture, simple cloud jetpack, small white-silver backpack with one compact air tank and soft cyan exhaust ports, beginner cloud-tech flight gear, friendly silhouette, transparent background, 16x16 pixel art.
```

#### `textures/item/stabilized_cloud_jetpack.png`
用途：稳定版喷气背包

```text
Minecraft item texture, upgraded stabilized cloud jetpack, refined white-silver backpack with dual balanced nozzles, cyan airflow highlights, slightly sleeker and more advanced than a basic jetpack, transparent background, 16x16 pixel art.
```

#### `textures/item/high_pressure_cloud_jetpack.png`
用途：高压版喷气背包

```text
Minecraft item texture, advanced high-pressure cloud jetpack, reinforced multi-tank backpack, white silver chassis with storm-blue glow and subtle purple energy accents, stronger heavier silhouette, endgame cloud-tech mobility gear, transparent background, 16x16 pixel art.
```

---

### D. 六类云方块：固态版

这 6 张都要做成**可平铺 seamless block texture**。

#### `textures/block/cloud_block/cumulus_cloud_block.png`

```text
Minecraft seamless block texture, cumulus cloud block, fluffy dense white cloud mass with soft rounded volume, bright highlights and pale blue shadows, soft and welcoming sky texture, 16x16 pixel art.
```

#### `textures/block/cloud_block/stratus_cloud_block.png`

```text
Minecraft seamless block texture, stratus cloud block, flatter layered gray-white cloud texture, calm horizontal streaks, soft muted atmosphere, slightly denser than cumulus, 16x16 pixel art.
```

#### `textures/block/cloud_block/cirrus_cloud_block.png`

```text
Minecraft seamless block texture, cirrus cloud block, thin wispy icy-white cloud texture, stretched feather-like strands, airy and high-altitude feel, cyan-tinted highlights, 16x16 pixel art.
```

#### `textures/block/cloud_block/altostratus_cloud_block.png`

```text
Minecraft seamless block texture, altostratus cloud block, broad mid-altitude cloud sheet, layered white-gray texture with soft smooth banding, denser and cooler than stratus, 16x16 pixel art.
```

#### `textures/block/cloud_block/nimbostratus_cloud_block.png`

```text
Minecraft seamless block texture, nimbostratus cloud block, rain-heavy dark gray cloud sheet, moist diffuse shading, subdued blue-gray undertone, heavy weather feel, 16x16 pixel art.
```

#### `textures/block/cloud_block/cumulonimbus_cloud_block.png`

```text
Minecraft seamless block texture, cumulonimbus cloud block, towering storm cloud texture, dense dark layered white-gray mass with subtle storm-blue and purple depth, energetic and dangerous mood, 16x16 pixel art.
```

---

### E. 六类云方块：气态版

气态版建议仍做成**可平铺 seamless block texture**，但更通透、更像流动云雾。

#### `textures/block/cloud_block/cumulus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous cumulus cloud, light translucent white mist, soft swirls and airy pockets, brighter center and diffused edges, 16x16 pixel art.
```

#### `textures/block/cloud_block/stratus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous stratus cloud, drifting flat mist bands, pale gray-white fog layers, calm horizontal motion feel, 16x16 pixel art.
```

#### `textures/block/cloud_block/cirrus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous cirrus cloud, thin flowing feather-like vapor strands, icy white and cyan highlights, fast high-altitude feeling, 16x16 pixel art.
```

#### `textures/block/cloud_block/altostratus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous altostratus cloud, cool smooth vapor sheet with soft layered motion, mid-altitude mist, slightly blue-gray, 16x16 pixel art.
```

#### `textures/block/cloud_block/nimbostratus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous nimbostratus cloud, rain-heavy dark mist, diffuse smoky gray with blue undertone, damp storm atmosphere, 16x16 pixel art.
```

#### `textures/block/cloud_block/cumulonimbus_cloud_block_gas.png`

```text
Minecraft seamless block texture, gaseous cumulonimbus cloud, violent storm vapor with dark gray, storm-blue and faint purple energy veins, dangerous charged mist, 16x16 pixel art.
```

---

### F. 气态转换器 `gas_state_converter`

这个方块不是一张图，而是多张分件贴图，建议全部按统一美术主题生成：
- 云科技装置
- 白银金属框架
- 半透明玻璃
- 内腔有气流/云雾/压缩空气元素
- 轻微青蓝发光

#### `textures/block/gas_state_converter/pedestal.png`

```text
Minecraft block texture, machine pedestal base, white-silver cloud-tech metal platform, sturdy but elegant, subtle cyan lines, seamless enough for block model face use, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/inside.png`

```text
Minecraft block texture, machine inner chamber texture, swirling compressed cloud vapor inside a device, pale white mist with cyan glow, magical industrial cloud-tech feeling, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/right.png`

```text
Minecraft block texture, machine side panel, right-side white-silver cloud-tech casing with vents and panel seams, slightly asymmetric mechanical detailing, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/left.png`

```text
Minecraft block texture, machine side panel, left-side white-silver cloud-tech casing with vents and panel seams, matching the opposite side but not mirrored too perfectly, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/top.png`

```text
Minecraft block texture, machine top plate, cloud-tech top surface with circular pressure hatch and subtle cyan energy ring, metallic white-silver finish, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/baffle.png`

```text
Minecraft block texture, internal machine baffle or fin assembly, compact metallic airflow divider, silver with cyan highlights, industrial cloud-tech detail, 32x32 pixel art.
```

#### `textures/block/gas_state_converter/glass.png`

```text
Minecraft block texture, translucent machine glass, slightly tinted cyan glass panel with soft reflections and cloudy diffusion, transparent areas preserved, 32x32 pixel art.
```

---

### G. 药水与效果图标

#### `textures/item/potion_bottle_solid_cloud.png`
用途：固态云药水瓶身图标

```text
Minecraft item texture, potion bottle for solid cloud effect, classic Minecraft-style potion bottle with milky white cloud liquid and faint cyan tint, whimsical airy magic potion, transparent background, 16x16 pixel art.
```

#### `textures/mob_effect/cloud_walker.png`
用途：Cloud Walker 效果图标

```text
Minecraft status effect icon, cloud walker, small white boot stepping on a soft cloud with pale cyan accent, clean readable symbol, transparent background, 18x18 pixel art.
```

---

## 3. 现在不急着重做的贴图

这些资源仓库里已经有，但**当前定义文件没有注册到正式玩法主线**，所以不是这轮必须生成：

- `textures/item/cumulus_cloud_helmet.png`
- `textures/item/cumulus_cloud_chestplate.png`
- `textures/item/cumulus_cloud_leggings.png`
- `textures/item/cumulus_cloud_boots.png`

如果后面你要正式接盔甲线，再单独给它们做一组统一 prompt 更合适。

---

## 4. 推荐给图片模型的附加负面提示词

```text
no realistic rendering, no 3d render look, no painterly brushwork, no busy background, no text, no watermark, no UI frame, no multiple items in one image, no blurry edges, no low contrast silhouette
```
