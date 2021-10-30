package com.example.cafe.retrofit

import com.example.cafe.retrofit.response.SubmitResponse
import com.example.cafe.retrofit.response.cart.CartResponse
import com.example.cafe.retrofit.response.category.CategoryResponse
import com.example.cafe.retrofit.response.product.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface CashierEndpoint {

    @GET("kategori")
    fun kategori(): Call<CategoryResponse>

    @GET("produk")
    fun produk(
        @Query("nama") nama: String?,
        @Query("id_kategori") id_kategori: Int?
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("keranjang")
    fun keranjang(
        @Field("username") username: String,
        @Field("id_produk") id_produk: Int,
        @Field("jumlah") jumlah: Int
    ): Call<SubmitResponse>

    @GET("keranjang")
    fun keranjang(
        @Query("username") username: String
    ): Call<CartResponse>

    @FormUrlEncoded
    @PUT("keranjang/{id}")
    fun updateKeranjang(
        @Path("id") id_keranjang: String,
        @Field("jumlah") jumlah: Int
    ): Call<SubmitResponse>

    @DELETE("keranjang/{id}")
    fun deleteKeranjang(
        @Path("id") id_keranjang: String
    ): Call<SubmitResponse>

    @FormUrlEncoded
    @POST("checkout")
    fun checkout(
        @Field("username") username: String,
        @Field("nama_pelanggan") nama_pelanggan: String,
        @Field("no_meja") no_meja: String,
        @Field("catatan") catatan: String
    ): Call<SubmitResponse>

}