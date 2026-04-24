@echo off
setlocal
set "ROOT=%~dp0"
set "JEI_VERSION=27.4.0.22"
set "JEI_BASE=https://maven.blamejared.com/mezz/jei"
set "MODS_DIR=%ROOT%fabric\runs\client\mods"
set "FABRIC_JAR=%MODS_DIR%\jei-1.21.11-fabric-%JEI_VERSION%.jar"
set "CORE_JAR=%MODS_DIR%\jei-1.21.11-core-%JEI_VERSION%.jar"
set "COMMON_JAR=%MODS_DIR%\jei-1.21.11-common-%JEI_VERSION%.jar"
set "COMMON_API_JAR=%MODS_DIR%\jei-1.21.11-common-api-%JEI_VERSION%.jar"
set "LIB_JAR=%MODS_DIR%\jei-1.21.11-lib-%JEI_VERSION%.jar"
set "GUI_JAR=%MODS_DIR%\jei-1.21.11-gui-%JEI_VERSION%.jar"
set "FABRIC_API_JAR=%MODS_DIR%\jei-1.21.11-fabric-api-%JEI_VERSION%.jar"
if exist "C:\Program Files\Java\jdk-21\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)
if not exist "%MODS_DIR%" mkdir "%MODS_DIR%"
if not exist "%FABRIC_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-fabric/%JEI_VERSION%/jei-1.21.11-fabric-%JEI_VERSION%.jar' -OutFile '%FABRIC_JAR%'"
if not exist "%CORE_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-core/%JEI_VERSION%/jei-1.21.11-core-%JEI_VERSION%.jar' -OutFile '%CORE_JAR%'"
if not exist "%COMMON_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-common/%JEI_VERSION%/jei-1.21.11-common-%JEI_VERSION%.jar' -OutFile '%COMMON_JAR%'"
if not exist "%COMMON_API_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-common-api/%JEI_VERSION%/jei-1.21.11-common-api-%JEI_VERSION%.jar' -OutFile '%COMMON_API_JAR%'"
if not exist "%LIB_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-lib/%JEI_VERSION%/jei-1.21.11-lib-%JEI_VERSION%.jar' -OutFile '%LIB_JAR%'"
if not exist "%GUI_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-gui/%JEI_VERSION%/jei-1.21.11-gui-%JEI_VERSION%.jar' -OutFile '%GUI_JAR%'"
if not exist "%FABRIC_API_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-fabric-api/%JEI_VERSION%/jei-1.21.11-fabric-api-%JEI_VERSION%.jar' -OutFile '%FABRIC_API_JAR%'"
call "%ROOT%gradlew.bat" :fabric:runClient
