package com.gnaix.game.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    //资源
    private Resources mResources;
    //游戏模型
    private Game mGame;

    //rectangle
    private Drawable backgroundRectangle;
    private Drawable lightUpRectangle;

    //时间
    private long lastFPSTime = System.nanoTime();
    private long currentTiem = System.nanoTime();

    public MainView(Context context) {
        super(context);
        Log.d(TAG, "MainView");

        mResources = context.getResources();
        mGame = new Game(context, this);

        //加载资源
        backgroundRectangle = mResources.getDrawable(R.drawable.background_rectangle);
        lightUpRectangle = mResources.getDrawable(R.drawable.light_up_rectangle);

        Log.d(TAG, "end MainView");
    }

    private Bitmap background = null;
    /** 画笔 */
    private Paint mPaint = new Paint();

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
        createBackgroundBitmp();

        Log.d(TAG, "end onSizeChanged");
    }

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
        startingX = (int) (boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        endingX = (int) (boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        startingY = (int) (boardMiddleY -(cellSize + gridWidth) * halfNumSquaresY + gridWidth /2);
        endingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY - gridWidth /2);

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

    private void createBitmapCells(){
        
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
