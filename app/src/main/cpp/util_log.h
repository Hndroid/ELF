//
// Created by iroot on 11/4/2021.
//

#ifndef ELF_UTIL_LOG_H
#define ELF_UTIL_LOG_H

#include<android/log.h>

#define LOG_TAG "ELF"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)




#endif //ELF_UTIL_LOG_H
