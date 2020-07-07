#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jstring JNICALL Java_com_base_basemodule_utils_BitmapUtils_compressBitmap(JNIEnv*,
		jclass, jobject , int , int , int ,
		jbyteArray , jboolean );

#ifdef __cplusplus
}
#endif

