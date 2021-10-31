package com.example.cafe.retrofit.response.transactiondetail

data class TransactionDetail(
    val created_at: String,
    val gambar_produk: String,
    val harga: String,
    val id_produk: String,
    val id_transaksi: String,
    val id_transaksi_detail: String,
    val jumlah: String,
    val nama_produk: String,
    val total: String,
    val updated_at: String
)