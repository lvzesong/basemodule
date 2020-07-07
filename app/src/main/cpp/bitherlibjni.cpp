/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "bitherlibjni.h"
#include <string.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <stdio.h>
#include <setjmp.h>
#include <math.h>
#include <stdint.h>
#include <time.h>

//统一编译方式
extern "C" {
#include "jpeg/jpeglib.h"
#include "jpeg/cdjpeg.h"		/* Common decls for cjpeg/djpeg applications */
#include "jpeg/jversion.h"		/* for version message */
#include "jpeg/android/config.h"
}


#define LOG_TAG "jni"
#define LOGW(...)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define true 1
#define false 0

typedef uint8_t BYTE;

char *error;
struct my_error_mgr {
  struct jpeg_error_mgr pub;
  jmp_buf setjmp_buffer;
};

typedef struct my_error_mgr * my_error_ptr;

METHODDEF(void)
my_error_exit (j_common_ptr cinfo)
{
  my_error_ptr myerr = (my_error_ptr) cinfo->err;
  (*cinfo->err->output_message) (cinfo);
  error=(char*)myerr->pub.jpeg_message_table[myerr->pub.msg_code];
  LOGE("jpeg_message_table[%d]:%s", myerr->pub.msg_code,myerr->pub.jpeg_message_table[myerr->pub.msg_code]);
 // LOGE("addon_message_table:%s", myerr->pub.addon_message_table);
//  LOGE("SIZEOF:%d",myerr->pub.msg_parm.i[0]);
//  LOGE("sizeof:%d",myerr->pub.msg_parm.i[1]);
  longjmp(myerr->setjmp_buffer, 1);
}

int generateJPEG(BYTE* data, int w, int h, int quality,
		const char* outfilename, jboolean optimize) {

	struct jpeg_compress_struct jcs;

	struct my_error_mgr jem;
	jcs.err = jpeg_std_error(&jem.pub);
	jem.pub.error_exit = my_error_exit;
	if (setjmp(jem.setjmp_buffer)) {
		return 0;
	}

	//初始化jsc结构体
	jpeg_create_compress(&jcs);
	//打开输出文件 wb:可写byte
	FILE* f = fopen(outfilename, "wb");
	if (f == NULL) {
        LOGE("打开文件失败，无权限或路径无效");
		return 0;
	}
	//设置结构体的文件路径
	jpeg_stdio_dest(&jcs, f);
	jcs.image_width = w;//设置宽高
	jcs.image_height = h;
//	if (optimize) {
//		LOGI("optimize==ture");
//	} else {
//		LOGI("optimize==false");
//	}

	jcs.arith_code = false;
	int nComponent = 3;
	jcs.input_components = nComponent;
	jcs.in_color_space = JCS_RGB;
//	if (nComponent == 1)
//		jcs.in_color_space = JCS_GRAYSCALE;
//	else
//		jcs.in_color_space = JCS_RGB;

	//全部设置默认参数/* Default parameter setup for compression */
	jpeg_set_defaults(&jcs);
	//是否采用哈弗曼表数据计算 品质相差5-10倍
	jcs.optimize_coding = optimize;
	//设置质量
	jpeg_set_quality(&jcs, quality, true);
	jpeg_start_compress(&jcs, TRUE);

	JSAMPROW row_pointer[1];
	int row_stride;
	row_stride = jcs.image_width * nComponent;
	while (jcs.next_scanline < jcs.image_height) {
		row_pointer[0] = &data[jcs.next_scanline * row_stride];

		jpeg_write_scanlines(&jcs, row_pointer, 1);//row_pointer就是一行的首地址，1：写入的行数
	}
	jpeg_finish_compress(&jcs);//结束
	jpeg_destroy_compress(&jcs);//销毁 回收内存
	fclose(f);//关闭文件

	return 1;
}

/**
 * byte数组转C的字符串
 */
char* jstrinTostring(JNIEnv* env, jbyteArray barr) {
	char* rtn = NULL;
	jsize alen = env->GetArrayLength( barr);
	jbyte* ba = env->GetByteArrayElements( barr, 0);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	env->ReleaseByteArrayElements( barr, ba, 0);
	return rtn;
}

jstring Java_com_base_basemodule_utils_BitmapUtils_compressBitmap(JNIEnv* env,
		jclass thiz, jobject bitmapcolor, int w, int h, int quality,
		jbyteArray fileNameStr, jboolean optimize) {
	BYTE *pixelscolor;
	AndroidBitmap_lockPixels(env,bitmapcolor,(void**)&pixelscolor);

	BYTE *data;
	BYTE r,g,b;
	data = (BYTE*)malloc(w*h*3);
	BYTE *tmpdata;
	tmpdata = data;
	int i=0,j=0;
	int color;
	for (i = 0; i < h; ++i) {
		for (j = 0; j < w; ++j) {
			color = *((int *)pixelscolor);//通过地址取值
			//0~255：
//			a = ((color & 0xFF000000) >> 24);
			r = ((color & 0x00FF0000) >> 16);
			g = ((color & 0x0000FF00) >> 8);
			b = ((color & 0x000000FF));
			*data = b;
			*(data+1) = g;
			*(data+2) = r;
			data = data + 3;
			pixelscolor += 4;
		}
	}

	AndroidBitmap_unlockPixels(env,bitmapcolor);
	char* fileName = jstrinTostring(env,fileNameStr);

	int resultCode = generateJPEG(tmpdata,w,h,quality,fileName,optimize);

	if(resultCode ==0){

		jstring result = env->NewStringUTF("-1");
		return result;
	}

	return env->NewStringUTF("1");
}
