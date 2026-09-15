#!/bin/bash
#
# Eazytec is pleased to support the open source community by making CPF-KMP-CMP available.
# Copyright (C) 2026 Eazytec. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#
set -e

# Record script start time
START_TIME=$(date +%s)

# ====================== [1. Default Configuration and Parameter Parsing] ======================
DEFAULT_PLATFORM="ohosArm64"
DEFAULT_TARGET_ID="127.0.0.1:5555"
DEFAULT_BUNDLE_NAME="com.example.harmonyapp"
DEFAULT_ABILITY_NAME="EntryAbility"
LOCAL_OHOS_PATH=""

usage() {
    echo "Usage: $0 [options] [PLATFORM] [TARGET_ID]"
    echo ""
    echo "Parameters:"
    echo "  PLATFORM      Build platform (default: $DEFAULT_PLATFORM)"
    echo "  TARGET_ID     Device ID (default: $DEFAULT_TARGET_ID)"
    echo ""
    echo "Options:"
    echo "  -m MODE       Build mode: debug or release (default: debug)"
    echo "  -b BUNDLE     Set bundle name (current: $DEFAULT_BUNDLE_NAME)"
    echo "  -a ABILITY    Set Ability name (current: $DEFAULT_ABILITY_NAME)"
    echo "  -d MODE       Debug mode: debug or attach (default: attach)"
    echo "  -p PATH       Set external OHOS project path (localOhosPath)"
    echo "  -h            Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 ohosArm64 127.0.0.1:5555"
    echo "  $0 -m release                  # Release build"
    echo "  $0 -b com.test.app -a MainAbility"
    echo "  $0 -p /path/to/external/ohos/project"
    exit 0
}

# Preset variables
BUNDLE_NAME=$DEFAULT_BUNDLE_NAME
ABILITY_NAME=$DEFAULT_ABILITY_NAME
BUILD_MODE="debug"
DEBUG_MODE="attach"
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'   # No Color
# Parse options (supports options in any position, e.g. ohosArm64 62Q0226107024702 -m release)
POSITIONALS=()
while [[ $# -gt 0 ]]; do
    case $1 in
        -m) BUILD_MODE="$2"; shift 2 ;;
        -b) BUNDLE_NAME="$2"; shift 2 ;;
        -a) ABILITY_NAME="$2"; shift 2 ;;
        -d) DEBUG_MODE="$2"; shift 2 ;;
        -p) LOCAL_OHOS_PATH="$2"; shift 2 ;;
        -h) usage ;;
        -*)
            echo "Unknown option: $1"
            usage
            ;;
        *)
            POSITIONALS+=("$1")
            shift
            ;;
    esac
done

PLATFORM=${POSITIONALS[0]:-$DEFAULT_PLATFORM}
TARGET_ID=${POSITIONALS[1]:-$DEFAULT_TARGET_ID}

echo -e "\033[32m Run environment configuration:\033[0m"
echo "  - Platform: $PLATFORM"
echo "  - Device: $TARGET_ID"
echo "  - Build mode: $BUILD_MODE"
echo "  - Debug mode: $DEBUG_MODE"
echo "  - Bundle: $BUNDLE_NAME"
echo "  - Ability: $ABILITY_NAME"
if [ -n "$LOCAL_OHOS_PATH" ]; then
    echo "  - External OHOS path: $LOCAL_OHOS_PATH"
fi
echo "------------------------------------------------------------"

# --- Configuration ---
DEVECO_PATH="${DEVECO_PATH:-/Applications/DevEco-Studio.app}"
MIN_VERSION="6.0.0"
echo " DevEco Studio Path ${DEVECO_PATH}"
echo "Checking environment configuration..."

# 1. Check if DevEco exists
if [ ! -d "$DEVECO_PATH" ]; then
    echo " Error: DevEco Studio not found at $DEVECO_PATH."
    exit 1
fi

# 2. Get version (from Info.plist CFBundleShortVersionString)
# Output example: 5.0.3.406
CURRENT_VERSION=$(defaults read "$DEVECO_PATH/Contents/Info.plist" CFBundleShortVersionString)

