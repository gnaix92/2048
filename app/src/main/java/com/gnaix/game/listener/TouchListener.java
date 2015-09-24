package com.gnaix.game.listener;

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
public class TouchListener implements View.OnTouchListener{
    private MainView mMainView;

    public TouchListener(MainView mainView){
        super();
        this.mMainView = mainView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
