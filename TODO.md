# CloudCraft 修复清单

每完成一项，立即把对应复选框改为 `[x]`，并保留实际验证命令。

- [x] 建立逐步修复清单
  - 验证：确认 `TODO.md` 存在且清单可执行。
- [x] 补注册完整性测试，降低新增内容漏同步风险
  - 涉及：`common/src/test/java/com/tr1c/cloudcraft/registry/`
  - 验证：`./gradlew :common:test`、`./gradlew build`
- [x] 明确 datagen 与资源边界
  - 涉及：`README.md`
  - 验证：`./gradlew :neoforge:runClientData`、`git diff --exit-code -- common/src/generated/resources`
- [x] 拆分 NeoForge 入口接线，保持主入口变薄
  - 涉及：`neoforge/src/main/java/com/tr1c/cloudcraft/CloudCraftNeoForge.java`
  - 验证：`./gradlew :neoforge:build`、`./gradlew :neoforge:runGameTestServer`、`./gradlew build`
- [x] 处理 Fabric 运行态验证不足
  - 涉及：`README.md`
  - 验证：`./gradlew :fabric:build`，必要时手工运行 `./gradlew :fabric:runClient`

## 本轮修复：注册、Datagen 与命名同步

- [x] 补齐方块 loot table，并明确 gas cloud 不掉落
  - 涉及：`common/src/main/java/com/tr1c/cloudcraft/datagen/CloudCraftLootTableProvider.java`
  - 验证：`./gradlew :neoforge:runClientData`
- [x] 固化方块注册顺序依赖
  - 涉及：`CloudCraftBlockDefinitions.java`、`CloudCraftRegistryDefinitionsTest.java`
  - 验证：`./gradlew :common:test`、`./gradlew :fabric:build`、`./gradlew :neoforge:build`
- [x] 拆分 solid cloud 药水注册 ID 与药水瓶模型 ID
  - 涉及：`ModIds.java`、药水注册、语言文件、注册测试
  - 验证：`./gradlew :common:test`、`./gradlew :fabric:build`、`./gradlew :neoforge:build`
- [x] 收敛药水瓶 asset ID 来源
  - 涉及：`CloudCraftRegistryDefinitions.java`、`CloudCraftAssetProvider.java`、注册测试
  - 验证：`./gradlew :common:test`、`./gradlew :neoforge:runClientData`
- [x] 收敛创造栏内容同步
  - 涉及：`CloudCraftCreativeTabContents.java`、Fabric/NeoForge item 与 creative tab
  - 验证：`./gradlew :fabric:build`、`./gradlew :neoforge:build`、`./gradlew :neoforge:runGameTestServer`

## 本轮修复：项目不足优化

- [x] 补资源与语言一致性测试
  - 涉及：`common/src/test/java/com/tr1c/cloudcraft/registry/CloudCraftRegistryDefinitionsTest.java`、语言文件、生成资源
  - 验证：`./gradlew :common:test`
- [x] 收敛平台 item 注册样板重复
  - 涉及：`FabricModItems.java`、`NeoForgeModItems.java`、`CloudCraftRegistryDefinitions.java`
  - 验证：`./gradlew :common:test`、`./gradlew :fabric:build`、`./gradlew :neoforge:build`
- [x] 补强 Fabric 运行态验证策略
  - 涉及：`README.md`、`.github/workflows/build.yml`
  - 验证：`./gradlew :common:test`、`./gradlew :fabric:build`；运行态仍需手工 `./gradlew :fabric:runClient`
- [x] 明确 datagen 单入口策略
  - 涉及：`README.md`、`TODO.md`
  - 验证：`./gradlew :neoforge:runClientData`、`git diff --exit-code -- common/src/generated/resources`
- [x] 分步管理当前工作区改动，避免修复项混杂
  - 涉及：本清单与每项独立验证记录
  - 验证：每完成一项立即更新对应复选框和实际验证命令

## 最终验证

```bash
./gradlew :common:test
./gradlew :fabric:build
./gradlew :neoforge:build
./gradlew :neoforge:runGameTestServer
./gradlew :neoforge:runClientData
./gradlew build
git diff --exit-code -- common/src/generated/resources
```
