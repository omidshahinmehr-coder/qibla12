package com.qibla.prayertimes.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LightQiblaWidgetUpdater {

    /** Fire-and-forget: asks every placed instance of the light widget to redraw from cached data. */
    fun requestUpdate(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                LightQiblaWidget().updateAll(context)
            } catch (_: Exception) {
                // Safe to ignore — no widget placed or transient Glance error
            }
        }
    }
}
