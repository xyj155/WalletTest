package com.fastchar.wallettest.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object BigDecimalUtils {

    /**
     * 保留最多 [scale] 位小数，去除尾部多余 0（无千分位）
     */
    fun formatDecimal(
        value: BigDecimal?,
        scale: Int = 8,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        if (value == null) return "0"
        return value.setScale(scale, roundingMode).stripTrailingZeros().toPlainString()
    }

    /**
     * 千分位格式化 + 最多保留 [scale] 位小数（去尾部 0）
     */
    fun formatWithThousands(
        value: BigDecimal?,
        scale: Int = 8,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        if (value == null) return "0"

        val scaled = value.setScale(scale, roundingMode).stripTrailingZeros()
        val symbols = DecimalFormatSymbols(Locale.US)
        val pattern = "#,##0.########" // 最多支持 8 位小数，但自动去 0
        val decimalFormat = DecimalFormat(pattern, symbols)

        return decimalFormat.format(scaled)
    }

    /**
     * 字符串安全转 BigDecimal，异常返回 0
     */
    fun fromString(input: String?): BigDecimal {
        return try {
            input?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}