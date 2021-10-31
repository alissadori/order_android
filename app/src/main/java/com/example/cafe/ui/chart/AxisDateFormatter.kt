package com.lazday.pointofsales.page.chart

import com.github.mikephil.charting.formatter.ValueFormatter

class AxisDateFormatter(private val mValues: Array<String>) :  ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value >= 0) {
            if (mValues.size > value.toInt()) {
                mValues[value.toInt()]
            } else ""
        } else {
            ""
        }
    }
}