package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;

/**
 * Created by hitma on 2017/10/15.
 */

public abstract class BaseSetupActivity extends AppCompatActivity{
    public SharedPreferences sp;
    private GestureDetector mGestureDetector;
    public abstract  void showNext();
    public  abstract  void  showPre();
}
