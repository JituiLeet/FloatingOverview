package com.example.floatingoverview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_OVERLAY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGrantOverlay = findViewById(R.id.btn_grant_overlay);
        Button btnGrantAccessibility = findViewById(R.id.btn_grant_accessibility);
        Button btnStart = findViewById(R.id.btn_start_service);
        Button btnStop = findViewById(R.id.btn_stop_service);

        btnGrantOverlay.setOnClickListener(v -> requestOverlayPermission());
        btnGrantAccessibility.setOnClickListener(v -> openAccessibilitySettings());

        btnStart.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, R.string.toast_need_overlay, Toast.LENGTH_SHORT).show();
                requestOverlayPermission();
                return;
            }
            if (!OverlayAccessibilityService.isRunning()) {
                Toast.makeText(this, R.string.toast_need_accessibility, Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, FloatingService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        });

        btnStop.setOnClickListener(v -> stopService(new Intent(this, FloatingService.class)));
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY);
        } else {
            Toast.makeText(this, "悬浮窗权限已授予", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAccessibilitySettings() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
