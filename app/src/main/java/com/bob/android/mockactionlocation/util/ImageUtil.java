package com.bob.android.mockactionlocation.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Bob on 2016/4/26.
 */
public class ImageUtil {

  /*  //將bitmap图片转换成byte数组
    public static void loadImage(Context context, ImageView img,String url){
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.image_load_no)
//                .centerCrop()
                .fallback(R.mipmap.image_load_failure);
        if (StringUtils.isEmpty(url)){
            img.setImageResource(R.mipmap.image_load_no);
        }else {
            GlideApp.with(context).load(AppConfig.UPOAD_IMAGE_PATH + url).error(R.mipmap.image_load_failure).apply(options).into(img);
        }}*/
    //將bitmap图片转换成byte数组
    public static byte[] BitmaptoByteArray(Bitmap bitmap){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        return os.toByteArray();
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {
        if (StringUtils.isBlank(filePath)) return "";
        Bitmap bm = getSmallBitmap(filePath);
        bm = rotateBitmapByDegree(bm, getBitmapDegree(filePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (null != bm) {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            return null;
        }
    }
    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
//        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //把String转换成bitmap
    public static Bitmap stringToBitmap(String data) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(data, Base64.NO_WRAP);
//            bitmapArray = Base64.decode(data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }
    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }



    /**
     * 设置水印图片在左上角
     *
     * @param
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 设置水印图片在右下角
     *
     * @param
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到右上角
     *
     * @param
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                dp2px(context, paddingTop));
    }

    /**
     * 设置水印图片到左下角
     *
     * @param
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到中间
     *
     * @param
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    /**
     * 给图片添加文字到左上角
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * 绘制文字到右下角
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param
     * @param
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
                                               int size, int color, int paddingRight, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
                                            int size, int color, int paddingRight, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到左下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    public static Bitmap drawTextToBitmap(Bitmap source, String text1, String text2, String text3) {
        if (text1 == null || text2 == null || text3 == null || source == null) {
            return source;
        } else {
            int bitmapWidth = source.getWidth();
            int bitmapHeight = source.getHeight();
            float textSize = 24;
            int textColor = Color.RED;
            Bitmap newBitmap;
            /**画图片的画笔*/
            Paint bitmapPaint = new Paint();
            /**画文字的画笔*/
            Paint textPaint = new Paint();
            textPaint.setColor(Color.RED);
            /**配文与图片间的距离*/
            float padding = 20;
            /**配文行与行之间的距离*/
            float linePadding = 5;
            //一行可以显示文字的个数
            int lineTextCount = (int) ((source.getWidth() - 10) / textSize) + 3;
            //一共要把文字分为几行
            int line1 = (int) Math.ceil(Double.valueOf(text1.length()) / Double.valueOf(lineTextCount));
            int line2 = (int) Math.ceil(Double.valueOf(text2.length()) / Double.valueOf(lineTextCount));
            int line3 = (int) Math.ceil(Double.valueOf(text3.length()) / Double.valueOf(lineTextCount));
            int line = line1 + line2 + line3;
//新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
//            newBitmap = Bitmap.createBitmap(bitmapWidth,
//                    (int) (bitmapHeight + padding + textSize * line + linePadding * line), Bitmap.Config.ARGB_8888);
            newBitmap = Bitmap.createBitmap(bitmapWidth,
                    bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
//把图片画上来
            canvas.drawBitmap(source, 0, 0, bitmapPaint);

//在图片下边画一个白色矩形块用来放文字，防止文字是透明背景，在有些情况下保存到本地后看不出来
//            textPaint.setColor(Color.WHITE);
//            canvas.drawRect(0, source.getHeight(), source.getWidth(),
//                    source.getHeight() + padding + textSize * line + linePadding * line, textPaint);

//把文字画上来
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);

            Rect bounds = new Rect();

//开启循环直到画完所有行的文字
            //第一行
            for (int i = 0; i < line1; i++) {
                String s;
                if (i == line1 - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                    s = text1.substring(i * lineTextCount, text1.length());
                } else {//不是最后一行
                    s = text1.substring(i * lineTextCount, (i + 1) * lineTextCount);
                }
                //获取文字的字宽高以便把文字与图片中心对齐
//                textPaint.getTextBounds(s, 0, s.length(), bounds);
                int mLeft = source.getWidth() / 2 - bounds.width() / 2;
                //画文字的时候高度需要注意文字大小以及文字行间距
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() -(padding + textSize * line + linePadding * line)  + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
                canvas.drawText(s, 20,
                        source.getHeight() - (padding + textSize * line + linePadding * line) + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
            }
            //第二行
            for (int i = 0; i < line2; i++) {
                String s;
                if (i == line2 - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                    s = text2.substring(i * lineTextCount, text2.length());
                } else {//不是最后一行
                    s = text2.substring(i * lineTextCount, (i + 1) * lineTextCount);
                }
                //获取文字的字宽高以便把文字与图片中心对齐
//                textPaint.getTextBounds(s, 0, s.length(), bounds);
                //画文字的时候高度需要注意文字大小以及文字行间距
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2 + line1 * textSize + line1 * linePadding, textPaint);
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() - (padding + textSize * line + linePadding * line) + padding + i * textSize + i * linePadding + bounds.height() / 2 + line1 * textSize + line1 * linePadding, textPaint);
                canvas.drawText(s, 20,
                        source.getHeight() - (padding + textSize * line + linePadding * line) + padding + i * textSize + i * linePadding + bounds.height() / 2 + line1 * textSize + line1 * linePadding, textPaint);
            }
            //第三行
            for (int i = 0; i < line3; i++) {
                String s;
                if (i == line3 - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                    s = text3.substring(i * lineTextCount, text3.length());
                } else {//不是最后一行
                    s = text3.substring(i * lineTextCount, (i + 1) * lineTextCount);
                }
                //获取文字的字宽高以便把文字与图片中心对齐
//                textPaint.getTextBounds(s, 0, s.length(), bounds);
                //画文字的时候高度需要注意文字大小以及文字行间距
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2 + (line1+line2) * textSize + (line1+line2) * linePadding, textPaint);
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() - (padding + textSize * line + linePadding * line) + padding + i * textSize + i * linePadding + bounds.height() / 2 + (line1 + line2) * textSize + (line1 + line2) * linePadding, textPaint);
                canvas.drawText(s, 20,
                        source.getHeight() - (padding + textSize * line + linePadding * line) + padding + i * textSize + i * linePadding + bounds.height() / 2 + (line1 + line2) * textSize + (line1 + line2) * linePadding, textPaint);
            }

