package com.qibla.prayertimes.widget

import android.content.Context
import android.os.Build
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
import com.qibla.prayertimes.util.LocalePrefs
import com.qibla.prayertimes.data.prayerLabels

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val goldText = ColorProvider(Color(0xFF8A6A2E))
private val cellFillColor = ColorProvider(Color(0xFFFBF6EA))
private val cellBorderColor = ColorProvider(Color(0xFFD9C8A0))

private val cellWidth = 70.dp
private val cellHeight = 50.dp

class LightQiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: androidx.glance.GlanceId) {

        // ⭐ مهم‌ترین بخش: ویجت سبک هم باید Locale را wrap کند
        val localizedContext = LocalePrefs.wrap(context)

        val snapshot = WidgetDataStore(context).load()

        provideContent {
            LightWidgetContent(localizedContext, snapshot)
        }
    }
}

@Composable
private fun LightWidgetContent(langContext: Context, snapshot: WidgetSnapshot?) {

    // ⭐ تشخیص زبان فعلی
    val config = langContext.resources.configuration
    val language =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            config.locales[0].language
        else
            config.locale.language

    // ⭐ گرفتن لیبل‌ها بر اساس زبان (مثل ویجت اصلی)
    val labels = prayerLabels(langContext)

    val jalaliText = snapshot?.jalaliText ?: langContext.getString(R.string.widget_updating)

    val fajr = snapshot?.timings?.get("Fajr") ?: "--:--"
    val sunrise = snapshot?.timings?.get("Sunrise") ?: "--:--"
    val dhuhr = snapshot?.timings?.get("Dhuhr") ?: "--:--"
    val sunset = snapshot?.timings?.get("Sunset") ?: "--:--"
    val maghrib = snapshot?.timings?.get("Maghrib") ?: "--:--"
    val midnight = snapshot?.timings?.get("Midnight") ?: "--:--"

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bgColor)
            .cornerRadius(20.dp)
            .padding(12.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {

        // ⭐ تاریخ شمسی
        Text(
            text = jalaliText,
            style = TextStyle(
                color = goldText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = GlanceModifier.height(10.dp))

        // ⭐ ردیف اول
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            LightCell(labels["Fajr"] ?: "Fajr", fajr)
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Sunrise"] ?: "Sunrise", sunrise)
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Dhuhr"] ?: "Dhuhr", dhuhr)
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        // ⭐ ردیف دوم
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            LightCell(labels["Sunset"] ?: "Sunset", sunset)
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Maghrib"] ?: "Maghrib", maghrib)
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell(labels["Midnight"] ?: "Midnight", midnight)
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