echo "Current DevEco version: $CURRENT_VERSION"
echo "Minimum required version: $MIN_VERSION"
CUR_MAJOR=$(echo "$CURRENT_VERSION" | cut -d. -f1)
CUR_MINOR=$(echo "$CURRENT_VERSION" | cut -d. -f2)

REQ_MAJOR=$(echo "$MIN_VERSION" | cut -d. -f1)
REQ_MINOR=$(echo "$MIN_VERSION" | cut -d. -f2)

if [ "$CUR_MAJOR" -lt "$REQ_MAJOR" ] 2>/dev/null || \
   { [ "$CUR_MAJOR" -eq "$REQ_MAJOR" ] && [ "$CUR_MINOR" -lt "$REQ_MINOR" ]; }; then
echo "-------------------------------------------------------"
echo -e "${RED} Error: DevEco version is too low!${NC}"
echo -e "${RED}Current version: $CURRENT_VERSION${NC}"
echo -e "${RED}Minimum required: $MIN_VERSION+${NC}"
echo -e "${RED}Please upgrade DevEco Studio and run this script again.${NC}"
echo "-------------------------------------------------------"
exit 5
fi

echo "DevEco version meets requirements ($CURRENT_VERSION)"
# ====================== [2. Environment Path and SDK Configuration] ======================
DEVECO_HOME="${DEVECO_HOME:-$DEVECO_PATH/Contents}"
echo " ($DEVECO_HOME)"
HDC_BIN="$DEVECO_HOME/sdk/default/openharmony/toolchains/hdc"
export DEVECO_SDK_HOME="$DEVECO_HOME/sdk"
export PATH="$DEVECO_SDK_HOME:$DEVECO_HOME/jbr/Contents/Home/bin:$DEVECO_HOME/tools/node/bin:$DEVECO_HOME/tools/ohpm/bin:$DEVECO_HOME/tools/hvigor/bin:$PATH"
"$HDC_BIN" -t "$TARGET_ID" shell aa force-stop "$BUNDLE_NAME" >/dev/null 2>&1 || true
echo "[Harmony-Debug-Pipeline] Prep: hdc force-stop $BUNDLE_NAME (ignore errors if not running)"

# ====================== [3. Gradle Build] ======================
echo "[Harmony-Debug-Pipeline] Build: Gradle KMP publishBinariesToHarmonyApp (mode=$BUILD_MODE; first run slower, later incremental)"
echo "Working path: $(pwd)"
echo "Building OpenHarmony ARM64..."
if [ "$PLATFORM" = "ohosArm64" ]; then
    # Execute Gradle build in project root
    if [ "$BUILD_MODE" = "release" ]; then
        if [ -n "$LOCAL_OHOS_PATH" ]; then
            echo "Using external OHOS path: $LOCAL_OHOS_PATH"
            echo "/gradlew :composeApp:publishReleaseBinariesToHarmonyApp"
            ./gradlew :composeApp:publishReleaseBinariesToHarmonyApp -PharmonyAppPath="$LOCAL_OHOS_PATH"
        else
            echo "/gradlew :composeApp:publishReleaseBinariesToHarmonyApp"
            ./gradlew :composeApp:publishReleaseBinariesToHarmonyApp
        fi
    else
        if [ -n "$LOCAL_OHOS_PATH" ]; then
            echo "Using external OHOS path: $LOCAL_OHOS_PATH"
            ./gradlew :composeApp:publishDebugBinariesToHarmonyApp -PharmonyAppPath="$LOCAL_OHOS_PATH"
        else
            echo "/gradlew :composeApp:publishDebugBinariesToHarmonyApp"
            ./gradlew :composeApp:publishDebugBinariesToHarmonyApp
        fi
    fi
elif [ "$PLATFORM" = "iosSimulatorArm64" ]; then
    ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
else
    echo -e "\033[31mError: Unsupported platform '$PLATFORM'\033[0m"
    exit 4
fi
echo "[Harmony-Debug-Pipeline] Build: KMP publish finished — next ohpm/hvigor HAP (incremental builds may be faster)"
# SO packaging end time
HAP_START_TIME=$(date +%s)
# --- Subsequent install logic (hdc install etc.) ---
# ====================== [4. HAP Package Build] ======================
# Switch to harmonyApp directory for subsequent OHOS commands
# Use external path if specified, otherwise use default harmonyApp directory
if [ -n "$LOCAL_OHOS_PATH" ]; then
    HARMONY_APP_DIR="$LOCAL_OHOS_PATH"
