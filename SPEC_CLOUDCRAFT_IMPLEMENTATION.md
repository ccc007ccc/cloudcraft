# CloudCraft 实施规格与检查清单

本文把外部审查结论、现有规划和当时代码状态整理成上一阶段可执行 checklist。它记录历史闭环，不代表当前最终玩法目标；新一轮故事、玩法和重构任务以 `CLOUDCRAFT_STORY_AND_GAMEPLAY.md` 与 `SPEC_CLOUDCRAFT_REWORK.md` 为准。

## 0. 取证基线

- [x] 已读取外部 AI 审查文件：`C:\Users\MengChao\.codex\attachments\604192e1-e89e-4720-871c-6e024f3db3f3\pasted-text-1.txt`。
- [x] 已读取长期维度规划：`CLOUD_DIMENSION_PLAN.md`。
- [x] 已读取工程约束：`CLAUDE.md`。
- [x] 已读取贴图规划：`TEXTURE_PROMPTS.md`。
- [x] 已确认项目结构为 `common / fabric / neoforge` 多加载器结构。
- [x] 已确认当前主要玩法集中在云方块、Cloud Walker、压缩空气、喷气背包、BackTank 背槽。
- [x] 已确认提交 `a5bb4b6` 已推送到远端。

## 1. 执行原则

- [x] 每一条玩法规则只能有一个公共真相源。
- [x] 平台无关规则优先放在 `common`。
- [x] 平台模块只做注册、事件接入、网络接入和客户端挂接。
- [x] 新增机制先做纯规则类和测试，再接世界行为。
- [x] Datagen 生成内容必须与实际注册内容同步。
- [x] 玩家可见名称必须匹配真实功能，不能保留误导性空壳。
- [x] 未注册、未使用或已废弃的资源必须分类到设计参考区，或接入正式玩法。
- [x] 贴图源文件、预览图、游戏资源图必须一一可追踪。
- [x] 每轮修改后运行与改动范围匹配的测试。
- [x] 大范围资源或玩法改动后运行 `build` 和必要的游戏内烟测。

## 2. 已完成系统守护项

- [x] 公共注册定义集中在 `CloudCraftRegistryDefinitions`、`CloudCraftItemDefinitions`、`CloudCraftBlockDefinitions`。
- [x] 现有注册完整性由 `CloudCraftRegistryDefinitionsTest` 覆盖。
- [x] 云方块基础碰撞和 Cloud Walker 规则已有独立规则/测试。
- [x] 气态云固化半径规则已有独立规则/测试。
- [x] 压缩空气数值集中在 `CompressedAirRules` 和 `CloudPressureProfile`。
- [x] 喷气背包飞行数值集中在 `JetpackFlightRules`。
- [x] 喷气背包运行态和物品栈状态已有单元测试。
- [x] BackTank 背槽 UI/容器逻辑已有测试。
- [x] NeoForge GameTest 入口存在。
- [x] 贴图工程目录已建立：`design/texture_project`。
- [x] 贴图工程已按 `source / preview / reference` 分类。
- [x] 物品、方块、效果图标 APX 源文件已进入 `design/texture_project/source`。
- [x] 预览图已按 `item / block / mob_effect` 分类。
- [x] 旧自定义药水瓶游戏资源已移除，药水显示回到原版模型链。

## 3. P0 稳定化修复

### 3.1 云气体水平移动

- [x] 移除 `CloudMotionRules` 中所有气云水平拖拽恒为 `1.0` 的假差异。
- [x] 将水平运动改为自然收敛模型，而不是单纯乘性衰减到 0。
- [x] 为每种气云定义可读的水平目标速度。
- [x] 为每种气云定义可读的水平收敛强度。
- [x] 保留三维方向，不让模型凭空改变玩家输入方向。
- [x] 低速实体不应被气云凭空水平加速。
- [x] 超过目标速度的实体应逐步向目标速度回落。
- [x] 层云、雨层云应明显压低水平速度。
- [x] 卷云应保留更高水平漂移速度。
- [x] 高层云应处于层云和卷云之间。
- [x] 积雨云应体现强扰动或强衰减，不应只是安全托举。
- [x] 垂直阻力继续保留单独参数。
- [x] `CloudMotionRulesTest` 更新为验证各云种差异。
- [x] 真实气云方块继续从 `CloudCraftBlockDefinitions` 读取同一套规则。

