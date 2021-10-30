package com.example.cafe.retrofit.response.transaction

data class TransactionResponse(
    val `data`: List<Transaction>,
    val error: Boolean
)