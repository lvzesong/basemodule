package com.base.basemodule.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
    public static final String TAG = "lzs";
    /**
     *
     */
    public static final int Defaultquality = 50;

    private static final String success = "1";
    private static final String failed = "-1";


    public static Bitmap loadBitmapRGB_565(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }


    /**
     * 质量压缩，图片大小不会改变，占用内存不变
     *
     * @return
     */
    public static Bitmap qualityCompress(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100 && options >= 10) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 采样压缩，图片大小会变小，占用内存会变小
     *
     * @param imgPath
     * @return
     */
    private static Bitmap simpleCompress(String imgPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        return qualityCompress(bitmap);//压缩好比例大小后再进行质量压缩
    }

    // 图片默认最大分辨率
    private final static int maxImageHeight = 1920;
    private final static int maxImageWidth = 1080;

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return
     * @Description:函数描述
     */
    public static float getRatioSize(int bitWidth, int bitHeight) {
        return getRatioSize(bitWidth, bitHeight, maxImageWidth, maxImageHeight);
    }

    /**
     * 定比例缩放
     *
     * @param bitWidth
     * @param bitHeight
     * @param maxImageWidth
     * @param maxImageHeight
     * @return
     */
    public static float getRatioSize(int bitWidth, int bitHeight, int maxImageWidth, int maxImageHeight) {
        // 缩放比
        float ratio = 1;
        if (bitHeight <= maxImageHeight && bitWidth <= maxImageWidth) {

            return ratio;
        }
        float hScale = bitHeight * 1.0f / maxImageHeight;
        float wScale = bitWidth * 1.0f / maxImageWidth;
        //Log.d("lzs", "hScale:" + hScale + "   wScale:" + wScale);
        if (hScale <= wScale) {
            ratio = wScale;
        } else {
            ratio = hScale;
        }
        return ratio;
    }

    public static Bitmap getBlackWhiteBitmap(@NonNull Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }


    /**
     * 尺寸压缩
     *
     * @param bmp
     * @return
     */

    public static Bitmap resizeCompress(Bitmap bmp, int maxImageWidth, int maxImageHeight) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        // 尺寸压缩倍数,值越大，图片尺寸越小
        float ratio = getRatioSize(w, h, maxImageWidth, maxImageHeight);
        if (ratio == 1) {
            return bmp;
        }
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap((int) (w / ratio), (int) (h / ratio), bmp.getConfig());
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, (int) (bmp.getWidth() / ratio), (int) (bmp.getHeight() / ratio));
        canvas.drawBitmap(bmp, null, rect, null);
        Log.e(TAG, "尺寸压缩 w:" + result.getWidth() + "  h:" + result.getHeight());
        return result;
    }


    public static boolean compressBitmapJNI(String path, String outPutPath) {
        return compressBitmapJNI(path, outPutPath, Defaultquality);
    }

    public static boolean compressBitmapJNI(String path, String outPutPath, int quality) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            //压缩图片
            String result = compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), quality, outPutPath.getBytes(), true);
            bitmap.recycle();
            return result.equals(success);
        } else {
            return false;
        }
    }


    /**
     * 根据A4尺寸的压缩比来生成新图片
     *
     * @param path
     * @param outPutPath
     * @param quality
     * @return
     */
//    public static boolean getA4ScaleCompressBitmap(String path, String outPutPath, int quality) {
//        Bitmap bmp = BitmapFactory.decodeFile(path);
//        if (null != bmp) {
//            boolean flag = false;
//            //先进行A4尺寸压缩
//            Bitmap tempBmp = resizeCompress(bmp, Constants.A4_WIDTH, Constants.A4_HEIGTH);
//            if (bmp != tempBmp) {
//                bmp.recycle();
//            }
//            if (null != tempBmp) {
//                //极限压缩
//                String result = compressBitmap(tempBmp, tempBmp.getWidth(), tempBmp.getHeight(), quality, outPutPath.getBytes(), true);
//
//                if (result.equals(success)) {
//                    flag = true;
//                }
//            }
//            tempBmp.recycle();
//            return flag;
//        } else {
//            Log.d(TAG, "getA4ScaleCompressBitmap bitmap is null");
//            return false;
//        }
//    }

