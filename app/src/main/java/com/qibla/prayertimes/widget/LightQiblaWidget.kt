package com.qibla.prayertimes.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.qibla.prayertimes.MainActivity
import com.qibla.prayertimes.R
import com.qibla.prayertimes.data.WidgetDataStore
import com.qibla.prayertimes.data.WidgetSnapshot
import com.qibla.prayertimes.data.prayerLabels
import com.qibla.prayertimes.util.LocalePrefs

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val goldText = ColorProvider(Color(0xFF8A6A2E))
private val cellFillColor = ColorProvider(Color(0xFFFBF6EA))
private val cellBorderColor = ColorProvider(Color(0xFFD9C8A0))

private val cellWidth = 90.dp
private val cellHeight = 50.dp

class LightQiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val localizedContext = LocalePrefs.wrap(context)
        val snapshot = WidgetDataStore(context).load()

        provideContent {
            LightWidgetContent(localizedContext, snapshot)
        }
    }
}

@Composable
private fun LightWidgetContent(langContext: Context, snapshot: WidgetSnapshot?) {

    val labels = prayerLabels(langContext)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bgColor)
            .cornerRadius(20.dp)
            .padding(12.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {

        if (snapshot == null) {
            Text(
                text = labels["Updating"] ?: "Updating...",
                style = TextStyle(
                    color = goldText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            return
        }

        // ⭐ ساعت (RemoteViews)
        Box(
            modifier = GlanceModifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AndroidRemoteViews(
                RemoteViews(langContext.packageName, R.layout.widget_clock)