### 3.2 可食用云朵

- [x] 审核 `cloud` 物品的食物定位：基础食物，不应强制玩家长时间悬浮。
- [x] 将 10 秒 `LEVITATION` 改成更温和、更可控的效果。
- [x] 保留轻盈主题，但降低新手误触惩罚。
- [x] 为食物效果增加或更新测试，锁定持续时间和效果类型。
- [x] 确认语言、模型和贴图不需要跟随改名。

### 3.3 悬空盔甲资产

- [x] 审核 `cumulus_cloud_helmet/chestplate/leggings/boots` 是否被注册、引用或生成。
- [x] 若本轮不实现盔甲线，将贴图源和预览移动到设计参考分类，避免误导。
- [x] 本阶段不实现盔甲线；若后续实现，必须补齐注册、材料、模型、语言、配方、贴图和测试。
- [x] 盔甲贴图若保留为正式资源，必须对称、居中、像素风统一。
- [x] `TEXTURE_PROMPTS.md` 更新为反映实际状态。

## 4. P1 气态转换器闭环

### 4.1 功能定义

- [x] 明确 `gas_state_converter` 是主路径机器，而不是装饰方块。
- [x] 明确它支持气态云到固态云的转换。
- [x] 明确支持固态云到气态云的反向转换。
- [x] 明确输入输出交互方式：右键、物品槽、区域扫描或红石触发。
- [x] 明确消耗资源：积云碎片固化气态云，卷云丝线气化固态云。
- [x] 保留投掷药水固化作为发现性机制或备用机制。

### 4.2 BlockEntity 与规则层

- [x] 增加或扩展平台无关的转换规则类。
- [x] 增加转换器 Block 或 BlockEntity 需要的公共行为类。
- [x] 不把平台注册对象回填到 `common` 可变静态字段。
- [x] 为 NeoForge 注册 BlockEntityType。
- [x] 为 Fabric 注册 BlockEntityType。
- [x] 如果需要菜单，先做最小无 GUI 交互；GUI 后置。
- [x] 转换扫描半径必须复用或扩展 `CloudTransformationRules`。
- [x] 转换结果必须保留云属，不允许所有气云变同一种固云。
- [x] 失败状态需要玩家可理解的反馈。
- [x] 转换成功需要粒子或声音反馈，初期可复用原版事件。

### 4.3 数据与测试

- [x] Recipe 与机器功能一致，不再是只做装饰的配方。
- [x] Loot table 保持掉落自身。
- [x] Blockstate 与模型保持朝向正常。
- [x] 增加转换规则单元测试。
- [x] 增加最小 GameTest 验证机器能转换气云。
- [x] 运行 datagen 并确认生成资源无漂移。

## 5. P1 云属性格差异

- [x] 六种已有云属都具备明确视觉差异。
- [x] 六种已有云属都具备明确运动差异。
- [x] 六种已有云属都具备明确压缩空气差异。
- [x] 六种已有云属都具备明确掉落或合成差异。
- [x] 积云定位为入门、稳定、基础材料。
- [x] 层云定位为低速、铺展、雾状。
- [x] 卷云定位为高速、轻薄、高空材料。
- [x] 高层云定位为中速、遮光、过渡材料。
- [x] 雨层云定位为慢速、潮湿、水循环。
- [x] 积雨云定位为危险、高压、雷暴材料。
- [x] 更新 `CLOUD_DIMENSION_PLAN.md` 时只写长期设定，不写短期实现噪音。

