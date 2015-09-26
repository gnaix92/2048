package com.gnaix.game.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.gnaix.game.widget.MainView;

/**
 * 名称: Game
 * 描述:
 *
 * @author xiangqing.xue
 * @date 15/9/22
 */
public class Game {
    private Context mContext;
    private MainView mView;

    //游戏结束最大值
    public static int endingMaxValue;

    //单元格数
    public static final int numSquaresX = 4;
    public static final int numSquaresY = 4;

    //分数
    public static int highScore;
    public static int score;

    //是否进行中
    public boolean isActive = true;

    private Grid mGrid;




    public Game(Context context, MainView view){
        this.mContext = context;
        this.mView = view;
        endingMaxValue = (int) Math.pow(2, view.numCellTypes-1);

    }

    /**
     * 创建新游戏
     */
    public void newGame(){
        if(mGrid == null){
            mGrid = new Grid(numSquaresX, numSquaresY);
        }else{
            
        }
    }
}
