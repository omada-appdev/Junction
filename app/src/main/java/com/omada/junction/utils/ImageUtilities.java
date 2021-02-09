package com.omada.junction.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omada.junction.utils.taskhandler.LiveEvent;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import me.shouheng.compress.Compress;
import me.shouheng.compress.RequestBuilder;
import me.shouheng.compress.listener.CompressListener;
import me.shouheng.compress.strategy.Strategies;
import me.shouheng.compress.strategy.config.ScaleMode;
import me.shouheng.utils.store.PathUtils;


public final class ImageUtilities {

    private static final float PROFILE_PICTURE_HEIGHT = 200;
    private static final float PROFILE_PICTURE_WIDTH = 200;

    private static final float POST_IMAGE_MAX_HEIGHT = 1200;
    private static final float POST_IMAGE_MAX_WIDTH = 900;


    private ImageUtilities(){
        throw new UnsupportedOperationException();
    }

    // returns bitmap if success, returns null if failure

    public static LiveData<LiveEvent<Bitmap>> scaleToProfilePictureGetBitmap(Context context, Bitmap bitmap) {

        MutableLiveData<LiveEvent<Bitmap>> bitmapLiveData = new MutableLiveData<>();

        Compress.Companion
                .with(context, cropToSquare(bitmap))
                .setQuality(100)
                .setTargetDir(FileUtilities.Companion.getTempFilesPath())
                .setCompressListener(new CompressListener() {
                    @Override
                    public void onStart() {
                        // Compression started
                        Log.e("ImageUtils", "Compression started");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // Compression success
                        Log.e("ImageUtils", "Compression success");
                    }

                    @Override
                    public void onError(@NotNull Throwable throwable) {
                        // Compression error
                        Log.e("ImageUtils", "Compression failure");
                    }
                })
                .strategy(Strategies.INSTANCE.compressor())
                .setConfig(Bitmap.Config.ALPHA_8)
                .setMaxHeight(PROFILE_PICTURE_HEIGHT)
                .setMaxWidth(PROFILE_PICTURE_WIDTH)
                .setScaleMode(ScaleMode.SCALE_WIDTH)
                .asBitmap()
                .setCompressListener(new RequestBuilder.Callback<Bitmap>() {
                    @Override
                    public void onStart() {
                        // Bitmap conversion started
                        Log.e("ImageUtils", "Bitmap conversion started");
                    }

                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        // Bitmap conversion success
                        Log.e("ImageUtils", "Bitmap conversion success");
                        bitmapLiveData.setValue(new LiveEvent<>(bitmap));
                    }

                    @Override
                    public void onError(@NotNull Throwable throwable) {
                        // Bitmap conversion failure
                        bitmapLiveData.setValue(new LiveEvent<>(null));
                        Log.e("ImageUtils", "Bitmap conversion failure");
                    }
                }).launch();

        return bitmapLiveData;
    }

    public static LiveData<LiveEvent<File>> scaleToProfilePictureGetFile(Context context, Bitmap bitmap) {
        // TODO integrate the third party compressor library

        MutableLiveData<LiveEvent<File>> fileLiveData = new MutableLiveData<>();

        Compress.Companion
                .with(context, cropToSquare(bitmap))
                .setQuality(100)
                .setTargetDir(FileUtilities.Companion.getTempFilesPath())
                .setCompressListener(new CompressListener() {
                    @Override
                    public void onStart() {
                        // Compression started
                        Log.e("ImageUtils", "Compression started");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // Compression success
                        Log.e("ImageUtils", "Compression success");
                        fileLiveData.setValue(new LiveEvent<>(file));
                        Log.e("Image", file.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NotNull Throwable throwable) {
                        // Compression error
                        throwable.printStackTrace();
                        Log.e("ImageUtils", "Compression failure\n"+throwable.getMessage());
                        fileLiveData.setValue(new LiveEvent<>(null));
                    }
                })
                .strategy(Strategies.INSTANCE.compressor())
                .setConfig(Bitmap.Config.ALPHA_8)
                .setMaxHeight(PROFILE_PICTURE_HEIGHT)
                .setMaxWidth(PROFILE_PICTURE_WIDTH)
                .setScaleMode(ScaleMode.SCALE_WIDTH)
                .launch();

        return fileLiveData;
    }

    private static Bitmap cropToSquare(Bitmap bitmap){

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = Math.min(height, width);
        int newHeight = Math.min(height, width);
        int cropW = (width - height) / 2;
        cropW = Math.max(cropW, 0);
        int cropH = (height - width) / 2;
        cropH = Math.max(cropH, 0);

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

    private static Bitmap cropToRatio(Bitmap bitmap, float heightToWidth){

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        float currentRatio = (float)height/(float)width;

        int newWidth, newHeight, cropW, cropH;

        if(currentRatio > heightToWidth) {
            newWidth = width;
            newHeight = (int) (newWidth * heightToWidth);
            cropW = 0;
            cropH = (height - newHeight)/2;
        } else {
            newHeight = height;
            newWidth = (int) (newHeight / heightToWidth);
            cropH = 0;
            cropW = (width - newWidth)/2;
        }

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }


}
