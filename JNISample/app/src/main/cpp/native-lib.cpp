#include <jni.h>
#include <string>
#include <android/log.h>

//extern "C" JNIEXPORT jstring JNICALL
//Java_com_yeungeek_jnisample_NativeHelper_stringFromJNI(JNIEnv *env, jclass type) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}


#define TAG "JNI_"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_yeungeek_jnisample_NativeHelper_stringFromJNI(JNIEnv *env, jclass jclass1) {
    LOGD("##### from c");
    return env->NewStringUTF("Hello JNI");
}

//JNICALL
//jstring getFromJni(JNIEnv *env, jobject/*this*/) {
//    std::string hello = "Hello from JNI";
//
//
//    return env->NewStringUTF(hello.c_str());
//}
//
//jint registerMethod(JNIEnv *env) {
//    jclass clazz = env->FindClass("com/yeungeek/jnisample/NativeHelper");
//    if (clazz == NULL) {
//        LOGD("can't find class: com/yeungeek/jnisample/NativeHelper ");
//    }
//
//    LOGD("##### call registerMethod method");
//    JNINativeMethod jniNativeMethod[] = {{"stringFromJNI", "()Ljava/lang/String;", (void *) getFromJni}};
//    return env->RegisterNatives(clazz, jniNativeMethod,
//                                sizeof(jniNativeMethod) / sizeof(jniNativeMethod[0]));
//}
//
//jint JNI_OnLoad(JavaVM *vm, void *reserved) {
//    JNIEnv *env = NULL;
//    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
//        return JNI_ERR;
//    }
//
//    jint result = registerMethod(env);
//    LOGD("RegisterNatives result: %d", result);
//    return JNI_VERSION_1_6;
//}