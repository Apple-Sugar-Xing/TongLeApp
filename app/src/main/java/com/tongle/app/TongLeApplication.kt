package com.tongle.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TongLeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 应用初始化代码
    }
}