//            for (int i = 0; i < line; i++) {
//                String s;
//                if (i == line - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
//                    s = text.substring(i * lineTextCount, text.length());
//                } else {//不是最后一行
//                    s = text.substring(i * lineTextCount, (i + 1) * lineTextCount);
//                }
//                //获取文字的字宽高以便把文字与图片中心对齐
//                textPaint.getTextBounds(s, 0, s.length(), bounds);
//                //画文字的时候高度需要注意文字大小以及文字行间距
//                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
//                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
//            }
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
            return newBitmap;
        }
    }
    //保存图片到本地路径
    public static File saveImage(Bitmap bmp, String path, String fileName) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    //保存图片到本地路径
    public static boolean saveImageToLocal(Context mContext,Bitmap bmp, String path, String fileName) {
        boolean haveSaved=false;
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            haveSaved= bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return haveSaved;
    }

    /**
     * 根据uri返回路径
     * @param context
     * @param contentURI
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    /**
     * 检查图片是否损坏
     *
     * @param filePath
     * @return
     */
    public static boolean checkImgDamage(String filePath) {
        BitmapFactory.Options options = null;
        if(checkPicType(filePath)){
            if (options == null) {
                options = new BitmapFactory.Options();
            }
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            if (options.mCancel || options.outWidth == -1
                    || options.outHeight == -1) {
                return true;
            }
            return false;
        }else{
            return true;
        }

    }

    /**
     * 检查文件格式是否是图片
     * @param path
     * @return
     */
    public static boolean checkPicType(String path){
        int picTypeIndex = path.lastIndexOf(".");
        String type = path.substring(picTypeIndex+1,path.length());
        if(type.equals("png") || type.equals("jpeg") || type.equals("jpg") || type.equals("bmp")){
            return true;
        }else{
            return false;
        }
    }
}
