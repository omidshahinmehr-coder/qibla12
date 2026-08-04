package com.qibla.prayertimes.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
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
import com.qibla.prayertimes.R
import com.qibla.prayertimes.data.WidgetDataStore
import com.qibla.prayertimes.data.prayerLabels
import com.qibla.prayertimes.data.LocalePrefs
import java.time.LocalTime

class LightQiblaWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context) {
        provideContent {
            val snapshot = WidgetDataStore(context).load()
            val locale = LocalePrefs.wrap(context)

            if (snapshot == null) {
                LightWidgetLoading()
            } else {
                LightWidgetContent(snapshot, locale)
            }
        }
    }
}

@Composable
fun LightWidgetLoading() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "در حال بارگذاری…",
            style = TextStyle(
                color = ColorProvider(android.graphics.Color.WHITE),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun LightWidgetContent(snapshot: WidgetDataStore.Snapshot, locale: LocalePrefs) {

    val goldText = ColorProvider(android.graphics.Color.parseColor("#FFD700"))
    val whiteText = ColorProvider(android.graphics.Color.WHITE)

    val cellWidth = 100.dp   // ⭐ عرض سلول‌ها بزرگ‌تر شد
    val cellHeight = 32.dp

    val now = LocalTime.now().toString().substring(0, 5)   // ⭐ ساعت فعلی

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ⭐ ساعت در بالاترین بخش
        Text(
            text = now,
            style = TextStyle(
                color = goldText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = GlanceModifier.height(6.dp))

        // ⭐ تاریخ شمسی زیر ساعت
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
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            prayerLabels(locale).forEach { (key, label) ->
                val time = snapshot.timings[key] ?: "—"

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
    }
}

class LightQiblaWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = LightQiblaWidget()
}
