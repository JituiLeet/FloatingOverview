# 悬浮概览按钮

一个面向 Android 的悬浮概览按钮工具。

打开应用提供悬浮窗口与无障碍服务之后，开启按钮之后。在左下角显示一个黑灰色按钮。点击会借助 Android 无障碍服务执行系统的 `GLOBAL_ACTION_RECENTS`，打开系统概览界面。

## 功能

- 悬浮按钮显示在屏幕左下角
- 黑灰色按钮样式
- 点击按钮打开系统概览界面
- 基于 Android 无障碍服务实现
- 需要悬浮窗口权限与无障碍服务权限

## 使用方法

1. 安装并打开应用
2. 授予悬浮窗口权限
3. 开启无障碍服务
4. 开启按钮
5. 点击左下角的黑灰色悬浮按钮，即可打开系统概览界面

## 许可证

本项目基于 MIT License 开源，详见 [LICENSE](LICENSE)。


## Android 14 compatibility

This build targets Android 14 (API 34) and compiles against API 34. The minimum supported Android version remains API 24. The floating foreground service declares the Android 14 `specialUse` foreground-service type and its required subtype property. Android 13+ notification permission is requested at runtime.
