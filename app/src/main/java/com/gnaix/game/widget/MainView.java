package com.gnaix.game.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.ViewUtils;
import android.util.Log;
import android.view.View;

import com.gnaix.game.R;
import com.gnaix.game.model.Game;
import com.gnaix.game.util.ViewUtil;

/**
 * 名称: MainView
 * 描述: 主页面
 *
 * @author xiangqing.xue
 * @date 15/9/22
 */
public class MainView extends View {
    private final static String TAG = "MainView";
    //是否保存状态
    public static boolean hasSaveState = false;
    //游戏模型
    private Game mGame;

    //资源
    private Resources mResources;
    //rectangle
    private Drawable backgroundRectangle;
    private Drawable lightUpRectangle;
    private Drawable fadeRectangle;
    private Bitmap background = null;

    /** 画笔 */
    private Paint mPaint = new Paint();
    private int textColor_black;
    private int textColor_while;
    private int textColor_brown;

    /** 单元格大小 */
    private int cellSize;
    /** 分割线宽度 */
    private int gridWidth;
    /** 图标大小 */
    private int iconSize;
    /** 字体大小 */
    private float textSize;
    private float cellTextSize;
    private float titleTextSize;
    private int bodyTextSize;
    private int instructionsTextSize;
    private float headerTextSize;
    private float gameOverTextSize;
    private int textPaddingSize;
    private int iconPaddingSize;
    /** board 坐标 */
    private int startingX;
    private int endingX;
    private int startingY;
    private int endingY;

    /** 绘制坐标 */
    private int startYAll;
    private int endYAll;

    /** title 坐标*/
    private int titleStartYAll;
    /** body 坐标*/
    private int bodyStartYAll;
    /** 最高分值标题长度 */
    private int titleWidthHighScore;
    /** 分值标题长度 */
    private int titleWidthScore;

    /** 按钮坐标 */
    private int iconStartYAll;
    private int newGameStartX;
    private int undoStartX;

    //时间
    private long lastFPSTime = System.nanoTime();
    private long currentTiem = System.nanoTime();



    /**
     * 构造函数
     * @param context
     */
    public MainView(Context context) {
        super(context);
        Log.d(TAG, "MainView");

        mResources = context.getResources();
        mGame = new Game(context, this);

        //加载资源
        backgroundRectangle = mResources.getDrawable(R.drawable.background_rectangle);
        lightUpRectangle = mResources.getDrawable(R.drawable.light_up_rectangle);
        fadeRectangle = mResources.getDrawable(R.drawable.fade_rectangle);

        textColor_black = mResources.getColor(R.color.text_black);
        textColor_while = mResources.getColor(R.color.text_white);
        textColor_brown = mResources.getColor(R.color.text_brown);

        this.setBackgroundColor(mResources.getColor(R.color.background));

        Typeface font = Typeface.createFromAsset(mResources.getAssets(), "ClearSans-Bold.ttf");
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(font);

        Log.d(TAG, "end MainView");
    }

    /**
     * 页面大小变化时调用（初始化变量）
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw, oldh);
        Log.d(TAG, "start onSizeChanged");

        getLayout(w, h);
        createBitmapCells();
        createBackgroundBitmp(w, h);
        createOverlays();

        Log.d(TAG, "end onSizeChanged");
    }

    /**
     * 计算控件布局坐标
     * @param width
     * @param height
     */
    private void getLayout(int width, int height){
        Log.d(TAG, "start getLayout");

        cellSize = Math.min(width / (Game.numSquaresX +1), height / (Game.numSquaresY + 3));
        gridWidth = cellSize / 7;

        int screenMiddleX = width / 2;
        int screenMiddleY = height /2;
        //面板区域
        int boardMiddleX = screenMiddleX;
        int boardMiddleY = screenMiddleY + cellSize / 2;
        //设置图标大小
        iconSize = cellSize / 2;

        //设置画笔字体居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(cellSize);
        // 其他字体大小
        textSize = cellSize * cellSize / Math.max(cellSize, mPaint.measureText("0000"));
        cellTextSize = textSize;
        titleTextSize = textSize / 3;
        bodyTextSize = (int) (textSize / 1.5);
        instructionsTextSize = (int) (textSize / 1.5);
        headerTextSize = textSize * 2;
        gameOverTextSize = textSize * 2;
        textPaddingSize = (int) (textSize / 3);
        iconPaddingSize = (int) (textSize / 5);

        //计算borad坐标
        double halfNumSquaresX = Game.numSquaresX / 2d;
        double halfNumSquaresY = Game.numSquaresY / 2d;
        startingX = (int) (boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        endingX = (int) (boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        startingY = (int) (boardMiddleY -(cellSize + gridWidth) * halfNumSquaresY - gridWidth /2);
        endingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth /2);

        //设置title字体大小
        mPaint.setTextSize(titleTextSize);
        int textShiftYAll1 = ViewUtil.centerText(mPaint);
        mPaint.setTextSize(bodyTextSize);
        int textShiftYAll2 = ViewUtil.centerText(mPaint);

        //计算分值菜单Y轴值
        startYAll = (int) (startingY - cellSize * 1.5);
        titleStartYAll = (int) (startYAll + textPaddingSize + titleTextSize / 2 - textShiftYAll1);
        bodyStartYAll = (int) (titleStartYAll + textPaddingSize + titleTextSize / 2 + textShiftYAll1 + bodyTextSize / 2 - textShiftYAll2);
        endYAll = bodyStartYAll + bodyTextSize / 2 + textShiftYAll2 + textPaddingSize;

        //计算按钮坐标
        iconStartYAll = (endYAll + startingY) / 2 - iconSize / 2;
        newGameStartX = endingX - iconSize;
        undoStartX = newGameStartX - iconSize * 3 / 2 - iconPaddingSize;

        //计算分值标题长度
        titleWidthHighScore = (int) (mPaint.measureText(getResources().getString(R.string.high_score)));
        titleWidthScore = (int) (mPaint.measureText(getResources().getString(R.string.score)));

        resyncTime();
        Log.d(TAG, "end getLayout, time:" + lastFPSTime);
    }

    /**
     * 绘制单元格
     */
    private void createBitmapCells(){

    }

    /**
     * 绘制页面背景
     * @param width
     * @param height
     */
    private void createBackgroundBitmp(int width, int height){
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);

        drawHeader(canvas);
        drawNewGameButton(canvas, false);
        drawUndoButton(canvas);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
        drawInstructions(canvas);
    }

