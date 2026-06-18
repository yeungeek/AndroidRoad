# the minimum version of CMake.
cmake_minimum_required(VERSION 3.5.0)
project(harmonyApp)

set(NATIVERENDER_ROOT_PATH ${CMAKE_CURRENT_SOURCE_DIR})

if(DEFINED PACKAGE_FIND_FILE)
    include(${PACKAGE_FIND_FILE})
endif()

# 按 ABI 选 libkn.so / libkn_api.h（须与 libs/<abi>/libkn.so 来自同一次 K/N link）
 	 if(CMAKE_OHOS_ARCH_ABI STREQUAL "x86_64" OR OHOS_ARCH STREQUAL "x86_64")
 	     set(LIBKN_ABI_DIR "x86_64")
 	 else()
 	     set(LIBKN_ABI_DIR "arm64-v8a")
 	 endif()
 	 

 include_directories(${NATIVERENDER_ROOT_PATH}
 	                     ${NATIVERENDER_ROOT_PATH}/include
 	                     ${NATIVERENDER_ROOT_PATH}/include/${LIBKN_ABI_DIR})


find_package(skikobridge)
add_library(entry SHARED napi_init.cpp)

# Link against ComposeApp shared library to resolve exported symbols
add_library(ComposeApp SHARED IMPORTED)
target_link_libraries(entry PUBLIC libace_napi.z.so)
target_link_libraries(entry PUBLIC libhilog_ndk.z.so)
target_link_libraries(entry PUBLIC libdeviceinfo_ndk.z.so)
target_link_libraries(entry PUBLIC librawfile.z.so)
#target_link_libraries(entry PUBLIC libicu.so)


set_target_properties(ComposeApp PROPERTIES
    IMPORTED_LOCATION "${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/${LIBKN_ABI_DIR}/libkn.so"
)


target_link_libraries(entry PUBLIC ComposeApp)
# 先链接 libkn.so，再链接 compose里面的skikobridge
target_link_libraries(entry PUBLIC compose::skikobridge)
target_link_libraries(entry PUBLIC ${EGL-lib} ${GLES-lib} ${hilog-lib} ${libace-lib} ${libnapi-lib} ${libuv-lib} libc++_shared.so)




