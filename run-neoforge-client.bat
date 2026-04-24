@echo off
setlocal
set "ROOT=%~dp0"
set "JEI_VERSION=27.4.0.22"
set "JEI_BASE=https://maven.blamejared.com/mezz/jei"
set "MODS_DIR=%ROOT%run\mods"
set "NEOFORGE_JAR=%MODS_DIR%\jei-1.21.11-neoforge-%JEI_VERSION%.jar"
if exist "C:\Program Files\Java\jdk-21\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)
if not exist "%MODS_DIR%" mkdir "%MODS_DIR%"
if not exist "%NEOFORGE_JAR%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest '%JEI_BASE%/jei-1.21.11-neoforge/%JEI_VERSION%/jei-1.21.11-neoforge-%JEI_VERSION%.jar' -OutFile '%NEOFORGE_JAR%'"
call "%ROOT%gradlew.bat" :neoforge:runClient
