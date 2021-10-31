package com.example.cafe.retrofit.response.chart

data class ChartResponse(
    val `data`: List<Chart>,
    val error: Boolean
)