## 6. P2 云之维度最小垂直切片

### 6.1 入口与返回

- [x] 设计最小进入方式。
- [x] 设计最小返回方式。
- [x] 入口材料与早期云科技线相关。
- [x] 避免直接复制原版传送门而没有云主题机制。
- [x] 潜行右键 `gas_state_converter` 可作为云维度往返入口。
- [x] 默认世界和旧世界自动实例化云维度已通过数据包维度入口和运行时服务器维度可用性验证确认。

### 6.2 维度注册

- [x] 添加维度 key。
- [x] 添加 dimension type 数据。
- [x] 添加 minimal noise settings 或等效世界生成配置。
- [x] 添加最小 biome。
- [x] Fabric 和 NeoForge 都能加载维度数据。
- [x] 不引入平台层重复维度规则。
- [x] 添加 `cloudcraft:cloud_dimension` 世界预设，选择该预设的新世界会包含云维度。

### 6.3 第一片可玩区域

- [x] 实现积云原野作为第一个生物群系。
- [x] 生成可站立的积云地形。
- [x] 玩家进入后不会立即坠落死亡。
- [x] 能采集基础云资源。
- [x] 能使用喷气背包与云环境形成充压循环。
- [x] 能回到主世界。
- [x] 有最小 GameTest 或手工测试记录。
- [x] GameTest 已验证世界预设加载和落点平台/返回转换器生成。
- [x] 固态和气态云都能映射到 `CloudPressureProfile`，云维度积云平台可被喷气背包运行时识别为可充压环境。

## 7. P2 进度、配置和玩家引导

- [x] 增加 advancement 数据生成器。
- [x] 增加根进度：发现 CloudCraft。
- [x] 增加收集云碎片进度。
- [x] 增加制作压缩气罐进度。
- [x] 增加制作基础喷气背包进度。
- [x] 增加升级稳定喷气背包进度。
- [x] 增加升级高压喷气背包进度。
- [x] 增加使用气态转换器进度。
- [x] 增加进入云之维度进度。
- [x] 增加基础配置层，至少覆盖关键数值倍率。
- [x] 配置默认值必须等于规则类当前默认值。
- [x] 配置加载不能破坏 common/platform 边界。
- [x] 配置覆盖喷气背包压力容量、消耗、云内充压、垂直推力、水平加速度、水平最大速度、气云水平目标速度和收敛强度。
- [x] NeoForge 通过 `ModConfigSpec` 加载配置，Fabric 通过 `config/cloudcraft.json` 加载配置。
- [x] Common 规则类只读取 `CloudCraftConfig` 快照，不直接依赖任一加载器 API。

## 8. P3 视觉与音效

- [x] 云方块气态和固态透明度在游戏内可读。
- [x] 云方块贴图可平铺，无明显断边。
- [x] 所有物品贴图居中。
- [x] 对称装备和机器部件必须对称。
- [x] 碎片类物品允许不对称，但轮廓必须清晰。
- [x] `cloud.png` 维持用户原始造型，但调色必须像素画化。
- [x] `cumulus_cloud_fragment.png` 需要保留“碎片”特征，不做成瓶子或整团云。
- [x] 气态转换器贴图若修改，优先从原贴图工程改，不凭空重画。
- [x] 增加云内粒子效果。
- [x] 增加转换器工作粒子。
- [x] 增加喷气背包推进音效或复用合适原版音效。
- [x] 增加转换成功音效或复用合适原版音效。

## 9. P3 后续大型化基建

- [x] 自定义实体基础包结构。
- [x] 最小云生物实体。
- [x] 云维度自然生成规则。
- [x] 天气事件规则。
- [x] 积雨云雷暴事件。
- [x] 雨层云降雨/水循环事件。
- [x] JEI/REI/EMI 配方展示策略。
- [x] 与其他科技 mod 的兼容策略。
- [x] 只在压缩空气闭环完整后再接电力兼容。

