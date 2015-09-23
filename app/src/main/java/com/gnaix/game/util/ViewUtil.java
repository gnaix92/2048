package com.gnaix.game.util;

import android.graphics.Paint;

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
}
