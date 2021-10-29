package com.example.cafe.retrofit.response.product

data class Product(
    val created_at: CreatedAt,
    val deleted_at: Any,
    val gambar_produk: String,
    val harga: String,
    val id_kategori: String,
    val id_produk: String,
    val nama_produk: String,
    val updated_at: UpdatedAt
)