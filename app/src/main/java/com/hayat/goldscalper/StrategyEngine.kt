
package com.hayat.goldscalper

import kotlin.math.abs
import kotlin.math.max

enum class TradeSignal { BUY, SELL, NONE }

data class Candle(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)

data class StrategyResult(
    val signal: TradeSignal,
    val entry: Double?,
    val stopLoss: Double?,
    val takeProfit: Double?,
    val atr: Double?,
    val rsi: Double?,
    val reason: String
)

object HayatStrategyEngine {
    fun analyze(entryCandles: List<Candle>, trendCandles: List<Candle>): StrategyResult {
        if (entryCandles.size < 220 || trendCandles.size < 220) {
            return noSignal("داده کافی نیست؛ حداقل ۲۲۰ کندل لازم است")
        }

        val entry = entryCandles.dropLast(1)
        val trend = trendCandles.dropLast(1)
        val closes = entry.map { it.close }
        val trendCloses = trend.map { it.close }

        val ema20 = ema(closes, 20)
        val ema50 = ema(trendCloses, 50)
        val ema200 = ema(trendCloses, 200)
        val rsi = rsi(closes, 14)
        val atr = atr(entry, 14)

        if (ema20 == null || ema50 == null || ema200 == null || rsi == null || atr == null) {
            return noSignal("محاسبه اندیکاتورها ناموفق بود")
        }

        val price = entry.last().close
        val trendPrice = trend.last().close
        if (atr <= 0.0 || price <= 0.0) return noSignal("قیمت یا ATR معتبر نیست")

        val buy = trendPrice > ema200 && ema50 > ema200 && price > ema20 && rsi >= 52.0
        val sell = trendPrice < ema200 && ema50 < ema200 && price < ema20 && rsi <= 48.0

        if (!buy && !sell) {
            return StrategyResult(TradeSignal.NONE, null, null, null, atr, rsi, "شرایط ورود کامل نیست")
        }

        val distance = atr * 1.5
        return if (buy) {
            StrategyResult(TradeSignal.BUY, price, price - distance, price + distance * 1.2, atr, rsi, "شرایط خرید برقرار است")
        } else {
            StrategyResult(TradeSignal.SELL, price, price + distance, price - distance * 1.2, atr, rsi, "شرایط فروش برقرار است")
        }
    }

    private fun ema(values: List<Double>, period: Int): Double? {
        if (values.size < period) return null
        val alpha = 2.0 / (period + 1.0)
        var result = values.take(period).average()
        for (value in values.drop(period)) result = value * alpha + result * (1.0 - alpha)
        return result
    }

    private fun rsi(values: List<Double>, period: Int): Double? {
        if (values.size <= period) return null
        var gains = 0.0
        var losses = 0.0
        for (i in 1..period) {
            val change = values[i] - values[i - 1]
            if (change > 0) gains += change else losses += abs(change)
        }
        var avgGain = gains / period
        var avgLoss = losses / period
        for (i in (period + 1) until values.size) {
            val change = values[i] - values[i - 1]
            avgGain = (avgGain * (period - 1) + max(change, 0.0)) / period
            avgLoss = (avgLoss * (period - 1) + max(-change, 0.0)) / period
        }
        if (avgLoss == 0.0) return 100.0
        val rs = avgGain / avgLoss
        return 100.0 - 100.0 / (1.0 + rs)
    }

    private fun atr(candles: List<Candle>, period: Int): Double? {
        if (candles.size <= period) return null
        val ranges = mutableListOf<Double>()
        for (i in 1 until candles.size) {
            val current = candles[i]
            val previous = candles[i - 1].close
            ranges.add(max(current.high - current.low, max(abs(current.high - previous), abs(current.low - previous))))
        }
        if (ranges.size < period) return null
        var value = ranges.take(period).average()
        for (range in ranges.drop(period)) value = (value * (period - 1) + range) / period
        return value
    }

    private fun noSignal(reason: String) =
        StrategyResult(TradeSignal.NONE, null, null, null, null, null, reason)
}

object HayatRiskManager {
    fun canOpenTrade(
        equity: Double,
        dailyStartingEquity: Double,
        dailyRealizedLoss: Double,
        openTrades: Int,
        riskPercent: Double = 0.25,
        dailyLimitPercent: Double = 1.5
    ): Boolean {
        if (equity <= 0.0 || dailyStartingEquity <= 0.0) return false
        if (openTrades >= 1) return false
        if (riskPercent <= 0.0 || riskPercent > 0.25) return false
        if (dailyLimitPercent <= 0.0 || dailyLimitPercent > 1.5) return false
        return dailyRealizedLoss < dailyStartingEquity * dailyLimitPercent / 100.0
    }

    fun riskBudget(equity: Double): Double =
        if (equity > 0.0) equity * 0.25 / 100.0 else 0.0

    fun isVolumeAllowed(estimatedLossAtMinimumLot: Double, equity: Double): Boolean =
        estimatedLossAtMinimumLot > 0.0 && estimatedLossAtMinimumLot <= riskBudget(equity)
}
