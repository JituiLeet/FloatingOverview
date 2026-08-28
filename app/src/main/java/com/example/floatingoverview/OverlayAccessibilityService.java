package com.example.floatingoverview;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

/**
 * 普通 App 无法直接调起系统"概览/最近任务"界面,
 * 系统只允许通过无障碍服务的 performGlobalAction(GLOBAL_ACTION_RECENTS) 来实现。
 * 用户需要在"设置 -> 无障碍"中手动开启这个服务一次。
 */
public class OverlayAccessibilityService extends AccessibilityService {

    private static volatile OverlayAccessibilityService instance;

    public static boolean isRunning() {
        return instance != null;
    }

    /** 请求打开概览界面,返回是否成功发起 */
    public static boolean requestOpenRecents() {
        OverlayAccessibilityService service = instance;
        if (service == null) {
            return false;
        }
        return service.performGlobalAction(GLOBAL_ACTION_RECENTS);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 不需要监听具体事件
    }

    @Override
    public void onInterrupt() {
        // 无需处理
    }

    @Override
    public boolean onUnbind(android.content.Intent intent) {
        instance = null;
        return super.onUnbind(intent);
    }
}