else
    HARMONY_APP_DIR="harmonyApp"
fi

if [ ! -d "$HARMONY_APP_DIR" ]; then
    echo -e "\033[31mError: harmonyApp directory not found: $HARMONY_APP_DIR\033[0m"
    exit 4
fi
cd "$HARMONY_APP_DIR"
echo "Switched to harmonyApp directory: $(pwd)"
echo "Running Hvigor sync and HAP packaging (buildMode=$BUILD_MODE)..."
ohpm install --all
node "$DEVECO_HOME/tools/hvigor/bin/hvigorw.js" --sync -p product=default -p buildMode="$BUILD_MODE" --analyze=normal --parallel --incremental --daemon
if [ "$BUILD_MODE" = "release" ]; then
    node "$DEVECO_HOME/tools/hvigor/bin/hvigorw.js" --mode module -p module=entry -p product=default -p buildMode=release -p requiredDeviceType=phone assembleHap compileNative --analyze=normal --parallel --incremental --daemon
else
    node "$DEVECO_HOME/tools/hvigor/bin/hvigorw.js" --mode module -p module=entry@default -p product=default -p buildMode=debug -p requiredDeviceType=phone assembleHap --analyze=normal --parallel --incremental --daemon
fi
echo "[Harmony-Debug-Pipeline] Build: HAP assemble finished"

# ====================== [5. Install and Push Debug Components] ======================
AVAILABLE_TARGETS=$("$HDC_BIN" list targets)
HAP_DIR="./entry/build/default/outputs/default"
SIGNED_HAP="entry-default-signed.hap"
HAP_FILE="$HAP_DIR/$SIGNED_HAP"
echo "Package path: $HAP_DIR/$SIGNED_HAP"

# Check if signed HAP package exists
if [ ! -f "$HAP_FILE" ]; then
    echo -e "\033[31mError: Signed HAP package not found!\033[0m"
    echo ""
    echo -e "\033[33mExpected path:\033[0m"
    echo "  $HARMONY_APP_DIR/entry/build/default/outputs/default/entry-default-signed.hap"
    echo ""
    echo -e "\033[33mPossible causes:\033[0m"
    echo "  • HAP package has not been built yet"
    echo "  • Build process failed"
    echo "  • HAP package is not signed"
    echo ""
    echo -e "\033[36mSuggested actions:\033[0m"
    echo "  1. Check the build logs above for errors"
    echo "  2. Verify DevEco Studio signing configuration is correct"
    echo "  3. Manually build and sign HAP package in DevEco Studio"
    echo "  4. Check files in entry/build/default/outputs/default/ directory"
    exit 6
fi
AVAILABLE_TARGETS=$("$HDC_BIN" list targets)
if ! echo "$AVAILABLE_TARGETS" | grep -q "$TARGET_ID"; then
    echo -e "\033[31mError: Device $TARGET_ID is offline!\033[0m"
    exit 5
fi

echo "[Harmony-Debug-Pipeline] Install: bm install signed HAP (may uninstall/reinstall if a prior install failed)"
echo "Pushing debug components and installing HAP..."
echo "  - Device: $TARGET_ID"
# Install HAP (using temp directory)
REMOTE_HAP_DIR="/data/local/tmp/debug_install"
"$HDC_BIN" -t "$TARGET_ID" shell mkdir -p "$REMOTE_HAP_DIR"
"$HDC_BIN" -t "$TARGET_ID" file send "$HAP_FILE" "$REMOTE_HAP_DIR"

INSTALL_OUTPUT=$("$HDC_BIN" -t "$TARGET_ID" shell bm install -p "$REMOTE_HAP_DIR/$SIGNED_HAP" 2>&1)
sleep 1
INSTALL_RESULT=$?
echo "Install status: $INSTALL_OUTPUT"
if echo "$INSTALL_OUTPUT" | grep -qE "failed"; then
    echo "Install failed detected, uninstalling and reinstalling..."
    "$HDC_BIN" -t "$TARGET_ID" shell bm uninstall -n "$BUNDLE_NAME" >/dev/null 2>&1 || true
    sleep 5
    INSTALL_OUTPUT=$("$HDC_BIN" -t "$TARGET_ID" shell bm install -p "$REMOTE_HAP_DIR/$SIGNED_HAP" 2>&1)
    sleep 5
