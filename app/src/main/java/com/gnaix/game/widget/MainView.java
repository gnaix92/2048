package com.gnaix.game.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.gnaix.game.R;
import com.gnaix.game.listener.TouchListener;
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
    public Game mGame;
    public final int numCellTypes = 18;

    //资源
    private Bitmap background = null;
    private Resources mResources;
    private Drawable backgroundRectangle;
    private Drawable lightUpRectangle;
    private Drawable fadeRectangle;
    //单元格
    private BitmapDrawable[] bitmapCells = new BitmapDrawable[numCellTypes];
    //弹窗提示
    private BitmapDrawable loseGameOverlay;
    private BitmapDrawable winGameContinueOverlay;
    private BitmapDrawable winGameFinalOverlay;

    // 画笔
    private Paint mPaint = new Paint();
    private int textColor_black;
    private int textColor_while;
    private int textColor_brown;

    // 单元格大小
    private int cellSize;
    // 分割线宽度
    private int gridWidth;
    // 按钮大小
    public int iconSize;
    // 字体大小
    private float textSize;
    private float cellTextSize;
    private float titleTextSize;
    private int bodyTextSize;
    private int instructionsTextSize;
    private float headerTextSize;
    private float gameOverTextSize;

    //间距
    private int textPaddingSize;
    private int iconPaddingSize;

    // board 坐标
    public int startingX;
    public int endingX;
    public int startingY;
    public int endingY;

    // 计分区坐标
    private int startScoreBoxY;
    private int endScoreBoxY;

    // title 坐标
    private int titleStartYAll;
    // body 坐标
    private int bodyStartYAll;
    // 最高分值标题长度
    private int titleWidthHighScore;
    // 分值标题长度
    private int titleWidthScore;

    // 按钮坐标
    public int iconStartYAll;
    public int newGameStartX;
    public int undoStartX;

    //time
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

        setOnTouchListener(new TouchListener(this));

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
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "start onSizeChanged");

        //计算坐标
        getLayout(w, h);
        //创建单元格
        createBitmapCells();
        //创建背景
        createBackgroundBitmp(w, h);
        //创建弹窗提示
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
        startScoreBoxY = (int) (startingY - cellSize * 1.5);
        titleStartYAll = (int) (startScoreBoxY + textPaddingSize + titleTextSize / 2 - textShiftYAll1);
        bodyStartYAll = (int) (titleStartYAll + textPaddingSize + titleTextSize / 2 + textShiftYAll1 + bodyTextSize / 2 - textShiftYAll2);
        endScoreBoxY = bodyStartYAll + bodyTextSize / 2 + textShiftYAll2 + textPaddingSize;

        //计算按钮坐标
        iconStartYAll = (endScoreBoxY + startingY) / 2 - iconSize / 2;
        newGameStartX = endingX - iconSize;
        undoStartX = newGameStartX - iconSize * 3 / 2 - iconPaddingSize;

        mPaint.setTextSize(titleTextSize);
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
        int[] cellRectangleIds = getCellRectangleIds();
        mPaint.setTextAlign(Paint.Align.CENTER);
        for(int i=0; i<numCellTypes; i++){
            int value = (int) Math.pow(2, i);
            mPaint.setTextSize(cellTextSize);
            float tempTextSize = cellTextSize * cellSize * 0.9f / Math.max(cellSize * 0.9f, mPaint.measureText(String.valueOf(value)));
            mPaint.setTextSize(tempTextSize);
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            ViewUtil.drawDrawable(canvas, mResources.getDrawable(cellRectangleIds[i]), 0, 0, cellSize, cellSize);
            drawCellText(canvas, value, 0, 0);
            bitmapCells[i] = new BitmapDrawable(mResources, bitmap);
        }
    }

    /**
     * 获取单元格样式
     * @return
     */
    private int[] getCellRectangleIds(){
        int[] cellRectangleIds = new int[numCellTypes];
        cellRectangleIds[0] = R.drawable.cell_rectangle;
        cellRectangleIds[1] = R.drawable.cell_rectangle_2;
        cellRectangleIds[2] = R.drawable.cell_rectangle_4;
        cellRectangleIds[3] = R.drawable.cell_rectangle_8;
        cellRectangleIds[4] = R.drawable.cell_rectangle_16;
        cellRectangleIds[5] = R.drawable.cell_rectangle_32;
        cellRectangleIds[6] = R.drawable.cell_rectangle_64;
        cellRectangleIds[7] = R.drawable.cell_rectangle_128;
        cellRectangleIds[8] = R.drawable.cell_rectangle_256;
        cellRectangleIds[9] = R.drawable.cell_rectangle_512;
        cellRectangleIds[10] = R.drawable.cell_rectangle_1024;
        cellRectangleIds[11] = R.drawable.cell_rectangle_2048;
        for(int i=12;i<numCellTypes; i++){
            cellRectangleIds[i] = R.drawable.cell_rectangle_4096;
        }
        return cellRectangleIds;
    }

    /**
     * 绘制单元格数字
     * @param canvas
     * @param value
     * @param sX
     * @param sY
     */
    private void drawCellText(Canvas canvas, int value, int sX, int sY){
        int textShiftY = ViewUtil.centerText(mPaint);
        if(value >= 8){
            mPaint.setColor(textColor_while);
        }else{
            mPaint.setColor(textColor_black);
        }
        canvas.drawText(String.valueOf(value), cellSize/2, cellSize-textShiftY, mPaint);
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
        int headerStartY = startScoreBoxY - textShiftY;
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
                newGameStartX + iconSize - iconPaddingSize,
                iconStartYAll + iconSize - iconPaddingSize);
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

                ViewUtil.drawDrawable(canvas, cellRectangle, sX, sY, eX, eY);
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
     * 绘制各种情况弹窗
     */
    private void createOverlays(){
        Bitmap bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        createEndGameState(canvas, true, true);
        winGameContinueOverlay = new BitmapDrawable(mResources, bitmap);

        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameState(canvas, true, false);
        winGameFinalOverlay = new BitmapDrawable(mResources, bitmap);

        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameState(canvas, false, false);
        loseGameOverlay = new BitmapDrawable(mResources, bitmap);
    }

    /**
     * 绘制弹窗
     * @param canvas
     * @param isWin
     * @param showBtn
     */
    private void createEndGameState(Canvas canvas, boolean isWin, boolean showBtn){
        int width = endingX - startingX;
        int height = endingY - startingY;
        int middleX = width / 2;
        int middleY = height / 2;

        if(isWin){
            lightUpRectangle.setAlpha(127);
            ViewUtil.drawDrawable(canvas, lightUpRectangle, 0, 0, width, height);
            //重置为255
            lightUpRectangle.setAlpha(255);
            mPaint.setAlpha(255);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(textColor_while);
            mPaint.setTextSize(gameOverTextSize);
            int textShiftY = ViewUtil.centerText(mPaint);
            canvas.drawText(mResources.getString(R.string.you_win), middleX, middleY - textShiftY, mPaint);
            //绘制选择按钮
            mPaint.setTextSize(bodyTextSize);
            textShiftY = ViewUtil.centerText(mPaint);
            String text = showBtn ? mResources.getString(R.string.go_on) : mResources.getString(R.string.for_now);
            canvas.drawText(text, middleX, middleY+textPaddingSize*2 - textShiftY*2, mPaint);
        }else{
            fadeRectangle.setAlpha(127);
            ViewUtil.drawDrawable(canvas, fadeRectangle, 0, 0, width, height);
            //重置为255
            fadeRectangle.setAlpha(255);
            mPaint.setAlpha(255);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(textColor_black);
            mPaint.setTextSize(gameOverTextSize);
            int textShiftY = ViewUtil.centerText(mPaint);
            canvas.drawText(mResources.getString(R.string.game_over), middleX, middleY - textShiftY, mPaint);
        }
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
        mPaint.setTextSize(bodyTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        //计算坐标
        int bodyWidthHighScore = (int) mPaint.measureText(String.valueOf(Game.highScore));
        int bodyWidthScore = (int) mPaint.measureText(String.valueOf(Game.score));

        int boxWidthHighScore = Math.max(bodyWidthHighScore, titleWidthHighScore) + textPaddingSize * 2;
        int boxWidthScore = Math.max(bodyWidthScore, titleWidthScore) + textPaddingSize * 2;

        int boxMiddleHighScore = boxWidthHighScore / 2;
        int boxMiddleScore = boxWidthScore / 2;

        int endXHighScore = endingX;
        int startXHighScore = endXHighScore - boxWidthHighScore;

        int endXScore = startXHighScore - textPaddingSize;
        int startXScore = endXScore - boxWidthScore;

        //绘制最高分
        backgroundRectangle.setBounds(startXHighScore, startScoreBoxY, endXHighScore, endScoreBoxY);
        backgroundRectangle.draw(canvas);
        mPaint.setTextSize(titleTextSize);
        mPaint.setColor(textColor_brown);
        canvas.drawText(mResources.getString(R.string.high_score), startXHighScore+boxMiddleHighScore, titleStartYAll, mPaint);
        mPaint.setTextSize(bodyTextSize);
        mPaint.setColor(textColor_while);
        canvas.drawText(String.valueOf(Game.highScore), startXHighScore + boxMiddleHighScore, bodyStartYAll, mPaint);

        //绘制得分
        backgroundRectangle.setBounds(startXScore, startScoreBoxY, endXScore, endScoreBoxY);
        backgroundRectangle.draw(canvas);
        mPaint.setTextSize(titleTextSize);
        mPaint.setColor(textColor_brown);
        canvas.drawText(mResources.getString(R.string.score), startXScore + boxMiddleScore, titleStartYAll, mPaint);
        mPaint.setTextSize(bodyTextSize);
        mPaint.setColor(textColor_while);
        canvas.drawText(String.valueOf(Game.highScore), startXScore + boxMiddleScore, bodyStartYAll, mPaint);
    }


    /**
     * 获取时间
     */
    private void  resyncTime(){
        lastFPSTime = System.nanoTime();
    }

}
