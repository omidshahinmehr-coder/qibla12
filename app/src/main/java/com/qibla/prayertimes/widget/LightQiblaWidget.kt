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

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val goldText = ColorProvider(Color(0xFF8A6A2E))
private val cellFillColor = ColorProvider(Color(0xFFFBF6EA))
private val cellBorderColor = ColorProvider(Color(0xFFD9C8A0))

private val cellWidth = 70.dp
private val cellHeight = 50.dp

class LightQiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: androidx.glance.GlanceId) {
        val snapshot = WidgetDataStore(context).load()
        provideContent {
            LightWidgetContent(snapshot)
        }
    }
}

@Composable
private fun LightWidgetContent(snapshot: WidgetSnapshot?) {

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bgColor)
            .cornerRadius(20.dp)
            .padding(12.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {

        // ⭐ اگر snapshot=null باشد، یک UI کامل و بدون کرش نشان بده
        if (snapshot == null) {
            Text(
                text = "در حال بروزرسانی...",
                style = TextStyle(
                    color = goldText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                LightCell("فجر", "--:--")
                Spacer(modifier = GlanceModifier.width(4.dp))
                LightCell("طلوع", "--:--")
                Spacer(modifier = GlanceModifier.width(4.dp))
                LightCell("ظهر", "--:--")
            }

            Spacer(modifier = GlanceModifier.height(6.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                LightCell("غروب", "--:--")
                Spacer(modifier = GlanceModifier.width(4.dp))
                LightCell("مغرب", "--:--")
                Spacer(modifier = GlanceModifier.width(4.dp))
                LightCell("نیمه‌شب", "--:--")
            }

            return
        }

        // ⭐ تاریخ شمسی از snapshot
        Text(
            text = snapshot.jalaliText,
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
            LightCell("فجر", snapshot.timings["Fajr"] ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell("طلوع", snapshot.timings["Sunrise"] ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell("ظهر", snapshot.timings["Dhuhr"] ?: "--:--")
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        // ⭐ ردیف دوم
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            LightCell("غروب", snapshot.timings["Sunset"] ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell("مغرب", snapshot.timings["Maghrib"] ?: "--:--")
            Spacer(modifier = GlanceModifier.width(4.dp))
            LightCell("نیمه‌شب", snapshot.timings["Midnight"] ?: "--:--")
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
