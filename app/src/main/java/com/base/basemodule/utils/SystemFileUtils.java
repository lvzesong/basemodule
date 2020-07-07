package com.base.basemodule.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by lzs on 2018/11/9 11:32
 * E-Mail Address：343067508@qq.com
 */
public class SystemFileUtils {


    private static boolean initOnce;

    public static SystemFileUtils getInstance() {
        return SingletonHolder.instance;
    }

    //设置初始化 app 文件目录
    public void init(@NonNull Context context, @NonNull String rootPathName) {
        if (!initOnce) {
            initOnce = true;
            SystemFileUtils.rootPathName = rootPathName;
            myPath = getBaseFilePath();
            genaratePath();
        }
    }

    private static class SingletonHolder {
        static final SystemFileUtils instance = new SystemFileUtils();
    }


    public static String getAudio_path() {
        return audio_path;
    }

    public static String getVideo_path() {
        return video_path;
    }

    public static String getImage_path() {
        return image_path;
    }

    public static String getDownload_path() {
        return download_path;//下载;
    }

    public static String getFile_path() {
        return file_path;
    }

    public static String getLog_path() {
        return log_path;
    }

    public static String getTemp_path() {
        return temp_path;
    }

    public static String getThumb_image_path() {
        return thumb_image_path;
    }

    public static String getOther() {
        return other;
    }

    private static String audio_path;//音频
    private static String video_path;//视频
    private static String image_path;//图片
    private static String download_path;//下载
    private static String file_path; //文件
    private static String log_path; //日志
    private static String temp_path; //临时
    private static String thumb_image_path; //缩略图
    private static String other; //

    private static String rootPathName = "";

    private static String myPath = "";

    public static String getBaseFilePath() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootPathName + File.separator;
        return filePath;
    }

    private void genaratePath() {
        audio_path = "audio" + File.separator;//音频
        video_path = "video" + File.separator;//视频
        image_path = "image" + File.separator;//图片
        download_path = "download" + File.separator;//下载
        file_path = "file" + File.separator; //文件
        log_path = "log" + File.separator; //日志
        temp_path = "temp" + File.separator; //临时
        thumb_image_path = "thumb" + File.separator; //缩略图
        other = "other" + File.separator; //
        audio_path = myPath + audio_path;//音频
        video_path = myPath + video_path;//视频
        image_path = myPath + image_path;//图片
        download_path = myPath + download_path;//下载
        file_path = myPath + file_path; //文件
        log_path = myPath + log_path; //日志
        temp_path = myPath + temp_path; //临时
        thumb_image_path = myPath + thumb_image_path; //缩略图
        other = myPath + other; //

        try {
            File file = new File(getBaseFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(audio_path);
            if (!file1.exists()) {
                file1.mkdirs();
                File f = new File(audio_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }
            }
            File file2 = new File(video_path);
            if (!file2.exists()) {
                file2.mkdirs();
                File f = new File(video_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }
            }
            File file3 = new File(image_path);
            if (!file3.exists()) {
                file3.mkdirs();
                File f = new File(image_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file4 = new File(download_path);
            if (!file4.exists()) {
                file4.mkdirs();
                File f = new File(download_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file5 = new File(temp_path);
            if (!file5.exists()) {
                file5.mkdirs();
                File f = new File(temp_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file6 = new File(log_path);
            if (!file6.exists()) {
                file6.mkdirs();
                File f = new File(log_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file7 = new File(other);
            if (!file7.exists()) {
                file7.mkdirs();
                File f = new File(other, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file8 = new File(file_path);
            if (!file8.exists()) {
                file8.mkdirs();
                File f = new File(file_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }
            File file9 = new File(thumb_image_path);
            if (!file9.exists()) {
                file9.mkdirs();
                File f = new File(thumb_image_path, ".nomedia");
                if (!f.exists()) {
                    boolean flag = f.createNewFile();
                }

            }

        } catch (Exception e) {
        }
    }

    public static String getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    public static String relativeExternalStoragePath(String absPath) {
        String path = absPath.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
        return path;
    }

    public static String ramdomTimeName(String suffix) {
        return System.currentTimeMillis() + "." + suffix;
    }


    private Uri getUriForFile(@NonNull Context context, @NonNull File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider
                    .getUriForFile(context.getApplicationContext(), context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


}
