package com.superbaidumapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CircleFrameLayout extends FrameLayout {

    public CircleFrameLayout(@NonNull Context context) {
        super(context);
    }

    public CircleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        //添加一个圆形的路径，Path.Direction.CW代表顺时针方向
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), getMeasuredWidth()/2, getMeasuredWidth()/2, Path.Direction.CW);
        //画布按照路线裁剪
//        Region.Op.DIFFERENCE
//        -- 得到的区域    ->在A中，与B不相同的部分
//        Region.Op.REVERSE_DIFFERENCE
//        -- 得到的区域    ->在B中，与A不相同的部分
//        Region.Op.INTERSECT
//        -- 得到的区域    ->A与B相同的部分（交集）
//        Region.Op.XOR
//        -- 得到的区域    ->不包含A与B相同的部分（全集减去交集）
//        Region.Op.UNION
//        -- 得到的区域    ->A与B的所有部分(全集)
//        Region.Op.REPLACE
//        -- 得到的区域    ->将A的区域用B替换
        canvas.clipPath(path, Region.Op.REPLACE);
        super.dispatchDraw(canvas);
    }

}
