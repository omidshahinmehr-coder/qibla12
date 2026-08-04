package com.qibla.prayertimes.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.qibla.prayertimes.data.WidgetDataStore
import com.qibla.prayertimes.data.WidgetSnapshot
import com.qibla.prayertimes.data.prayerLabels
import com.qibla.prayertimes.util.LocalePrefs
import java.text.SimpleDateFormat
import java.util.*

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val goldText = ColorProvider(Color(0xFF8A6A2E))
private val cellFillColor = ColorProvider(Color(0xFFFBF6EA))
private val cellBorderColor = ColorProvider(Color(0xFFD9C8A0))

private val cellWidth = 70.dp
private val cellHeight = 50.dp

class LightQiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: androidx.glance.GlanceId) {
        val localizedContext = LocalePrefs.wrap(context)
        val snapshot = WidgetDataStore(context).load()
        provideContent {
            LightWidgetContent(localizedContext, snapshot)
        }
    }
}

@Composable
private fun LightWidgetContent(context: Context, snapshot: WidgetSnapshot?) {

    val labels = prayerLabels(context)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bgColor)
            .cornerRadius(20.dp)
            .padding(12.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {

        val timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val dateText = snapshot?.jalaliText ?: "—"

        // ⭐ ساعت
        Text(
            text = timeText,
            style = TextStyle(
                color = goldText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = GlanceModifier.height(4.dp))

        // ⭐ تاریخ شمسی
        Text(
            text = dateText,
            style = TextStyle(
                color = goldText,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = GlanceModifier.height(10.dp))

        // ⭐ ردیف اول
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            LightCell(labels["Fajr"] ?: "فجر", snapshot?.timings?.get("Fajr") ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Sunrise"] ?: "طلوع", snapshot?.timings?.get("Sunrise") ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Dhuhr"] ?: "ظهر", snapshot?.timings?.get("Dhuhr") ?: "--:--")
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        // ⭐ ردیف دوم
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            LightCell(labels["Sunset"] ?: "غروب", snapshot?.timings?.get("Sunset") ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Maghrib"] ?: "مغرب", snapshot?.timings?.get("Maghrib") ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Midnight"] ?: "نیمه‌شب", snapshot?.timings?.get("Midnight") ?: "--:--")
        }
    }
}

@Composable
private fun LightCell(label: String, time: String) {
    Column(
        modifier = GlanceModifier
            .width(cellWidth)
            .background(cellBorderColor)
            .cornerRadius(10.dp)
            .padding(1.dp)
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(cellHeight)
                .background(cellFillColor)
                .cornerRadius(8.dp)
                .padding(4.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = goldText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = time,
                style = TextStyle(
                    color = goldText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
