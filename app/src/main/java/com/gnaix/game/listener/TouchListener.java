package com.gnaix.game.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gnaix.game.widget.MainView;

/**
 * 名称: TouchListener
 * 描述:
 *
 * @author xiangqing.xue
 * @date 15/9/24
 */
public class TouchListener implements View.OnTouchListener {
    private final static String TAG = "TouchListener";
    private MainView mMainView;

    //移动最少距离
    private static final int SWIPE_MIN_DISTANCE = 0;
    //本次移动最少距离
    private static final int SWIPE_THRESHOLD_VELOCITY = 25;
    //移动最少距离（直接滑动）
    private static final int MOVE_THRESHOLD = 250;

    //当前触摸坐标
    private float x;
    private float y;

    //action_down坐标
    private float startingX;
    private float startingY;

    //action_move前坐标
    private float previousX;
    private float previousY;

    //action_move移动距离
    private float lastdx;
    private float lastdy;

    //移动方向
    private int previousDirection = 1;
    private int veryLastDirection = 1;

    private boolean hasMoved = false;


    public TouchListener(MainView mainView) {
        super();
        this.mMainView = mainView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                startingX = x;
                startingY = y;
                previousX = x;
                previousY = y;
                lastdx = 0;
                lastdy = 0;
                hasMoved = false;
                Log.d(TAG, "action down:" + x + "," + y);
                return true;

            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                //判断游戏是否进行
                if (mMainView.mGame.isActive) {
                    float dx = x - previousX;
                    float dy = y - previousY;

                    resetStarting(dx, dy);

                    if (pathMoved() > Math.pow(SWIPE_MIN_DISTANCE, 2) && !hasMoved) {
                        boolean moved = false;
                        //水平反向滑动
                        if (((dx >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) > Math.abs(dy))
                                || x - startingX >= MOVE_THRESHOLD)
                                && previousDirection % 2 != 0) {
                            //向右滑动
                            moved = true;
                            previousDirection = previousDirection * 2;
                            veryLastDirection = 2;
                            Log.d(TAG, "right");

                        } else if (((dx <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) > Math.abs(dy))
                                || x - startingX <= -MOVE_THRESHOLD)
                                && previousDirection % 3 != 0) {
                            //向左滑动
                            moved = true;
                            previousDirection = previousDirection * 3;
                            veryLastDirection = 3;
                            Log.d(TAG, "left");
                        }

                        //垂直反向滑动
                        if (((dy >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) > Math.abs(dx))
                                || y - startingY >= MOVE_THRESHOLD)
                                && previousDirection % 5 != 0) {
                            //向右滑动
                            moved = true;
                            previousDirection = previousDirection * 5;
                            veryLastDirection = 5;
                            Log.d(TAG, "down");

                        } else if (((dy <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) > Math.abs(dx))
                                || y - startingY <= -MOVE_THRESHOLD)
                                && previousDirection % 7 != 0) {
                            //向左滑动
                            moved = true;
                            previousDirection = previousDirection * 7;
                            veryLastDirection = 7;
                            Log.d(TAG, "up");
                        }
                        if(moved){
                            hasMoved = true;
                            startingX = x;
                            startingY = y;
                        }
                    }
                }
                Log.d(TAG, "action move:" + x + "," + y);
                previousX = x;
                previousY = y;
                return true;

            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                //反向初始化
                previousDirection = 1;
                veryLastDirection = 1;
                //menu点击事件
                if (!hasMoved) {
                    if (iconPressed(mMainView.newGameStartX, mMainView.iconStartYAll)) {
                        //新游戏按钮
                        Log.d(TAG, "new game");
                    } else if (iconPressed(mMainView.undoStartX, mMainView.iconStartYAll)) {
                        //返回上一步
                        Log.d(TAG, "return");
                    } else if (isTap(2)
                            && isRange(mMainView.startingX, x, mMainView.endingX)
                            && isRange(mMainView.startingY, y, mMainView.endingY)) {
                        //有些结束
                        Log.d(TAG, "end game");
                    }
                }


                Log.d(TAG, "action up:" + x + "," + y);
                break;
        }
        return true;
    }

    private void resetStarting(float dx, float dy){


        lastdy = dy;
        lastdx = dx;
    }

    /**
     * 判断是否在按钮点击区域
     *
     * @param sx
     * @param sy
     * @return
     */
    private boolean iconPressed(int sx, int sy) {
        return isTap(1) && isRange(sx, x, sx + mMainView.iconSize) && isRange(sy, y, sy + mMainView.iconSize);
    }

    /**
     * 判读是否在可接收移动范围
     *
     * @param factor
     * @return
     */
    private boolean isTap(int factor) {
        boolean isTap = pathMoved() <= mMainView.iconSize * factor;
        return isTap;
    }

    /**
     * 移动距离
     *
     * @return
     */
    private double pathMoved() {
        double result = Math.pow((x - startingX), 2) + Math.pow((y - startingY), 2);
        return result;
    }

    /**
     * 判断是否在点击区域
     *
     * @param starting
     * @param check
     * @param ending
     * @return
     */
    private boolean isRange(float starting, float check, float ending) {
        boolean isRange = (starting <= check) && (check <= ending);
        return isRange;
    }

}
