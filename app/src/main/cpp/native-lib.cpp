#include <jni.h>
#include <string>
#include <fcntl.h>
#include "util_elf.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_android_elf_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_elf_MainActivity_parsingElf(JNIEnv *env, jobject thiz, jstring path) {
    const char* soPath = env->GetStringUTFChars(path, NULL);
    int fd = open(soPath, O_RDONLY | O_NONBLOCK);
    if (fd < 0) {
        perror("ELF: open fail.");
    }
    ElfUtil elf;
    // 开始解析 ELF
    elf.parsingElfHeader(fd);
    env->ReleaseStringUTFChars(path, soPath);
}