fi
# ====================== [6. App Launch and Debug Mount] ======================
echo "[Harmony-Debug-Pipeline] Run: aa start -a $ABILITY_NAME -b $BUNDLE_NAME (DEBUG_MODE=$DEBUG_MODE; debug uses -D)"
echo -e "\033[33mLaunching app and starting debug listener...\033[0m"
"$HDC_BIN" -t "$TARGET_ID" shell rm -rf "$REMOTE_HAP_DIR"
sleep 1
# Get system version
#SYSTEM_VERSION=$($HDC_BIN -t $TARGET_ID shell param get const.ohos.apiversion 2>/dev/null || echo "unknown")
#echo "Detected system version: $SYSTEM_VERSION"

# Check screen lock status and prompt
echo ""
echo -e "\033[33m  Important:\033[0m"
echo -e "  If device screen is locked, please unlock it manually"
echo -e "  System cannot auto-unlock in developer mode (security restriction)"
echo ""

# Step 1: Start app (with -D if debug mode enabled)
echo "  -> Executing aa start (launch app)..."
if [ "$DEBUG_MODE" = "debug" ]; then
    echo "  -> Debug mode, starting with -D flag"
    AA_START_OUTPUT=$("$HDC_BIN" -t "$TARGET_ID" shell aa start -a "$ABILITY_NAME" -b "$BUNDLE_NAME" -D 2>&1)
else
    echo "  -> Attach mode, starting without -D flag"
    AA_START_OUTPUT=$("$HDC_BIN" -t "$TARGET_ID" shell aa start -a "$ABILITY_NAME" -b "$BUNDLE_NAME" 2>&1)
fi
# Package push complete time
END_TIME=$(date +%s)

AA_START_RESULT=$?
# Check if screen lock error
if echo "$AA_START_OUTPUT" | grep -q "10106102\|screen is locked"; then
    echo -e "\033[31mError: Device screen is locked!\033[0m"
    echo ""
    echo -e "\033[33mPlease follow these steps:\033[0m"
    echo "  1. Manually unlock device screen"
    echo "  2. Keep screen awake (recommended for dev: Settings -> Display & Brightness -> Screen timeout -> Never)"
    echo "  3. Run this script again"
    echo ""
    echo -e "\033[36mNote: Auto-unlock is not available in developer mode (system security restriction)\033[0m"
    exit 1
fi
# If start failed but not screen lock error, continue anyway
if [ $AA_START_RESULT -ne 0 ]; then
    echo -e "\033[33m  App start command returned non-zero exit code, but continuing...\033[0m"
fi
echo "------------------------------------------------------------"
echo -e "\033[32mBuild, install and app launch completed!\033[0m"

# Calculate total duration

ELAPSED_TIME=$((END_TIME - START_TIME))
MINUTES=$((ELAPSED_TIME / 60))
SECONDS=$((ELAPSED_TIME % 60))

echo -e "App info:"
echo -e "  - Bundle: $BUNDLE_NAME"
echo -e "  - Device: $TARGET_ID"
ELAPSED_TIME=$((HAP_START_TIME - START_TIME))
MINUTES=$((ELAPSED_TIME / 60))
SECONDS=$((ELAPSED_TIME % 60))
echo -e "  - SO build time: ${MINUTES}m${SECONDS}s (${ELAPSED_TIME}s)"
ELAPSED_TIME=$((END_TIME - HAP_START_TIME))
MINUTES=$((ELAPSED_TIME / 60))
SECONDS=$((ELAPSED_TIME % 60))
echo -e "  - HAP package and push time: ${MINUTES}m${SECONDS}s (${ELAPSED_TIME}s)"
ELAPSED_TIME=$((END_TIME - START_TIME))
MINUTES=$((ELAPSED_TIME / 60))
SECONDS=$((ELAPSED_TIME % 60))
echo -e "  - Total time: ${MINUTES}m${SECONDS}s (${ELAPSED_TIME}s)"
# ====================== 【6. Keep alive】 ======================
echo ""
echo -e "\033[32m▶ App running (click Stop button to end)...\033[0m"
echo ""