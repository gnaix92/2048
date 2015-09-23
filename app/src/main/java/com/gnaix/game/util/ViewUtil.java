package com.gnaix.game.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * 名称: ViewUtil
 * 描述:
 *
 * @author xiangqing.xue
 * @date 15/9/23
 */
public class ViewUtil {

    /**
     * 计算字体居中
     * @param paint
     * @return
     */
    public static int centerText(Paint paint){
        return (int) (paint.ascent() + paint.descent()) / 2;
    }

    /**
     * 填充绘制区域
     * @param canvas
     * @param draw
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     */
    public static void drawDrawable(Canvas canvas, Drawable draw, int startX, int startY, int endX, int endY){
        draw.setBounds(startX, startY, endX, endY);
        draw.draw(canvas);
    }
}
