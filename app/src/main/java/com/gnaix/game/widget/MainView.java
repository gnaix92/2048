package com.gnaix.game.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.gnaix.game.R;
import com.gnaix.game.model.MainGame;

/**
 * 名称: MainView
 * 描述: 主页面
 *
 * @author xiangqing.xue
 * @date 15/9/22
 */
public class MainView extends View {
    //是否保存状态
    public static boolean hasSaveState = false;

    //
    private Resources mResources;

    //
    private MainGame mMainGame;

    //rectangle
    private Drawable backgroundRectangle;
    private Drawable lightUpRectangle;

    public MainView(Context context) {
        super(context);

        mResources = context.getResources();
        mMainGame = new MainGame(context, this);

        //加载资源
        backgroundRectangle = mResources.getDrawable(R.drawable.background_rectangle);
        lightUpRectangle = mResources.getDrawable(R.drawable.light_up_rectangle);


    }

    private Bitmap background = null;
    private Paint mPaint = new Paint();

    /**
     * 页面大小变化时调用（初始化变量）
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    /**
     * 绘制页面
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, mPaint);
        drawScoreText(canvas);
    }

    /**
     * 绘制分数区域
     * @param canvas
     */
    private void drawScoreText(Canvas canvas) {

    }



}
