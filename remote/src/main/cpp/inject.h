#pragma once

#include <jni.h>

void preloadDex(const char * dex_path);
int load_and_invoke_dex(JNIEnv* env, const char *argument);