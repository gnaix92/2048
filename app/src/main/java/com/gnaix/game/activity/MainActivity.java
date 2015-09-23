package com.gnaix.game.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.gnaix.game.widget.MainView;

/**
 * 名称: MainActivity
 * 描述: 游戏入口
 *
 * @author xiangqing.xue
 * @date 15/9/22
 */
public class MainActivity extends Activity {

    //主页面
    private MainView mainView;
    //状态
    private SharedPreferences pref;
    //
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mainView = new MainView(this.getApplicationContext());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mainView.hasSaveState = pref.getBoolean("save_state", false);

        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("has_state")){
                loading();
            }
        }
        setContentView(mainView);
        mActivity = this;
    }


    private void loading(){

    }
}
