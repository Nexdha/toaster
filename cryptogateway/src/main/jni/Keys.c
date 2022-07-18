#include <jni.h>//JNIEXPORT jstring JNICALL


JNIEXPORT jstring JNICALL
Java_com_crypto_cryptogateway_PaymentGatewayActivity_getNativeKey1(JNIEnv *env, jobject thiz) {
    return (*env)->  NewStringUTF(env, "Hai how are you?");
}