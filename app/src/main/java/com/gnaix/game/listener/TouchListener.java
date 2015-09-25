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
public class TouchListener implements View.OnTouchListener{
    private final static String TAG = "TouchListener";
    private MainView mMainView;

    public TouchListener(MainView mainView){
        super();
        this.mMainView = mainView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "action down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "action move");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up");
                break;
        }
        return true;
    }
}
