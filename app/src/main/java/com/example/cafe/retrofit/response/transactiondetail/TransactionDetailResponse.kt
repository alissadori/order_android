package com.example.cafe.retrofit.response.transactiondetail

data class TransactionDetailResponse(
    val `data`: List<TransactionDetail>,
    val error: Boolean
)