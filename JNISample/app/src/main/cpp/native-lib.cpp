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

void crash() {
    int *p = NULL;

//    if (*p == NULL) {
//        printf("##### null point exception");
//        return;
//    }
    printf("%d\n", *p);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_yeungeek_jnisample_NativeHelper_stringFromJNI(JNIEnv *env, jclass jclass1) {
    LOGD("##### from c");
//    crash();

    return env->NewStringUTF("Hello JNI");
}

extern "C" JNIEXPORT void JNICALL
Java_com_yeungeek_jnisample_NativeHelper_callStaticMethod__I
        (JNIEnv *env, jclass jclass1, jint i) {
    LOGD("##### call static method");

    jclass staticClass = env->FindClass("com/yeungeek/jnisample/NativeHelper");
    if (staticClass == NULL) {
        return;
    }

    jmethodID staticMethod = env->GetStaticMethodID(staticClass, "staticMethod",
                                                    "(Ljava/lang/String;)V");
    if (staticMethod == NULL) {
        return;
    }

    jfieldID staticField = env->GetStaticFieldID(staticClass, "value", "Ljava/lang/String;");
    if (staticField == NULL) {
        return;
    }

    jstring newValue = env->NewStringUTF("new Hello JNI");
    env->SetStaticObjectField(staticClass, staticField, newValue);

    jstring str = env->NewStringUTF("call static method from c");
    env->CallStaticVoidMethod(staticClass, staticMethod, str);


    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        jclass newException = env->FindClass("java/lang/Exception");

        env->ThrowNew(newException, "##### new exception");
        LOGD("##### ndk error");

        return;
    }

    LOGD("##### ndk error");

    env->DeleteLocalRef(staticClass);
    env->DeleteLocalRef(str);
    env->DeleteLocalRef(newValue);
}

extern "C" JNIEXPORT void JNICALL
Java_com_yeungeek_jnisample_NativeHelper_callMethod__I(JNIEnv *env, jobject jobject1, jint i) {
    jclass helperClass = env->FindClass("com/yeungeek/jnisample/NativeHelper");
    if (helperClass == NULL) {
        return;
    }

    jmethodID normalMethod = env->GetMethodID(helperClass, "normalMethod", "(Ljava/lang/String;)V");
    if (normalMethod == NULL) {
        return;
    }

    jmethodID constructMethod = env->GetMethodID(helperClass, "<init>", "()V");
    if (constructMethod == NULL) {
        return;
    }

    jobject newInstance = env->NewObject(helperClass, constructMethod);
    if (newInstance == NULL) {
        return;
    }

    jstring newValue = env->NewStringUTF("normal method");
    if (newValue == NULL) {
        return;
    }

    jfieldID localValue = env->GetFieldID(helperClass, "localValue", "Ljava/lang/String;");
    env->SetObjectField(newInstance, localValue, newValue);

    env->CallVoidMethod(newInstance, normalMethod, newValue);


    env->DeleteLocalRef(newValue);
    env->DeleteLocalRef(newInstance);
    env->DeleteLocalRef(helperClass);
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