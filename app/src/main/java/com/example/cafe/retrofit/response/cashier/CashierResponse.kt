package com.example.cafe.retrofit.response.cashier

data class CashierResponse(
    val `data`: List<Cashier>,
    val error: Boolean
)