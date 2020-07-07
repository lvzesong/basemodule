package com.base.basemodule.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.text.DecimalFormat;

public class FileUtil {
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }


    // 递归方式 计算文件的大小
    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

    /**
     * 递归删除文件夹
     *
     * @param file
     */
    public static void deleteDir(final File file) {
        try {
            if (file.isFile())
                file.delete();
            else {
                final File[] children = file.listFiles();
                if (children != null) {
                    for (File file2 : children) {
                        if (file2.isFile()) {
                            file2.delete();
                        } else {
                            deleteDir(file2);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }



    public static void deleteFiles(final File file) {
        try {
            if (file.isFile())
                file.delete();
            else {
                final File[] children = file.listFiles();
                if (children != null) {
                    for (File file2 : children) {
                        if (file2.isFile()) {
                            file2.delete();
                        } else {
                            deleteFiles(file2);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static void deleteFile(String path) {
        try {
            if(TextUtils.isEmpty(path)){
                return;
            }
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            //Log.d("lzs", "删除文件:" + e.getMessage());
        }
    }

    /**
     * 获取可读的文件大小
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;
        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * 获取文件的文件名(不包括扩展名)
     */
    public static String getFileNameWithoutExtension(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        if (separatorIndex < 0) {
            separatorIndex = 0;
        }
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0) {
            dotIndex = path.length();
        } else if (dotIndex < separatorIndex) {
            dotIndex = path.length();
        }
        return path.substring(separatorIndex + 1, dotIndex);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 获取扩展名
     */
    public static String getExtension(String path) {
        if (path == null) {
            return null;
        }
        int dot = path.lastIndexOf(".");
        if (dot >= 0) {
            return path.substring(dot);
        } else {
            return "";
        }
    }

    /**
     * 检测SD卡是否可用
     *
     * @return
     */
    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    public static File getUriFile(Context context, Uri uri) {
        String path = getUriPath(context, uri);
        if (path == null) {
            return null;
        }
        return new File(path);
    }

    public static String getUriPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
