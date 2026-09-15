@echo off
rem Eazytec is pleased to support the open source community by making CPF-KMP-CMP available.
rem Copyright (C) 2026 Eazytec. All rights reserved.
rem
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem
rem      http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

setlocal EnableDelayedExpansion
REM Run OHOS app on Windows (no debug / LLDB)
REM Usage: runOhosApp-Win.bat [options] [PLATFORM] [TARGET_ID]
REM   Options: -b BUNDLE -a ABILITY -p LOCAL_OHOS_PATH
REM   Example: runOhosApp-Win.bat ohosArm64 127.0.0.1:5555

set DEFAULT_PLATFORM=ohosArm64
set DEFAULT_TARGET_ID=127.0.0.1:5555
set DEFAULT_BUNDLE_NAME=com.example.harmonyapp
set DEFAULT_ABILITY_NAME=EntryAbility
set BUNDLE_NAME=%DEFAULT_BUNDLE_NAME%
set ABILITY_NAME=%DEFAULT_ABILITY_NAME%
set BUILD_MODE=debug
set DEBUG_MODE=attach
set LOCAL_OHOS_PATH=
set PLATFORM=
set TARGET_ID=

REM Parse options (supports options in any position, e.g. ohosArm64 62Q0226107024702 -m release)
:parse
if "%~1"=="" goto done_parse
if "%~1"=="-m" (set "BUILD_MODE=%~2" & shift & shift & goto parse)
if "%~1"=="-b" (set "BUNDLE_NAME=%~2" & shift & shift & goto parse)
if "%~1"=="-a" (set "ABILITY_NAME=%~2" & shift & shift & goto parse)
if "%~1"=="-d" (set "DEBUG_MODE=%~2" & shift & shift & goto parse)
if "%~1"=="-p" (set "LOCAL_OHOS_PATH=%~2" & shift & shift & goto parse)
if "%~1"=="-h" goto show_help
REM Positional: first=PLATFORM, second=TARGET_ID
if "!PLATFORM!"=="" (set "PLATFORM=%~1") else if "!TARGET_ID!"=="" (set "TARGET_ID=%~1")
shift
goto parse
:done_parse
if "!PLATFORM!"=="" set "PLATFORM=%DEFAULT_PLATFORM%"
if "!TARGET_ID!"=="" set "TARGET_ID=%DEFAULT_TARGET_ID%"

REM Record script start time (epoch seconds for elapsed calculation)
for /f %%t in ('powershell -NoProfile -Command "[int][double]::Parse((Get-Date -UFormat '%%s'))"') do set START_TIME=%%t

echo Run environment configuration:
echo   - Platform: %PLATFORM%
echo   - Device: %TARGET_ID%
echo   - Build mode: %BUILD_MODE%
echo   - Debug mode: %DEBUG_MODE%
echo   - Bundle: %BUNDLE_NAME%
echo   - Ability: %ABILITY_NAME%
if not "%LOCAL_OHOS_PATH%"=="" echo   - External OHOS path: %LOCAL_OHOS_PATH%
echo ------------------------------------------------------------

REM ====================== [2. Environment Path and SDK Configuration] ======================
REM DevEco/SDK path: OHOS_SDK_HOME env or default Windows location
if "%DEVECO_PATH%"=="" (
  if exist "C:\Program Files\Huawei\DevEco Studio" (
    set "DEVECO_PATH=C:\Program Files\Huawei\DevEco Studio"
  ) else if exist "D:\Program Files\Huawei\DevEco Studio" (
    set "DEVECO_PATH=D:\Program Files\Huawei\DevEco Studio"
  ) else (
    echo Error: DevEco Studio not found. Set OHOS_SDK_HOME or install to default path.
    exit /b 1
  )
)
echo DevEco Studio Path: %DEVECO_PATH%
echo Checking environment configuration...

set "MIN_VERSION=6.0.0"
set "PRODUCT_INFO=%DEVECO_PATH%\product-info.json"
if exist "%PRODUCT_INFO%" (
  for /f "tokens=*" %%v in ('powershell -NoProfile -Command "(Get-Content '%PRODUCT_INFO%' | ConvertFrom-Json).version"') do set "CURRENT_VERSION=%%v"
) else (
  echo Warning: product-info.json not found, skipping version check.
  set "CURRENT_VERSION=unknown"
)

