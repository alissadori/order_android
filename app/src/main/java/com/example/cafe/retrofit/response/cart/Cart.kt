package com.example.cafe.retrofit.response.cart

import java.io.Serializable

data class Cart(
    val created_at: String,
    val gambar_produk: String,
    var harga: Int,
    val id_keranjang: String,
    val id_produk: String,
    var jumlah: Int,
    val nama_produk: String,
    var total: Int,
    val updated_at: String,
    val username: String
) : Serializable