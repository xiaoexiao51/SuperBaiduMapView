package com.superbaidumapview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

/**
 * Created by pc5 on 2016/3/17.
 */
public class ActivityUtils {

    public static void launchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void launchActivity(Context context, Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launchActivity(Context context, Class<?> activity, String key, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        launchActivity(context, activity, bundle);
    }

    public static void launchActivity(Context context, Class<?> activity, String key, double value) {
        Bundle bundle = new Bundle();
        bundle.putDouble(key, value);
        launchActivity(context, activity, bundle);
    }

    public static void launchActivity(Context context, Class<?> activity, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        launchActivity(context, activity, bundle);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        launchActivityForResult(context, activity, requestCode, bundle);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode, String key, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        launchActivityForResult(context, activity, requestCode, bundle);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
        Intent intent = new Intent(context, activity);
        context.startActivityForResult(intent, requestCode);
    }


    public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchService(Context context, Class<?> service) {
        Intent intent = new Intent(context, service);
        context.startService(intent);
    }

    public static void stopService(Context context, Class<?> service) {
        Intent intent = new Intent(context, service);
        context.stopService(intent);
    }

    public static void startInstall(Context context, String apkFilePath) {
        File apkfile = new File(apkFilePath);
        if (apkfile.exists()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            context.startActivity(i);
        }
    }

//    public static void openImageSelector(Activity activity, int REQUEST_CODE) {
//        ImageLoader loader = new ImageLoader() {
//            @Override
//            public void displayImage(Context context, String path, ImageView imageView) {
//                Glide.with(context).load(path).into(imageView);
//            }
//        };
//        ImgSelConfig config = new ImgSelConfig.Builder(activity, loader)
//                // 是否多选
//                .multiSelect(true)
//                // “确定”按钮背景色
//                .btnBgColor(Color.WHITE)
//                // “确定”按钮文字颜色
//                .btnTextColor(Color.parseColor("#DA3651"))
//                // 使用沉浸式状态栏
//                .statusBarColor(Color.parseColor("#B0B0B0"))
//                // 返回图标ResId
//                .backResId(R.drawable.icon_back)
//                // 标题
//                .title("图片")
//                // 标题文字颜色
//                .titleColor(Color.parseColor("#333333"))
//                // TitleBar背景色
//                .titleBgColor(Color.WHITE)
//                // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, 200, 200)
//                .needCrop(false)
//                // 第一个是否显示相机
//                .needCamera(true)
//                // 最大选择图片数量
//                .maxNum(3)
//                .build();
//        ImgSelActivity.startActivity(activity, config, REQUEST_CODE);
//    }

    /**
     * 晒单图片选择器
     */
//    public static void openPictureSelector(Context mContext, int selectMode, boolean enableCrop,
//                                           PictureConfig.OnSelectResultCallback resultCallback) {
//
//        FunctionConfig config = new FunctionConfig();
//        config.setType(LocalMediaLoader.TYPE_IMAGE);
//        config.setCopyMode(FunctionConfig.COPY_MODEL_1_1);
//        config.setCompress(true);
//        config.setEnablePixelCompress(true);
//        config.setEnableQualityCompress(true);
//        config.setMaxSelectNum(3);
//        config.setSelectMode(selectMode);//FunctionConfig.MODE_MULTIPLE
//        config.setShowCamera(true);
//        config.setEnablePreview(true);
//        config.setEnableCrop(enableCrop);
//        config.setPreviewVideo(false);
//        config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
//        config.setRecordVideoSecond(60);// 视频秒数
//        config.setCropW(500);
//        config.setCropH(500);
//        config.setCheckNumMode(false);
//        config.setCompressQuality(100);
//        config.setImageSpanCount(3);
//        config.setCheckNumMode(false); //是否显示QQ选择风格(带数字效果)
////        config.setPreviewColor //预览文字颜色
////        config.setCompleteColor //完成文字颜色
////        config.setPreviewBottomBgColor //预览界面底部背景色
////        config.setBottomBgColor //选择图片页面底部背景色
//
//        if (false) {
//            config.setThemeStyle(ContextCompat.getColor(mContext, R.color.blue));
//            // 可以自定义底部 预览 完成 文字的颜色和背景色
//            if (false) {
//                // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
//                config.setPreviewColor(ContextCompat.getColor(mContext, R.color.white));
//                config.setCompleteColor(ContextCompat.getColor(mContext, R.color.white));
//                config.setPreviewBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//                config.setBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//            }
//        }
//        if (true) {
////            config.setCheckedBoxDrawable(R.drawable.checkbox_selecter);
//        }
//
//        // 先初始化参数配置，在启动相册
//        PictureConfig.init(config);
//        PictureConfig.getPictureConfig().openPhoto(mContext, resultCallback);
//    }

}
