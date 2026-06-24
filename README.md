# CloudCraft

## 项目结构

- `common/`：平台无关的方块、物品、效果、药水、通用规则、datagen 与共享资源
- `fabric/`：Fabric 平台注册、客户端初始化与平台适配
- `neoforge/`：NeoForge 平台注册、客户端初始化、GameTest 与 datagen 接入
- `buildSrc/`：共享 Gradle 构建逻辑

## 常用命令

```bash
./gradlew build
./gradlew :common:test
./gradlew :fabric:build
./gradlew :neoforge:runGameTestServer
./gradlew :neoforge:runCommonData
```

游戏启动和手工烟测见 `docs/PLAYTEST_GUIDE.md`。新一轮故事、玩法和重构规格见 `CLOUDCRAFT_STORY_AND_GAMEPLAY.md` 与 `SPEC_CLOUDCRAFT_REWORK.md`。

涉及客户端、渲染或平台接入时，还需要手工运行：

```bash
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
```

当前自动化运行态测试集中在 NeoForge GameTest，Fabric 在 CI 中先覆盖编译与 common 规则测试。修改 Fabric 注册、客户端初始化或 Fabric 专属适配时，至少运行 `./gradlew :common:test`、`./gradlew :fabric:build`，并手工启动 `./gradlew :fabric:runClient` 验证进入游戏与相关功能。

## Datagen

生成资源由 NeoForge `clientData` 任务统一触发，输出到 `common/src/generated/resources`，供两个加载器共享。datagen provider 仍放在 `common`，NeoForge 只作为当前唯一执行入口，避免 Fabric 再维护一套重复生成流程。修改生成逻辑后运行：

```bash
./gradlew :neoforge:runCommonData
git diff --exit-code -- common/src/generated/resources
```

资源边界：

- `common/src/main/resources`：手写资源，例如语言文件、纹理和复杂手写模型
- `common/src/generated/resources`：datagen 产物，不手工修改
- `fabric` / `neoforge`：只放平台专属资源，不复制 common 资源

## 配置

CloudCraft 的基础数值配置由 `common` 的 `CloudCraftConfig` 统一生效。NeoForge 使用原生 common config，Fabric 首次启动时会生成 `config/cloudcraft.json`。

当前配置键全部是倍率，默认值均为 `1.0`，表示保持代码默认手感：

- `pressureCapacityMultiplier`：喷气背包最大压力容量
- `pressureCostMultiplier`：推进和悬停压力消耗
- `pressureRechargeMultiplier`：云内充压与云碎片充压
- `jetpackVerticalSpeedMultiplier`：喷气背包垂直推力
- `jetpackHorizontalAccelerationMultiplier`：喷气背包水平加速度
- `jetpackHorizontalMaxSpeedMultiplier`：喷气背包水平最大速度
- `gasCloudHorizontalTargetSpeedMultiplier`：气态云水平目标速度
- `gasCloudHorizontalConvergenceMultiplier`：气态云水平速度收敛强度

## 兼容策略

配方查看器和其他科技 mod 的接入边界记录在 `docs/COMPATIBILITY_STRATEGY.md`。当前版本不把 JEI、REI、EMI 或电力 API 接成运行时依赖；后续兼容必须保持 optional integration，并以 CloudCraft 自己的压缩空气闭环为核心。

## 验证重点

每次修改核心玩法后至少确认：

1. `./gradlew build`
2. `./gradlew :common:test`
3. `./gradlew :fabric:build`
4. `./gradlew :neoforge:runGameTestServer`
