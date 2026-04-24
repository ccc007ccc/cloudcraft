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
./gradlew :neoforge:runClientData
```

涉及客户端、渲染或平台接入时，还需要手工运行：

```bash
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
```

## Datagen

生成资源由 NeoForge `clientData` 任务触发，输出到 `common/src/generated/resources`。修改生成逻辑后运行：

```bash
./gradlew :neoforge:runClientData
git diff --exit-code -- common/src/generated/resources
```

## 验证重点

每次修改核心玩法后至少确认：

1. `./gradlew build`
2. `./gradlew :common:test`
3. `./gradlew :fabric:build`
4. `./gradlew :neoforge:runGameTestServer`
