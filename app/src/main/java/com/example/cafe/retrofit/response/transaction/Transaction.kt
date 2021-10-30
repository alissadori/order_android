package com.example.cafe.retrofit.response.transaction

data class Transaction(
    val catatan: String,
    val created_at: String,
    val id_transaksi: String,
    val nama_pelanggan: String,
    val no_meja: String,
    val no_transaksi: String,
    val total: String,
    val updated_at: String,
    val username: String
)