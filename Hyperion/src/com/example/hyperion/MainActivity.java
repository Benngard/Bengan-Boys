package com.example.hyperion;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Main Activity for Project Hyperion, initializes MainGamePanel
 * 
 * @author 		Mattias Benngård
 * @version		1.0
 * @since		2015-10-05
 */
public class MainActivity extends Activity
{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MainGamePanel(this));
    }
}