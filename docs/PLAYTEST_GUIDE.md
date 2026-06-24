# CloudCraft 游戏测试启动指南

本文记录本地启动、自动化测试和手工烟测方式。

## 常用构建验证

```powershell
.\gradlew.bat :common:test
.\gradlew.bat build
.\gradlew.bat :neoforge:runGameTestServer
```

修改 datagen provider 后运行：

```powershell
.\gradlew.bat :neoforge:runCommonData
```

## 启动 Fabric 客户端

推荐直接运行仓库脚本：

```powershell
.\run-fabric-client.bat
```

这个脚本会把 JEI 测试 jar 放进 `fabric/runs/client/mods`，然后执行：

```powershell
.\gradlew.bat :fabric:runClient
```

如果只想启动原始 Fabric 开发客户端，不需要脚本下载 JEI，可以直接运行：

```powershell
.\gradlew.bat :fabric:runClient
```

Fabric 客户端运行目录：

```text
fabric/runs/client
```

日志位置：

```text
fabric/runs/client/logs/latest.log
```

## 启动 NeoForge 客户端

推荐运行：

```powershell
.\run-neoforge-client.bat
```

脚本会把 JEI 测试 jar 放进 `run/mods`，然后执行：

```powershell
.\gradlew.bat :neoforge:runClient
```

如果只想启动原始 NeoForge 开发客户端：

```powershell
.\gradlew.bat :neoforge:runClient
```

NeoForge 客户端运行目录：

```text
run
```

## 启动 NeoForge GameTest

```powershell
.\gradlew.bat :neoforge:runGameTestServer
```

成功时日志会显示 CloudCraft 的 GameTest 全部通过。

## 启动 Fabric 服务端烟测

```powershell
.\gradlew.bat :fabric:runServer
```

Fabric 服务端运行目录：

```text
fabric/runs/server
```

看到 `Done` 后说明服务端启动完成。

## 手工客户端烟测清单

进入游戏后优先看这些点：

- 主菜单能正常出现。
- 资源包加载日志包含 `cloudcraft`。
- 创造模式标签里物品模型不缺失。
- `cloud.png` 图标在物品栏里有体积和层次。
- 喷气背包图标显示气压条。
- 鼠标悬浮喷气背包显示气压和飞行参数。
- 背槽装备喷气背包后第三人称可见。
- 推进时喷口附近出现粒子。
- 气态转换器右键打开 GUI。
- 临时维度入口方块可进入和返回云域。
- 新建普通世界不应弹实验性设置警告。

## Python 截屏

可以用 Python 截当前屏幕：

```powershell
@'
from PIL import ImageGrab
img = ImageGrab.grab()
img.save("playtest-screenshot.png")
'@ | python -
```

截图文件会输出到当前命令所在目录。

## Python 键鼠控制

如果需要自动化按键或鼠标，可先确认安装了可用库。没有库时不要硬跑，改用手工测试。

常见思路：

```powershell
@'
import time
import pyautogui
time.sleep(2)
pyautogui.press("f5")
pyautogui.press("space")
'@ | python -
```

如果 `pyautogui` 不存在，可以先停下来，不要临时污染项目依赖。

## 结束残留客户端进程

正常情况下直接在游戏里退出。若脚本或测试中断后怀疑有残留 Java 进程，可以查看：

```powershell
Get-Process | Where-Object { $_.ProcessName -match 'java|javaw|Minecraft|gradle' } | Select-Object Id,ProcessName,MainWindowTitle,StartTime
```

不要随便杀掉所有 Java 进程；先确认窗口标题或启动时间属于这次测试。

