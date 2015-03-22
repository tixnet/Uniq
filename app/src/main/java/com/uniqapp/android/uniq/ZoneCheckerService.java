package com.uniqapp.android.uniq;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Button;

public class ZoneCheckerService extends Service {

    private WindowManager windowManager;
    private Button zoneButton;

    @Override
    public IBinder onBind(Intent intent) {
        /*Not userd*/
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

    }
}
