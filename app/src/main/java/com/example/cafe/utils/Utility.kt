package com.example.cafe.utils

import java.text.DecimalFormat
import java.text.NumberFormat

fun idrFormat(number: Int): String{
    val decimalFormat: NumberFormat = DecimalFormat("#,###")
    return decimalFormat.format(number)
}