## 10. 验证门槛

- [x] 改纯规则后运行 `.\gradlew.bat :common:test`。
- [x] 改 datagen 后运行 `.\gradlew.bat :neoforge:runCommonData`。
- [x] 改注册、资源或跨模块逻辑后运行 `.\gradlew.bat build`。
- [x] 改 NeoForge 平台行为后运行 `.\gradlew.bat :neoforge:runGameTestServer`。
- [x] 改客户端渲染、贴图或 UI 后运行实际客户端烟测。
- [x] 改 Fabric 平台行为后运行 Fabric 客户端或服务端烟测。
- [x] 提交前检查 `git status --short`，确认只包含本轮相关文件。
- [x] 提交信息使用中文。

## 11. 手工/烟测记录

- [x] NeoForge GameTest 服务端可加载 `cloudcraft:cloud_dimension` world preset，并验证云维度落点平台与返回转换器。
- [x] Fabric 服务端烟测可加载 `cloudcraft 1.0.0`，使用 `level-type=cloudcraft:cloud_dimension` 创建新世界，加载 1490 recipes / 1595 advancements，并启动到 `Done`。
- [x] 物品贴图审计显示所有正式 `textures/item/*.png` 的可见像素边界都在画布中心 ±0.5 像素内；喷气背包、基础框架、压缩气罐、稳定喷口、高压腔体等对称部件镜像差异为 0%。
- [x] Fabric 客户端烟测可启动到 Minecraft 1.21.11 主菜单；日志显示 ResourceManager 加载 `cloudcraft`，并成功创建 `minecraft:textures/atlas/items.png-atlas`。
- [x] NeoForge GameTest 已验证 `cloudcraft:cumulus_fields` biome 运行时包含 `stratus_cloud_patch`、`nimbostratus_cloud_patch`、`cumulonimbus_cloud_patch`、`cirrus_gas_wisps` 四个 placed feature。
- [x] NeoForge GameTest 已验证普通服务器世界中 `MinecraftServer#getLevel(cloudcraft:cloud_dimension)` 非空，云维度不依赖手动选择自定义 world preset 才能被传送逻辑访问。
- [x] NeoForge GameTest 已验证 `cloudcraft:cloud_wisp` 可生成，且保持浮空、非充能、无爆炸。
- [x] NeoForge GameTest 已验证积雨云簇会生成视觉闪电雷暴事件，雨层云簇会在承托面上方凝结水源；17 个必需测试全部通过。
- [x] `docs/COMPATIBILITY_STRATEGY.md` 已明确 JEI/REI/EMI 展示顺序、气态转换器展示语义、科技 mod optional integration 边界和电力兼容门槛；`CompatibilityBoundaryTest` 验证当前源码与 Gradle 仍未硬依赖配方查看器或能量 API。
- [x] `CloudTextureAssetTest` 验证云方块 APX 源、预览和游戏 PNG 可追踪；固态云全不透明，气态云平均 alpha 40-150 且透明像素充足；云方块边缘 RGBA 差异不超过 16；六类云按亮度、冷暖和气态密度保持可区分视觉定位。
- [x] Fabric 客户端烟测 `fabric-client-smoke-20260624-013521` 启动到 Minecraft 1.21.11 主菜单；日志显示 ResourceManager 加载 `cloudcraft`，并成功创建 `minecraft:textures/atlas/blocks.png-atlas` 与 `minecraft:textures/atlas/items.png-atlas`。
- [x] `ArchitectureBoundaryTest` 验证 `common/src/main/java` 不引用 Fabric/NeoForge/Forge 加载器 API，Fabric/NeoForge 平台模块不定义 `*Rules.java` 玩法规则类；`CloudCraftRegistryDefinitionsTest`、`CompatibilityBoundaryTest` 和 `CloudTextureAssetTest` 分别守护 datagen 同步、第三方兼容边界与贴图追踪。
