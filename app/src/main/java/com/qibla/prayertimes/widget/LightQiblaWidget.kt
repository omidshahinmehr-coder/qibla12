package com.qibla.prayertimes.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.FontWeight
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.dp
import androidx.glance.unit.sp
import com.qibla.prayertimes.data.WidgetDataStore
import com.qibla.prayertimes.data.WidgetSnapshot
import com.qibla.prayertimes.data.prayerLabels
import com.qibla.prayertimes.util.LocalePrefs
import java.time.LocalTime

// ⭐ رنگ‌ها
private val goldText = ColorProvider(android.graphics.Color.parseColor("#8A6A2E"))
private val whiteText = ColorProvider(android.graphics.Color.WHITE)

// ⭐ مقدار ثابت سلول‌ها (مثل ویجت اصلی)
private val cellWidth = 100.dp
private val cellHeight = 32.dp

class LightQiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context) {
        val localizedContext = LocalePrefs.wrap(context)
        val snapshot = WidgetDataStore(context).load()
        provideContent {
            LightWidgetContent(localizedContext, snapshot)
        }
    }
}

@Composable
private fun LightWidgetContent(langContext: Context, snapshot: WidgetSnapshot?) {

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {

        if (snapshot != null) {

            // ⭐ ساعت بالا
            val now = LocalTime.now().toString().substring(0, 5)
            Text(
                text = now,
                style = TextStyle(
                    color = goldText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = GlanceModifier.height(6.dp))

            // ⭐ تاریخ شمسی
            Text(
                text = snapshot.jalaliText,
                style = TextStyle(
                    color = goldText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = GlanceModifier.height(12.dp))

            // ⭐ جدول اوقات شرعی
            val labels = prayerLabels(langContext)

            Column(
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                labels.forEach { (key, label) ->
                    val time = snapshot.timings[key] ?: "--:--"

                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // عنوان
                        Box(
                            modifier = GlanceModifier
                                .width(cellWidth)
                                .height(cellHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = TextStyle(
                                    color = whiteText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = GlanceModifier.width(8.dp))

                        // زمان
                        Box(
                            modifier = GlanceModifier
                                .width(cellWidth)
                                .height(cellHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = time,
                                style = TextStyle(
                                    color = goldText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

        } else {
            Text(
                text = "در حال بارگذاری…",
                style = TextStyle(
                    color = goldText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

class LightQiblaWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = LightQiblaWidget()
}
