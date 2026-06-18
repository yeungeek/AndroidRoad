#ifndef KONAN_LIBKN_H
#define KONAN_LIBKN_H
#ifdef __cplusplus
extern "C" {
#endif
#ifdef __cplusplus
typedef bool            libkn_KBoolean;
#else
typedef _Bool           libkn_KBoolean;
#endif
typedef unsigned short     libkn_KChar;
typedef signed char        libkn_KByte;
typedef short              libkn_KShort;
typedef int                libkn_KInt;
typedef long long          libkn_KLong;
typedef unsigned char      libkn_KUByte;
typedef unsigned short     libkn_KUShort;
typedef unsigned int       libkn_KUInt;
typedef unsigned long long libkn_KULong;
typedef float              libkn_KFloat;
typedef double             libkn_KDouble;
typedef float __attribute__ ((__vector_size__ (16))) libkn_KVector128;
typedef void*              libkn_KNativePtr;
struct libkn_KType;
typedef struct libkn_KType libkn_KType;

typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Byte;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Short;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Int;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Long;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Float;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Double;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Char;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Boolean;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_Unit;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_UByte;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_UShort;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_UInt;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_kotlin_ULong;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_com_example_cmp_hello_Platform;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_com_example_cmp_hello_Greeting;
typedef struct {
  libkn_KNativePtr pinned;
} libkn_kref_com_example_cmp_hello_OhosPlatform;

extern void* MainArkUIViewController(void* env);
extern libkn_KInt kn_get_render_backend_id();
extern void androidx_compose_ui_arkui_ArkUIViewController_aboutToAppear(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_aboutToDisappear(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_cancelSyncRefresh(void* controllerRef, libkn_KInt refreshId);
extern void androidx_compose_ui_arkui_ArkUIViewController_dispatchHoverEvent(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_dispatchMouseEvent(void* controllerRef);
extern libkn_KBoolean androidx_compose_ui_arkui_ArkUIViewController_dispatchTouchEvent(void* controllerRef, void* nativeTouchEvent, libkn_KBoolean ignoreInteropView);
extern const char* androidx_compose_ui_arkui_ArkUIViewController_getId(void* controllerRef);
extern void* androidx_compose_ui_arkui_ArkUIViewController_getXComponentRender(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_keyboardWillHide(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_keyboardWillShow(void* controllerRef, libkn_KFloat keyboardHeight);
extern libkn_KBoolean androidx_compose_ui_arkui_ArkUIViewController_onBackPress(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onFinalize(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onFocusEvent(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onFrame(void* controllerRef, libkn_KLong timestamp, libkn_KLong targetTimestamp);
extern void androidx_compose_ui_arkui_ArkUIViewController_onKeyEvent(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onPageHide(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onPageShow(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onSurfaceChanged(void* controllerRef, libkn_KInt width, libkn_KInt height);
extern void androidx_compose_ui_arkui_ArkUIViewController_onSurfaceCreated(void* controllerRef, void* xcomponentPtr, libkn_KInt width, libkn_KInt height);
extern void androidx_compose_ui_arkui_ArkUIViewController_onSurfaceDestroyed(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onSurfaceHide(void* controllerRef);
extern void androidx_compose_ui_arkui_ArkUIViewController_onSurfaceShow(void* controllerRef);
extern libkn_KInt androidx_compose_ui_arkui_ArkUIViewController_requestSyncRefresh(void* controllerRef);
extern const char* androidx_compose_ui_arkui_ArkUIViewController_sendMessage(void* controllerRef, const char* type, const char* message);
extern void androidx_compose_ui_arkui_ArkUIViewController_setContext(void* controllerRef, void* context);
extern void androidx_compose_ui_arkui_ArkUIViewController_setEnv(void* controllerRef, void* env);
extern void androidx_compose_ui_arkui_ArkUIViewController_setId(void* controllerRef, const char* id);
extern void androidx_compose_ui_arkui_ArkUIViewController_setLocaleAndStringProvider(void* controllerRef, void* provider);
extern void androidx_compose_ui_arkui_ArkUIViewController_setMessenger(void* controllerRef, void* messenger);
extern void androidx_compose_ui_arkui_ArkUIViewController_setRenderBackendId(void* controllerRef, libkn_KInt backendId);
extern void androidx_compose_ui_arkui_ArkUIViewController_setRootView(void* controllerRef, void* backRootView, void* foreRootView, void* touchableRootView);
extern void androidx_compose_ui_arkui_ArkUIViewController_setUIContext(void* controllerRef, void* uiContext);
extern void androidx_compose_ui_arkui_ArkUIViewController_setXComponentRender(void* controllerRef, void* render);
extern void buildInstance(void* env, void* func);
extern void* createRootRenderNode();
extern void renderNodeDraw(void* env, void* jsCanvas, void* jsNode);
extern void renderNodeNotifyRedraw(void* env, void* jsNode);
extern void setNodeConstructor(void* env, void* func);
extern void setPixelRatio(libkn_KDouble ratio);
extern void androidx_compose_ui_arkui_init(void* env, void* exports);

typedef struct {
  /* Service functions. */
  void (*DisposeStablePointer)(libkn_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libkn_KBoolean (*IsInstance)(libkn_KNativePtr ref, const libkn_KType* type);
  libkn_kref_kotlin_Byte (*createNullableByte)(libkn_KByte);
  libkn_KByte (*getNonNullValueOfByte)(libkn_kref_kotlin_Byte);
  libkn_kref_kotlin_Short (*createNullableShort)(libkn_KShort);
  libkn_KShort (*getNonNullValueOfShort)(libkn_kref_kotlin_Short);
  libkn_kref_kotlin_Int (*createNullableInt)(libkn_KInt);
  libkn_KInt (*getNonNullValueOfInt)(libkn_kref_kotlin_Int);
  libkn_kref_kotlin_Long (*createNullableLong)(libkn_KLong);
  libkn_KLong (*getNonNullValueOfLong)(libkn_kref_kotlin_Long);
  libkn_kref_kotlin_Float (*createNullableFloat)(libkn_KFloat);
  libkn_KFloat (*getNonNullValueOfFloat)(libkn_kref_kotlin_Float);
  libkn_kref_kotlin_Double (*createNullableDouble)(libkn_KDouble);
  libkn_KDouble (*getNonNullValueOfDouble)(libkn_kref_kotlin_Double);
  libkn_kref_kotlin_Char (*createNullableChar)(libkn_KChar);
  libkn_KChar (*getNonNullValueOfChar)(libkn_kref_kotlin_Char);
  libkn_kref_kotlin_Boolean (*createNullableBoolean)(libkn_KBoolean);
  libkn_KBoolean (*getNonNullValueOfBoolean)(libkn_kref_kotlin_Boolean);
  libkn_kref_kotlin_Unit (*createNullableUnit)(void);
  libkn_kref_kotlin_UByte (*createNullableUByte)(libkn_KUByte);
  libkn_KUByte (*getNonNullValueOfUByte)(libkn_kref_kotlin_UByte);
  libkn_kref_kotlin_UShort (*createNullableUShort)(libkn_KUShort);
  libkn_KUShort (*getNonNullValueOfUShort)(libkn_kref_kotlin_UShort);
  libkn_kref_kotlin_UInt (*createNullableUInt)(libkn_KUInt);
  libkn_KUInt (*getNonNullValueOfUInt)(libkn_kref_kotlin_UInt);
  libkn_kref_kotlin_ULong (*createNullableULong)(libkn_KULong);
  libkn_KULong (*getNonNullValueOfULong)(libkn_kref_kotlin_ULong);

  /* User functions. */
  struct {
    struct {
      struct {
        struct {
          struct {
            struct {
              libkn_KType* (*_type)(void);
              libkn_kref_com_example_cmp_hello_Greeting (*Greeting)();
              const char* (*greet)(libkn_kref_com_example_cmp_hello_Greeting thiz);
            } Greeting;
            struct {
              libkn_KType* (*_type)(void);
              const char* (*get_name)(libkn_kref_com_example_cmp_hello_Platform thiz);
            } Platform;
            struct {
              libkn_KType* (*_type)(void);
              libkn_kref_com_example_cmp_hello_OhosPlatform (*OhosPlatform)();
              const char* (*get_name)(libkn_kref_com_example_cmp_hello_OhosPlatform thiz);
            } OhosPlatform;
            libkn_KInt (*com_example_cmp_hello_Greeting$stableprop_getter)();
            libkn_KInt (*com_example_cmp_hello_OhosPlatform$stableprop_getter)();
            libkn_KInt (*com_example_cmp_hello_Greeting$stableprop_getter_)();
            libkn_KInt (*com_example_cmp_hello_OhosPlatform$stableprop_getter_)();
            libkn_KInt (*com_example_cmp_hello_Greeting$stableprop_getter__)();
            libkn_KInt (*com_example_cmp_hello_OhosPlatform$stableprop_getter__)();
            void* (*MainArkUIViewController_)(void* env);
            libkn_KInt (*com_example_cmp_hello_Greeting$stableprop_getter___)();
            libkn_KInt (*com_example_cmp_hello_OhosPlatform$stableprop_getter___)();
            libkn_KInt (*com_example_cmp_hello_Greeting$stableprop_getter____)();
            libkn_KInt (*com_example_cmp_hello_OhosPlatform$stableprop_getter____)();
            libkn_kref_com_example_cmp_hello_Platform (*getPlatform)();
          } cmp_hello;
        } example;
      } com;
      struct {
        struct {
          struct {
            struct {
              struct {
                libkn_KInt (*kn_get_render_backend_id_)();
              } internal;
            } ohos;
          } compose;
        } jetbrains;
      } org;
      struct {
        struct {
          struct {
            struct {
              struct {
                void* (*get_NodeDrawCallback)();
                void (*_Export_ArkUIViewController_aboutToAppear)(void* controllerRef);
                void (*_Export_ArkUIViewController_aboutToDisappear)(void* controllerRef);
                void (*_Export_ArkUIViewController_cancelSyncRefresh)(void* controllerRef, libkn_KInt refreshId);
                void (*_Export_ArkUIViewController_dispatchHoverEvent)(void* controllerRef);
                void (*_Export_ArkUIViewController_dispatchMouseEvent)(void* controllerRef);
                libkn_KBoolean (*_Export_ArkUIViewController_dispatchTouchEvent)(void* controllerRef, void* nativeTouchEvent, libkn_KBoolean ignoreInteropView);
                const char* (*_Export_ArkUIViewController_getId)(void* controllerRef);
                void* (*_Export_ArkUIViewController_getXComponentRender)(void* controllerRef);
                void (*_Export_ArkUIViewController_keyboardWillHide)(void* controllerRef);
                void (*_Export_ArkUIViewController_keyboardWillShow)(void* controllerRef, libkn_KFloat keyboardHeight);
                libkn_KBoolean (*_Export_ArkUIViewController_onBackPress)(void* controllerRef);
                void (*_Export_ArkUIViewController_onFinalize)(void* controllerRef);
                void (*_Export_ArkUIViewController_onFocusEvent)(void* controllerRef);
                void (*_Export_ArkUIViewController_onFrame)(void* controllerRef, libkn_KLong timestamp, libkn_KLong targetTimestamp);
                void (*_Export_ArkUIViewController_onKeyEvent)(void* controllerRef);
                void (*_Export_ArkUIViewController_onPageHide)(void* controllerRef);
                void (*_Export_ArkUIViewController_onPageShow)(void* controllerRef);
                void (*_Export_ArkUIViewController_onSurfaceChanged)(void* controllerRef, libkn_KInt width, libkn_KInt height);
                void (*_Export_ArkUIViewController_onSurfaceCreated)(void* controllerRef, void* xcomponentPtr, libkn_KInt width, libkn_KInt height);
                void (*_Export_ArkUIViewController_onSurfaceDestroyed)(void* controllerRef);
                void (*_Export_ArkUIViewController_onSurfaceHide)(void* controllerRef);
                void (*_Export_ArkUIViewController_onSurfaceShow)(void* controllerRef);
                libkn_KInt (*_Export_ArkUIViewController_requestSyncRefresh)(void* controllerRef);
                const char* (*_Export_ArkUIViewController_sendMessage)(void* controllerRef, const char* type, const char* message);
                void (*_Export_ArkUIViewController_setContext)(void* controllerRef, void* context);
                void (*_Export_ArkUIViewController_setEnv)(void* controllerRef, void* env);
                void (*_Export_ArkUIViewController_setId)(void* controllerRef, const char* id);
                void (*_Export_ArkUIViewController_setLocaleAndStringProvider)(void* controllerRef, void* provider);
                void (*_Export_ArkUIViewController_setMessenger)(void* controllerRef, void* messenger);
                void (*_Export_ArkUIViewController_setRenderBackendId)(void* controllerRef, libkn_KInt backendId);
                void (*_Export_ArkUIViewController_setRootView)(void* controllerRef, void* backRootView, void* foreRootView, void* touchableRootView);
                void (*_Export_ArkUIViewController_setUIContext)(void* controllerRef, void* uiContext);
                void (*_Export_ArkUIViewController_setXComponentRender)(void* controllerRef, void* render);
                void (*buildInstance_)(void* env, void* func);
                void* (*createRootRenderNode_)();
                libkn_KLong (*getCurrentTimeNanos)();
                void (*renderNodeDraw_)(void* env, void* jsCanvas, void* jsNode);
                void (*renderNodeNotifyRedraw_)(void* env, void* jsNode);
                void (*setNodeConstructor_)(void* env, void* func);
                void (*setPixelRatio_)(libkn_KDouble ratio);
                void (*_Export_ArkUIViewInitializer_init)(void* env, void* exports);
              } arkui;
            } ui;
          } export_;
        } compose;
      } androidx;
      struct {
        struct {
          struct {
            struct {
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_array$stableprop_getter)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_drawable$stableprop_getter)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_font$stableprop_getter)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_plurals$stableprop_getter)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_string$stableprop_getter)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_array$stableprop_getter_)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_drawable$stableprop_getter_)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_font$stableprop_getter_)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_plurals$stableprop_getter_)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_string$stableprop_getter_)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_array$stableprop_getter__)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_drawable$stableprop_getter__)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_font$stableprop_getter__)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_plurals$stableprop_getter__)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_string$stableprop_getter__)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_array$stableprop_getter___)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_drawable$stableprop_getter___)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_font$stableprop_getter___)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_plurals$stableprop_getter___)();
              libkn_KInt (*cmp_hello_composeapp_generated_resources_Res_string$stableprop_getter___)();
            } resources;
          } generated;
        } composeapp;
      } cmp_hello;
    } root;
  } kotlin;
} libkn_ExportedSymbols;
extern libkn_ExportedSymbols* libkn_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBKN_H */
