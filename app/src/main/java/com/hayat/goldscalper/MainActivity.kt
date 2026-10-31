package com.hayat.goldscalper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Black = Color(0xFF080808)
private val Panel = Color(0xFF171717)
private val Gold = Color(0xFFFFC629)
private val Muted = Color(0xFFB8B8B8)
private val Green = Color(0xFF20D66B)
private val Red = Color(0xFFFF5252)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HayatApp() }
    }
}

@Composable
fun HayatApp() {
    var message by remember { mutableStateOf("نسخه اولیه؛ هنوز به VPS و MT5 متصل نیست") }
    var tab by remember { mutableStateOf("داشبورد") }
    var risk by remember { mutableStateOf("0.25") }
    var dailyLimit by remember { mutableStateOf("1.50") }
    var spread by remember { mutableStateOf("80") }
    var rr by remember { mutableStateOf("1.20") }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Black, surface = Panel, primary = Gold,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Scaffold(
            containerColor = Black,
            snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(Modifier.fillMaxWidth().padding(top = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("♛  HAYAT", color = Gold, fontSize = 34.sp, fontWeight = FontWeight.Black)
                        Text("GOLD SCALPER", color = Gold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("ربات هوشمند معاملات طلا", color = Muted, fontSize = 13.sp)
                        Spacer(Modifier.height(12.dp))
                        Surface(color = Color(0xFF321B12), shape = RoundedCornerShape(30.dp)) {
                            Text("●  اتصال MT5: برقرار نشده     ●  VPS: نامشخص",
                                color = Color(0xFFFFB74D), modifier = Modifier.padding(12.dp), fontSize = 12.sp)
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetricCard("موجودی حساب", "—", Modifier.weight(1f))
                        MetricCard("سود و زیان امروز", "—", Modifier.weight(1f))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetricCard("اکوییتی حساب", "—", Modifier.weight(1f))
                        MetricCard("ضرر روزانه", "—", Modifier.weight(1f))
                    }
                }
                item {
                    CardBox {
                        Text("وضعیت ربات", color = Gold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("نماد معاملاتی", color = Muted)
                            Text("XAUUSD", fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("معاملات باز", color = Muted)
                            Text("—")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("وضعیت اجرا", color = Muted)
                            Text("غیرفعال / بدون اتصال", color = Red)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("نمودار و اطلاعات زنده پس از اتصال امن به سرویس VPS نمایش داده می‌شود.",
                            color = Muted, fontSize = 12.sp)
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { message = "برای کنترل واقعی، ابتدا باید سرویس امن VPS نصب و متصل شود." },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Black)
                        ) { Text("▶ شروع ربات", fontWeight = FontWeight.Bold) }
                        OutlinedButton(
                            onClick = { message = "فرمان توقف تا زمان اتصال VPS قابل ارسال نیست." },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold)
                        ) { Text("Ⅱ توقف") }
                    }
                    Button(
                        onClick = { message = "توقف اضطراری واقعی فقط پس از اتصال سرویس VPS فعال می‌شود." },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D1717))
                    ) { Text("■ توقف اضطراری", color = Color.White, fontWeight = FontWeight.Bold) }
                    Text(message, color = Muted, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
                item {
                    CardBox {
                        Text("تنظیمات مدیریت ریسک", color = Gold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        SettingLine("ریسک هر معامله (%)", risk) { risk = it }
                        SettingLine("حد ضرر روزانه (%)", dailyLimit) { dailyLimit = it }
                        SettingLine("حداکثر اسپرد (پوینت)", spread) { spread = it }
                        SettingLine("نسبت سود به ضرر", rr) { rr = it }
                        Text("این مقادیر فعلاً فقط در رابط کاربری هستند و به EA ارسال نمی‌شوند.",
                            color = Color(0xFFFFB74D), fontSize = 11.sp)
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf("داشبورد", "معاملات", "تاریخچه", "تنظیمات").forEach {
                            TextButton(onClick = { tab = it }) {
                                Text(it, color = if (tab == it) Gold else Muted, fontSize = 12.sp)
                            }
                        }
                    }
                    Text("بخش انتخاب‌شده: $tab", color = Muted, fontSize = 11.sp,
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp).fillMaxWidth()) {
            Text(title, color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CardBox(content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
fun SettingLine(label: String, value: String, onValue: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(label, color = Muted, modifier = Modifier.weight(1f), fontSize = 13.sp)
        OutlinedTextField(value = value, onValueChange = onValue, singleLine = true,
            modifier = Modifier.width(100.dp), textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold, unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White, unfocusedTextColor = Color.White
            ))
    }
}
