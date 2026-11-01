package com.hayat.goldscalper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

private val Black = Color(0xFF080808)
private val Panel = Color(0xFF171717)
private val Gold = Color(0xFFFFC629)
private val Muted = Color(0xFFB8B8B8)
private val Green = Color(0xFF20D66B)
private val Red = Color(0xFFFF5252)

private val moneyFormat = DecimalFormat("0.00")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HayatApp()
        }
    }
}
@Composable
fun HayatApp() {
    var isRunning by remember { mutableStateOf(false) }
    var emergencyStop by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("داشبورد") }

    var risk by remember { mutableStateOf("0.25") }
    var dailyLimit by remember { mutableStateOf("1.5") }
    var spreadLimit by remember { mutableStateOf("80") }

    var strategyStatus by remember {
    mutableStateOf("موتور تحلیل آماده است؛ منتظر داده بازار")
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Black,
            surface = Panel,
            primary = Gold
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Black
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "HAYAT",
                            color = Gold,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "GOLD SCALPER",
                            color = Muted,
                            fontSize = 14.sp
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoCard("موجودی", "—")
                        InfoCard("اکوییتی", "—")
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoCard("سود امروز", "—")
                        InfoCard("معاملات باز", "0")
                    }
                }

                item {
                    SectionTitle("وضعیت ربات")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Panel
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = status,
                                color = if (isRunning) Green else Gold,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "نماد معاملاتی: XAUUSD",
                                color = Color.White
                            )

                            Text(
                                text = "تایم‌فریم ورود: M1",
                                color = Muted
                            )

                            Text(
                                text = "تایم‌فریم روند: M5",
                                color = Muted
                            )

                            Text(
                                text = "اتصال MT5: برقرار نشده",
                                color = Red
                            )
                            Text(
    text = strategyStatus,
    color = Gold,
    fontSize = 13.sp
)
                        }
                    }
                }

                item {
                    SectionTitle("کنترل ربات")

                    Button(
                        onClick = {
                            if (!emergencyStop) {
                                isRunning = !isRunning
                                status = if (isRunning) {
                                    "ربات فعال است؛ اتصال معاملاتی هنوز برقرار نیست"
                                } else {
                                    "ربات متوقف شد"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) Red else Green
                        )
                    ) {
                        Text(
                            if (isRunning) "توقف ربات" else "شروع ربات",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                            isRunning = false
                            emergencyStop = true
                            status = "توقف اضطراری فعال شد"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        )
                    ) {
                        Text(
                            "توقف اضطراری",
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            emergencyStop = false
                            status = "توقف اضطراری غیرفعال شد"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "بازنشانی توقف اضطراری",
                            color = Gold
                        )
                    }
                }

                item {
                    SectionTitle("مدیریت ریسک")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Panel
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingField(
                                title = "ریسک هر معامله (درصد)",
                                value = risk,
                                onValueChange = { risk = it }
                            )

                            SettingField(
                                title = "حداکثر ضرر روزانه (درصد)",
                                value = dailyLimit,
                                onValueChange = { dailyLimit = it }
                            )

                            SettingField(
                                title = "حداکثر اسپرد (پوینت)",
                                value = spreadLimit,
                                onValueChange = { spreadLimit = it }
                            )

                            Text(
                                text = "حداکثر معاملات هم‌زمان: ۱",
                                color = Muted
                            )

                            Text(
                                text = "استراتژی: بدون مارتینگل",
                                color = Muted
                            )
                        }
                    }
                }

                item {
                    SectionTitle("وضعیت اتصال")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Panel
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ConnectionRow("اینترنت", "بررسی نشده")
                            ConnectionRow("قیمت زنده طلا", "متصل نیست")
                            ConnectionRow("LiteFinance MT5", "متصل نیست")
                            ConnectionRow("VPS", "استفاده نشده")
                        }
                    }
                }

                item {
                    SectionTitle("تاریخچه معاملات")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Panel
                        )
                    ) {
                        Text(
                            text = "هنوز اطلاعات معامله‌ای دریافت نشده است.",
                            color = Muted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = "نسخه آزمایشی رابط کاربری HAYAT",
                        color = Muted,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .width(155.dp)
            .height(95.dp),
        colors = CardDefaults.cardColors(
            containerColor = Panel
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Muted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = Gold,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Gold,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SettingField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            color = Muted,
            fontSize = 13.sp
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Gold,
                unfocusedBorderColor = Muted,
                cursorColor = Gold
            )
        )
    }
}

@Composable
fun ConnectionRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Text(value, color = Red)
    }
}