if not "%CURRENT_VERSION%"=="unknown" (
  echo Current DevEco version: %CURRENT_VERSION%
  echo Minimum required version: %MIN_VERSION%
  for /f "tokens=1,2 delims=." %%a in ("%CURRENT_VERSION%") do (
    set "CUR_MAJOR=%%a"
    set "CUR_MINOR=%%b"
  )
  for /f "tokens=1,2 delims=." %%a in ("%MIN_VERSION%") do (
    set "REQ_MAJOR=%%a"
    set "REQ_MINOR=%%b"
  )
  if !CUR_MAJOR! lss !REQ_MAJOR! (
    echo -------------------------------------------------------
    echo Error: DevEco version is too low!
    echo Current version: %CURRENT_VERSION%
    echo Minimum required: %MIN_VERSION%+
    echo Please upgrade DevEco Studio and run this script again.
    echo -------------------------------------------------------
    exit /b 5
  )
  if !CUR_MAJOR! equ !REQ_MAJOR! if !CUR_MINOR! lss !REQ_MINOR! (
    echo -------------------------------------------------------
    echo Error: DevEco version is too low!
    echo Current version: %CURRENT_VERSION%
    echo Minimum required: %MIN_VERSION%+
    echo Please upgrade DevEco Studio and run this script again.
    echo -------------------------------------------------------
    exit /b 5
  )
  echo DevEco version meets requirements (%CURRENT_VERSION%)
)

set "HDC_BIN=%DEVECO_PATH%\sdk\default\openharmony\toolchains\hdc.exe"
set "DEVECO_SDK_HOME=%DEVECO_PATH%\sdk"
set "PATH=%DEVECO_PATH%\tools\node;%DEVECO_PATH%\tools\ohpm\bin;%DEVECO_PATH%\tools\hvigor\bin;%DEVECO_SDK_HOME%;%PATH%"

if not exist "%HDC_BIN%" (
  echo Error: hdc not found at %HDC_BIN%
  exit /b 1
)

"%HDC_BIN%" -t %TARGET_ID% shell aa force-stop %BUNDLE_NAME% 2>nul
echo [Harmony-Debug-Pipeline] Prep: hdc force-stop %BUNDLE_NAME% (ignore errors if not running)
REM ====================== Gradle Build ======================
echo [Harmony-Debug-Pipeline] Build: Gradle KMP publishBinariesToHarmonyApp (mode=%BUILD_MODE%; first run slower, later incremental)
echo Working path: %CD%
echo Building OpenHarmony (%PLATFORM%)...

if "%PLATFORM%"=="ohosArm64" (
   REM for ohosArm64 ohos in windows x86_64 build
  if "%BUILD_MODE%"=="release" (
    if not "%LOCAL_OHOS_PATH%"=="" (
      call gradlew.bat :composeApp:publishReleaseBinariesToHarmonyApp -PharmonyAppPath="%LOCAL_OHOS_PATH%"
    ) else (
      call gradlew.bat :composeApp:publishReleaseBinariesToHarmonyApp
    )
  ) else (
    if not "%LOCAL_OHOS_PATH%"=="" (
      call gradlew.bat :composeApp:publishDebugBinariesToHarmonyApp -PharmonyAppPath="%LOCAL_OHOS_PATH%"
    ) else (
      call gradlew.bat :composeApp:publishDebugBinariesToHarmonyApp
    )
  )
) else if "%PLATFORM%"=="ohosX86_64" (
    REM  for simulate ohos in windows x86_64
  if "%BUILD_MODE%"=="release" (
    if not "%LOCAL_OHOS_PATH%"=="" (
      call gradlew.bat :composeApp:publishReleaseBinariesToHarmonyApp -PharmonyAppPath="%LOCAL_OHOS_PATH%"
    ) else (
      call gradlew.bat :composeApp:publishReleaseBinariesToHarmonyApp
    )
  ) else (
    if not "%LOCAL_OHOS_PATH%"=="" (
      call gradlew.bat :composeApp:publishDebugBinariesToHarmonyApp -PharmonyAppPath="%LOCAL_OHOS_PATH%"
    ) else (
      call gradlew.bat :composeApp:publishDebugBinariesToHarmonyApp
    )
  )
) else (
  echo Error: Unsupported platform '%PLATFORM%'
  exit /b 4
)

echo [Harmony-Debug-Pipeline] Build: KMP publish finished  next ohpm/hvigor HAP (incremental builds may be faster)
REM SO packaging end time / HAP build start time
for /f %%t in ('powershell -NoProfile -Command "[int][double]::Parse((Get-Date -UFormat '%%s'))"') do set HAP_START_TIME=%%t

