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
import com.qibla.prayertimes.R
import com.qibla.prayertimes.data.WidgetDataStore
import com.qibla.prayertimes.data.WidgetSnapshot
import com.qibla.prayertimes.data.prayerLabels
import com.qibla.prayertimes.util.LocalePrefs
import java.text.SimpleDateFormat
import java.util.*

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val goldText = ColorProvider(Color(0xFF8A6A2E))
private val faintGoldText = ColorProvider(Color(0xFFAD8F55))
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

        if (snapshot != null) {

            val timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val dateText = snapshot.jalaliText

            Text(
                text = timeText,
                style = TextStyle(
                    color = goldText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = GlanceModifier.height(4.dp))

            Text(
                text = dateText,
                style = TextStyle(
                    color = goldText,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = GlanceModifier.height(10.dp))

            val keysRow1 = listOf("Fajr", "Sunrise", "Dhuhr")
            val keysRow2 = listOf("Sunset", "Maghrib", "Midnight")

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                keysRow1.forEachIndexed { index, key ->
                    if (index > 0) Spacer(modifier = GlanceModifier.width(4.dp))
                    LightCell(labels[key] ?: key, snapshot.timings[key] ?: "--:--")
                }
            }

            Spacer(modifier = GlanceModifier.height(6.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                keysRow2.forEachIndexed { index, key ->
                    if (index > 0) Spacer(modifier = GlanceModifier.width(4.dp))
                    LightCell(labels[key] ?: key, snapshot.timings[key] ?: "--:--")
                }
            }

        } else {
            Text(
                text = context.getString(R.string.widget_updating),
                style = TextStyle(
                    color = goldText,
                    fontSize = 15.sp
                )
            )
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
