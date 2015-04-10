package com.uniqapp.android.uniq;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class ZoneCheckerService extends Service {

    private WindowManager windowManager;
    private ImageView zoneButton;

    @Override
    public IBinder onBind(Intent intent) {
        /*Not userd*/
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        zoneButton = new ImageView(this);
        zoneButton.setImageResource(R.mipmap.ic_test);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(zoneButton, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (zoneButton != null) windowManager.removeView(zoneButton);
    }
}
