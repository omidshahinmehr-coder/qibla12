package com.qibla.prayertimes.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        QiblaWidget().updateAll(context)
    }
}
