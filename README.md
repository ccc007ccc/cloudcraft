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

## 验证重点

每次修改核心玩法后至少确认：

1. `./gradlew build`
2. `./gradlew :common:test`
3. `./gradlew :fabric:build`
4. `./gradlew :neoforge:runGameTestServer`