REM ====================== HAP build ======================
if not "%LOCAL_OHOS_PATH%"=="" (
  set "HARMONY_APP_DIR=%LOCAL_OHOS_PATH%"
) else (
  set "HARMONY_APP_DIR=harmonyApp"
)

if not exist "%HARMONY_APP_DIR%" (
  echo Error: harmonyApp directory not found: %HARMONY_APP_DIR%
  exit /b 4
)

cd /d "%HARMONY_APP_DIR%"
echo Switched to harmonyApp directory: %CD%
echo Running ohpm and Hvigor...

call ohpm install --all
if errorlevel 1 (echo ohpm install failed & exit /b 1)

REM hvigorw.js may return exit code 0 even when logging BUILD FAILED; detect failure from log text.
set "HVIGOR_LOG=%TEMP%\hvigor_sync_%RANDOM%_%RANDOM%.log"
call node "%DEVECO_PATH%\tools\hvigor\bin\hvigorw.js" --sync -p product=default -p buildMode=%BUILD_MODE% --analyze=normal --parallel --incremental --daemon > "%HVIGOR_LOG%" 2>&1
set "HVIGOR_SYNC_ERR=0"
if errorlevel 1 set "HVIGOR_SYNC_ERR=1"
type "%HVIGOR_LOG%"
findstr /I /C:"BUILD FAILED" "%HVIGOR_LOG%" >nul && set "HVIGOR_SYNC_ERR=1"
if "!HVIGOR_SYNC_ERR!"=="1" (
  echo.
  echo Error: hvigor sync failed ^(BUILD FAILED in log or non-zero exit^).
  del "%HVIGOR_LOG%" 2>nul
  exit /b 1
)
del "%HVIGOR_LOG%" 2>nul

set "HVIGOR_ERR=0"
if "%BUILD_MODE%"=="release" (
  set "HVIGOR_LOG=%TEMP%\hvigor_asm_%RANDOM%_%RANDOM%.log"
  call node "%DEVECO_PATH%\tools\hvigor\bin\hvigorw.js" --mode module -p module=entry -p product=default -p buildMode=release -p requiredDeviceType=phone assembleHap compileNative --analyze=normal --parallel --incremental --daemon > "!HVIGOR_LOG!" 2>&1
  if errorlevel 1 set "HVIGOR_ERR=1"
  type "!HVIGOR_LOG!"
  findstr /I /C:"BUILD FAILED" "!HVIGOR_LOG!" >nul && set "HVIGOR_ERR=1"
) else (
  set "HVIGOR_LOG=%TEMP%\hvigor_asm_%RANDOM%_%RANDOM%.log"
  call node "%DEVECO_PATH%\tools\hvigor\bin\hvigorw.js" --mode module -p module=entry@default -p product=default -p buildMode=debug -p requiredDeviceType=phone assembleHap --analyze=normal --parallel --incremental --daemon > "!HVIGOR_LOG!" 2>&1
  if errorlevel 1 set "HVIGOR_ERR=1"
  type "!HVIGOR_LOG!"
  findstr /I /C:"BUILD FAILED" "!HVIGOR_LOG!" >nul && set "HVIGOR_ERR=1"
)
if "!HVIGOR_ERR!"=="1" (
  echo.
  echo Error: Hvigor assembleHap failed ^(BUILD FAILED in log or non-zero exit; node may still report 0^).
  del "!HVIGOR_LOG!" 2>nul
  exit /b 1
)
del "!HVIGOR_LOG!" 2>nul

echo [Harmony-Debug-Pipeline] Build: HAP assemble finished

REM ====================== Install HAP ======================
set "HAP_DIR=entry\build\default\outputs\default"
set "SIGNED_HAP=entry-default-signed.hap"
set "HAP_FILE=%HAP_DIR%\%SIGNED_HAP%"

if not exist "%HAP_FILE%" (
  echo Error: Signed HAP package not found!
  echo.
  echo Expected path: %HARMONY_APP_DIR%\%HAP_DIR%\%SIGNED_HAP%
  echo.
  echo Possible causes: build not done, build failed, or HAP not signed.
  echo Suggested: check build logs, verify signing in DevEco Studio.
  exit /b 6
)

