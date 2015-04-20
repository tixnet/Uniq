package com.uniqapp.android.uniq;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MiniAppService extends Service {

    // Layout containers for various widgets
    private WindowManager windowManager;
    private WindowManager.LayoutParams rootLayoutParams;
    private RelativeLayout rootLayout;

    // Widgets
    private ImageButton infoButton;

    // Variables for controlling drag
    private int startDragX;
    private int prevDragX;
    private int prevDragY;


    @Override
    public IBinder onBind(Intent intent) {
        /*Not used*/
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

//        infoButton = new ImageView(this);
//        zoneButton.setImageResource(R.mipmap.ic_heart);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
//        params.x = 0;
//        params.y = 100;
//
//        windowManager.addView(zoneButton, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
