package com.qibla.prayertimes.widget

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Image
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
import java.text.SimpleDateFormat
import java.util.*

private val bgColor = ColorProvider(Color(0xFFF3ECDD))
private val cellBorderColor = ColorProvider(Color(0xFFD9C8A0))
private val cellFillColor = ColorProvider(Color(0xFFFBF6EA))

// رنگ‌های جدید
private val goldText = ColorProvider(Color(0xFF6F5525))      // طلایی تیره‌تر
private val faintGoldText = ColorProvider(Color(0xFF222222)) // مشکی ملایم

private val widgetPrayerKeys = listOf("Fajr", "Sunrise", "Dhuhr", "Sunset", "Maghrib", "Midnight")
private val cellWidth = 70.dp

private val WEEKDAYS_FA = arrayOf("یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه")
private val WEEKDAYS_AR = arrayOf("الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")

class QiblaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val localizedContext = LocalePrefs.wrap(context)
        val snapshot = WidgetDataStore(context).load()
        provideContent {
            WidgetContent(localizedContext, snapshot)
        }
    }
}

@Composable
private fun WidgetContent(langContext: Context, snapshot: WidgetSnapshot?) {
    val labels = prayerLabels(langContext)
    val language = langContext.resources.configuration.locales[0].language

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(bgColor)
            .cornerRadius(20.dp)
            .padding(12.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        if (snapshot != null) {

            val countdown = nextPrayerCountdown(snapshot.timings)
            val weekdayName = weekdayName(language)
            val gregorianText = formatGregorian(langContext, snapshot.gregorianDateKey)
            val jalaliWithWeekday = listOf(weekdayName, snapshot.jalaliText).filter { it.isNotBlank() }.joinToString(" ")

            val clockBlock: @Composable () -> Unit = {
                AndroidRemoteViews(RemoteViews(langContext.packageName, R.layout.widget_clock))
            }

            val countdownLabelBlock: @Composable () -> Unit = {
                if (countdown != null) {
                    Text(
                        text = langContext.getString(
                            R.string.widget_countdown_label,
                            labels[countdown.first] ?: countdown.first
                        ),
                        style = TextStyle(
                            color = goldText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 1
                    )
                }
            }

            val jalaliBlock: @Composable () -> Unit = {
                Text(
                    text = jalaliWithWeekday,
                    style = TextStyle(
                        color = goldText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1
                )
            }

            val timerBlock: @Composable () -> Unit = {
                if (countdown != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val nowElapsed = SystemClock.elapsedRealtime()
                        val nowWall = System.currentTimeMillis()
                        val base = nowElapsed + (countdown.second - nowWall)
                        val rv = RemoteViews(langContext.packageName, R.layout.widget_countdown)
                        rv.setChronometer(R.id.widget_countdown_view, base, null, true)
                        AndroidRemoteViews(rv)
                    } else {
                        Text(
                            text = staticDuration(countdown.second),
                            style = TextStyle(
                                color = goldText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    clockBlock()
                }

                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        Text(
                            text = snapshot.cityName,
                            style = TextStyle(
                                color = goldText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        Image(
                            provider = androidx.glance.ImageProvider(R.drawable.ic_location_pin),
                            contentDescription = null,
                            modifier = GlanceModifier.width(12.dp).height(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = GlanceModifier.height(2.dp))

            Box(
                modifier = GlanceModifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                jalaliBlock()
            }

            Spacer(modifier = GlanceModifier.height(2.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {

                Text(
                    text = snapshot.hijriText,
                    style = TextStyle(
                        color = faintGoldText,
                        fontSize = 15.sp
                    ),
                    modifier = GlanceModifier.padding(4.dp)
                )

                Text(
                    text = "-",
                    style = TextStyle(
                        color = faintGoldText,
                        fontSize = 15.sp
                    ),
                    modifier = GlanceModifier.padding(horizontal = 4.dp)
                )

                Text(
                    text = gregorianText,
                    style = TextStyle(
                        color = faintGoldText,
                        fontSize = 15.sp
                    ),
                    modifier = GlanceModifier.padding(4.dp)
                )
            }

            Spacer(modifier = GlanceModifier.height(2.dp))

            Box(
                modifier = GlanceModifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    timerBlock()
                    Spacer(modifier = GlanceModifier.width(6.dp))
                    countdownLabelBlock()
                }
            }

            Spacer(modifier = GlanceModifier.height(2.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                widgetPrayerKeys.forEachIndexed { index, key ->
                    if (index > 0) Spacer(modifier = GlanceModifier.width(4.dp))
                    PrayerCell(label = labels[key] ?: key, time = snapshot.timings[key] ?: "--:--")
                }
            }

        } else {
            Text(
                text = langContext.getString(R.string.widget_updating),
                style = TextStyle(
                    color = goldText,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = GlanceModifier.height(6.dp))
            Text(
                text = langContext.getString(R.string.widget_open_app_hint),
                style = TextStyle(
                    color = faintGoldText,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun PrayerCell(label: String, time: String) {
    Column(
        modifier = GlanceModifier
            .width(cellWidth)
            .background(cellBorderColor)
            .cornerRadius(16.dp)
            .padding(1.2.dp)
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(cellFillColor)
                .cornerRadius(15.dp)
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = goldText,   // عنوان‌ها طلایی تیره‌تر
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = time,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF222222)),   // زمان‌ها مشکی ملایم
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