set DEVICE_FOUND=
for /f "delims=" %%i in ('"%HDC_BIN%" list targets 2^>nul') do echo %%i | findstr /C:"%TARGET_ID%" >nul && set DEVICE_FOUND=1
if not defined DEVICE_FOUND (
  echo Error: Device %TARGET_ID% is offline!
  exit /b 5
)

echo [Harmony-Debug-Pipeline] Install: bm install signed HAP (may uninstall/reinstall if a prior install failed)
echo Pushing and installing HAP...
echo   - Device: %TARGET_ID%
set "REMOTE_HAP_DIR=/data/local/tmp/debug_install"
set "REMOTE_HAP_PATH=%REMOTE_HAP_DIR%/%SIGNED_HAP%"
"%HDC_BIN%" -t %TARGET_ID% shell mkdir -p %REMOTE_HAP_DIR%
"%HDC_BIN%" -t %TARGET_ID% file send "%HAP_FILE%" %REMOTE_HAP_DIR%

"%HDC_BIN%" -t %TARGET_ID% shell bm install -p "%REMOTE_HAP_PATH%" 2>nul
if errorlevel 1 (
  echo Reinstall: uninstall then install again...
  "%HDC_BIN%" -t %TARGET_ID% shell bm uninstall -n %BUNDLE_NAME% 2>nul
  timeout /t 5 /nobreak >nul
  "%HDC_BIN%" -t %TARGET_ID% shell bm install -p "%REMOTE_HAP_PATH%"
  timeout /t 5 /nobreak >nul
)

REM ====================== Launch App ======================
echo [Harmony-Debug-Pipeline] Run: aa start -a %ABILITY_NAME% -b %BUNDLE_NAME% (DEBUG_MODE=%DEBUG_MODE%; debug uses -D)
"%HDC_BIN%" -t %TARGET_ID% shell rm -rf %REMOTE_HAP_DIR%
timeout /t 1 /nobreak >nul
echo Launching app...
echo   If device screen is locked, please unlock it manually.
echo.
if "%DEBUG_MODE%"=="debug" (
  echo   Debug mode, starting with -D flag
  "%HDC_BIN%" -t %TARGET_ID% shell aa start -a %ABILITY_NAME% -b %BUNDLE_NAME% -D
) else (
  echo   Attach mode, starting without -D flag
  "%HDC_BIN%" -t %TARGET_ID% shell aa start -a %ABILITY_NAME% -b %BUNDLE_NAME%
)



echo ------------------------------------------------------------
echo Build, install and app launch completed!
echo.
echo App info:
echo   - Bundle: %BUNDLE_NAME%
echo   - Device: %TARGET_ID%
set /a ELAPSED=HAP_START_TIME-START_TIME
set /a MINUTES=ELAPSED/60
set /a SECONDS=ELAPSED%%60
echo   - SO build time: !MINUTES!m!SECONDS!s (!ELAPSED!s)
set /a ELAPSED=END_TIME-HAP_START_TIME
set /a MINUTES=ELAPSED/60
set /a SECONDS=ELAPSED%%60
echo   - HAP package and push time: !MINUTES!m!SECONDS!s (!ELAPSED!s)
set /a ELAPSED=END_TIME-START_TIME
set /a MINUTES=ELAPSED/60
set /a SECONDS=ELAPSED%%60
echo   - Total time: !MINUTES!m!SECONDS!s (!ELAPSED!s)
echo ------------------------------------------------------------
exit /b 0

:show_help
echo Usage: %~nx0 [options] [PLATFORM] [TARGET_ID]
echo.
echo Parameters:
echo   PLATFORM    Build platform (default: %DEFAULT_PLATFORM%)
echo   TARGET_ID   Device ID (default: %DEFAULT_TARGET_ID%)
echo.
echo Options:
echo   -m MODE    Build mode: debug or release (default: debug)
echo   -b BUNDLE   Set bundle name (current: %DEFAULT_BUNDLE_NAME%)
echo   -a ABILITY  Set Ability name (current: %DEFAULT_ABILITY_NAME%)
echo   -d MODE     Debug mode: debug or attach (default: attach)
echo   -p PATH     Set external OHOS project path (localOhosPath)
echo   -h          Show this help
echo.
echo Examples:
echo   %~nx0 ohosArm64 127.0.0.1:5555
echo   %~nx0 -m release
echo   %~nx0 -b com.test.app -a MainAbility
echo   %~nx0 -p D:\path\to\external\ohos\project
exit /b 0
