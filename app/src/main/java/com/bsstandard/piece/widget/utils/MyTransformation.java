package com.bsstandard.piece.widget.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : MyTransformation
 * author         : piecejhm
 * date           : 2022/10/29
 * description    : 이미지 Size 가져오기 , 이미지 top,bottom,left,right radius
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/29        piecejhm       최초 생성
 */

public class MyTransformation extends BitmapTransformation {

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    public enum CornerType {
        NONE,
        ALL,
        TOP,
        BOTTOM,
    }

    private int radius;
    private CornerType cornerType;

    public MyTransformation(Context context, int radius) {
        this.radius = radius;
        this.cornerType = CornerType.NONE;
    }

    public MyTransformation(Context context, int radius, CornerType cornerType) {
        this.radius = radius;
        this.cornerType = cornerType;
    }


    /**
     *
     * @param bitmapPool
     * An interface for a pool that allows users to reuse Bitmap objects.
     *
     * @param original
     * Glide로 받아온 이미지
     *
     * @param width
     * ImageView의 넓이
     *
     * @param height
     * ImageView의 높이
     *
     */
    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap original, int width, int height) {

        Log.d("MyTag","imageView 사이즈 width : " + width + " , height : " + height); // imageView 사이즈

        int orgWidth = original.getWidth();
        int orgHeight = original.getHeight();

        Log.d("MyTag","받아온 이미지 사이즈 orgWidth : " + orgWidth + " , orgHeight : " + orgHeight); // 받아온 이미지의 사이즈


        float scaleX = (float) width / orgWidth;
        float scaleY = (float) height / orgHeight;

        float scaledWidth;
        float scaledHeight;

        if(orgWidth >= orgHeight) {
            scaledWidth = scaleY * orgWidth;
            scaledHeight = height;
        } else {
            scaledWidth = width;
            scaledHeight = scaleX * orgHeight;
        }

        Log.d("MyTag","스케일 사이즈 scaledWidth : " + scaledWidth + " , scaledHeight : " + scaledHeight);

        Bitmap result = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        //캔버스 준비
        Canvas canvas = new Canvas(result);

        //크레파스 준비
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        //모서리가 둥근 사각형(destination) 그리기
        switch (cornerType) {

            case ALL: {
                RectF rectF = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rectF, radius, radius, paint);

                //SRC_IN -> source 이미지가 destination 이미지를 덮습니다.
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                break;
            }

            case TOP:{

                Rect rect = new Rect(0, 0, width, height);
                RectF rectF = new RectF(rect);

                canvas.drawRoundRect(rectF, radius, radius, paint);

                //Fill in bottom corner
                Rect bottomRect = new Rect(0, height/2, width, height);
                canvas.drawRect(bottomRect, paint);

                //SRC_IN -> source 이미지가 destination 이미지를 덮습니다.
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                break;
            }

            case BOTTOM: {

                Rect rect = new Rect(0, 0, width, height);
                RectF rectF = new RectF(rect);

                canvas.drawRoundRect(rectF, radius, radius, paint);

                //Fill in top corner
                Rect topRect = new Rect(0, 0, width, height/2);
                canvas.drawRect(topRect, paint);

                //SRC_IN -> source 이미지가 destination 이미지를 덮습니다.
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                break;
            }

            default: {
                break;
            }

        }

        //블랙펜서 비트맵(source) 그리기
        RectF targetRect = new RectF(0, 0, scaledWidth, scaledHeight);
        canvas.drawBitmap(original, null, targetRect, paint);

        return result;
    }
}