//    public static boolean getA4ScaleCompressBitmap(String path, String outPutPath) {
//        return getA4ScaleCompressBitmap(path, outPutPath, Defaultquality);
//    }

    /**
     * 保存图像
     *
     * @param path
     * @param bmp
     * @return
     */
    public static void saveBmp(final String path, final Bitmap bmp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fop;
                try {
                    fop = new FileOutputStream(path);
                    //实例化FileOutputStream，参数是生成路径
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
                    //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
                    //格式可以为jpg,png,jpg不能存储透明
                    fop.flush();
                    fop.close();
                    //关闭流
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException>" + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException>" + e.getMessage());
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "IllegalArgumentException>" + e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "saveBmp Exception>" + e.getMessage());
                } finally {
                    if (bmp != null) {
                        bmp.recycle();
                    }
                }
            }
        }).start();
    }

    public static boolean saveBmpSynJpeg(final String path, final Bitmap bmp) {
        FileOutputStream fop;
        try {
            fop = new FileOutputStream(path);
            //实例化FileOutputStream，参数是生成路径
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
            //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
            //格式可以为jpg,png,jpg不能存储透明
            fop.flush();
            fop.close();
            //关闭流
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException>" + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e(TAG, "IOException>" + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException>" + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "saveBmp Exception>" + e.getMessage());
            return false;
        } finally {
            if (bmp != null) {
                bmp.recycle();
            }
        }
        return true;
    }

    public static boolean saveBmpSynPNG(final String path, final Bitmap bmp) {
        FileOutputStream fop;
        try {
            fop = new FileOutputStream(path);
            //实例化FileOutputStream，参数是生成路径
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fop);
            //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
            //格式可以为jpg,png,jpg不能存储透明
            fop.flush();
            fop.close();
            //关闭流
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException>" + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e(TAG, "IOException>" + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException>" + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "saveBmp Exception>" + e.getMessage());
            return false;
        } finally {
            if (bmp != null) {
                bmp.recycle();
            }
        }
        return true;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }
//    public static void setJpegDegree(String path,int degree){
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            switch (degree) {
//                case 90:
//                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_ROTATE_90+"");
//                    break;
//                case 180:
//                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_ROTATE_180+"");
//                    break;
//                case 270:
//                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_ROTATE_270+"");
//                    break;
//                default:
//                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL+"");
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 旋转birmap
     *
     * @param path
     * @param rotate
     * @return
     */
    public static Bitmap rotateBimap(String path, int rotate) {
        //Log.d("lzs","path:"+path);
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap bitmap2 = null;
        if (bmp != null) {
            // Log.d("lzs", "bmp is not null");
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bitmap2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            bmp.recycle();
        } else {
            //Log.d("lzs", "bmp is null");
        }
        return bitmap2;
    }

    public static Bitmap rotateBimap(Bitmap bmp, int rotate) {
        //Log.d("lzs","path:"+path);
        Bitmap bitmap2 = null;
        if (bmp != null) {
            // Log.d("lzs", "bmp is not null");
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bitmap2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        } else {
            //Log.d("lzs", "bmp is null");
        }
        return bitmap2;
    }


    public static byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, output);
        byte[] result = output.toByteArray();
        //Log.e("img_size", result.length + "");
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


//
//        FileOutputStream fop;
//        try {
//            fop = new FileOutputStream(outPutPath);
//            //实例化FileOutputStream，参数是生成路径
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fop);
//            //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
//            //格式可以为jpg,png,jpg不能存储透明
//            fop.flush();
//            fop.close();
//            //关闭流
//            return true;
//        } catch (FileNotFoundException e) {
//            //Log.e("lzs", "FileNotFoundException>" + e.getMessage());
//            return false;
//        } catch (IOException e) {
//            //Log.e("lzs", "IOException>" + e.getMessage());
//            return false;
//        } catch (IllegalArgumentException e) {
//           // Log.e("lzs", "IllegalArgumentException>" + e.getMessage());
//            return false;
//        }


    static {
        System.loadLibrary("jpegbither");
        // System.loadLibrary("bitherlib");
    }

    /**
     * jni 极限压缩
     *
     * @param bit
     * @param width
     * @param height
     * @param quality
     * @param outputPath
     * @param optimize
     * @return
     */
    private static native String compressBitmap(Bitmap bit, int width, int height, int quality, byte[] outputPath, boolean optimize);
}
