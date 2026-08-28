package com.example.floatingoverview;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class FloatingService extends Service {

    private static final String CHANNEL_ID = "floating_overview_channel";
    private static final int NOTIFICATION_ID = 1;

    private WindowManager windowManager;
    private View overlayView;
    private ImageButton btnSquare;
    private ImageButton btnToggle;

    private boolean collapsed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundWithNotification();
        addOverlayView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeOverlayView();
    }

    private void startForegroundWithNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_MIN);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    private void addOverlayView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_buttons, null);
        btnSquare = overlayView.findViewById(R.id.btn_square);
        btnToggle = overlayView.findViewById(R.id.btn_toggle);

        int overlayType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                overlayType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        // 左下角
        params.gravity = Gravity.BOTTOM | Gravity.START;
        params.x = 24;
        params.y = 48;

        windowManager.addView(overlayView, params);

        btnSquare.setOnClickListener(v -> onSquareButtonClicked());
        btnToggle.setOnClickListener(v -> onToggleButtonClicked());
    }

    private void removeOverlayView() {
        if (windowManager != null && overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }

    /** 点击空心正方形按钮:打开系统概览界面 */
    private void onSquareButtonClicked() {
        boolean success = OverlayAccessibilityService.requestOpenRecents();
        if (!success) {
            Toast.makeText(this, R.string.toast_need_accessibility, Toast.LENGTH_LONG).show();
        }
    }

    /** 点击三角形按钮:折叠/展开正方形按钮 */
    private void onToggleButtonClicked() {
        collapsed = !collapsed;
        if (collapsed) {
            btnSquare.setVisibility(View.GONE);
            btnToggle.setImageResource(R.drawable.ic_triangle_right);
        } else {
            btnSquare.setVisibility(View.VISIBLE);
            btnToggle.setImageResource(R.drawable.ic_triangle_left);
        }
    }
}