    /**
     * 绘制title
     * @param canvas
     */
    private void drawHeader(Canvas canvas){
        mPaint.setTextSize(headerTextSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(textColor_black);
        int textShiftY = ViewUtil.centerText(mPaint) * 2;
        int headerStartY = startYAll - textShiftY;
        canvas.drawText(mResources.getString(R.string.header), startingX, headerStartY, mPaint);

    }

    /**
     * 绘制重新开始按钮
     * @param canvas
     * @param lightUp
     */
    private void drawNewGameButton(Canvas canvas, boolean lightUp){
        if(lightUp){
            ViewUtil.drawDrawable(
                    canvas,
                    lightUpRectangle,
                    newGameStartX,
                    iconStartYAll,
                    newGameStartX + iconSize,
                    iconStartYAll + iconSize);
        }else{
            ViewUtil.drawDrawable(
                    canvas,
                    backgroundRectangle,
                    newGameStartX,
                    iconStartYAll,
                    newGameStartX + iconSize,
                    iconStartYAll + iconSize);
        }

        ViewUtil.drawDrawable(
                canvas,
                mResources.getDrawable(R.drawable.ic_action_refresh),
                newGameStartX + iconPaddingSize,
                iconStartYAll + iconPaddingSize,
                newGameStartX +iconSize - iconPaddingSize,
                iconStartYAll +iconSize - iconPaddingSize);
    }

    /**
     * 绘制返回按钮
     * @param canvas
     */
    private void drawUndoButton(Canvas canvas){
        ViewUtil.drawDrawable(
                canvas,
                backgroundRectangle,
                undoStartX,
                iconStartYAll,
                undoStartX + iconSize,
                iconStartYAll + iconSize);

        ViewUtil.drawDrawable(
                canvas,
                mResources.getDrawable(R.drawable.ic_action_undo),
                undoStartX + iconPaddingSize,
                iconStartYAll + iconPaddingSize,
                undoStartX +iconSize - iconPaddingSize,
                iconStartYAll +iconSize - iconPaddingSize);
    }

    /**
     * 绘制borad
     * @param canvas
     */
    private void drawBackground(Canvas canvas){
        ViewUtil.drawDrawable(canvas, backgroundRectangle, startingX, startingY, endingX, endingY);
    }

    /**
     * 绘制单元格边框
     * @param canvas
     */
    private void drawBackgroundGrid(Canvas canvas) {
        Drawable cellRectangle = mResources.getDrawable(R.drawable.cell_rectangle);

        for (int i = 0; i < Game.numSquaresX; i++) {
            for (int j = 0; j < Game.numSquaresY; j++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * i;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * j;
                int eY = sY + cellSize;

                ViewUtil.drawDrawable(
                        canvas,
                        cellRectangle,
                        sX,
                        sY,
                        eX,
                        eY);
            }
        }
    }

    /**
     * 介绍栏
     * @param canvas
     */
    private void drawInstructions(Canvas canvas){
        mPaint.setTextSize(instructionsTextSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        int textShiftY = ViewUtil.centerText(mPaint)*2;

       canvas.drawText(mResources.getString(R.string.instructions), startingX, endingY + textPaddingSize - textShiftY , mPaint);
    }


    /**
     * 绘制页面
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "start onDraw");

        canvas.drawBitmap(background, 0, 0, mPaint);
        drawScoreText(canvas);

        Log.d(TAG, "end onDraw");
    }

    /**
     * 绘制分数区域
     * @param canvas
     */
    private void drawScoreText(Canvas canvas) {

    }


    /**
     * 获取时间
     */
    private void  resyncTime(){
        lastFPSTime = System.nanoTime();
    }

}
