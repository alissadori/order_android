package com.example.cafe.retrofit

import com.example.cafe.retrofit.response.cashier.CashierResponse
import com.example.cafe.retrofit.response.transaction.TransactionResponse
import retrofit2.Call
import retrofit2.http.*

interface OwnerEndpoint {

    @GET("kasir")
    fun kasir(): Call<CashierResponse>

    @FormUrlEncoded
    @POST("transaksi-date")
    fun transaksiDate(
        @Field("tgl_awal") tgl_awal: String,
        @Field("tgl_akhir") tgl_akhir: String,
        @Field("no_transaksi") no_transaksi: String
    ): Call<TransactionResponse>

    @FormUrlEncoded
    @POST("transaksi-kasir")
    fun transaksiKasir(
        @Field("username") username: String,
        @Field("no_transaksi") no_transaksi: String
    ): Call<TransactionResponse>
/*
    @FormUrlEncoded
    @POST("transaksi/detail")
    fun transaksiDetail(
        @Field("id_transaksi") id_transaksi: String
    ): Call<TransactionDetailResponse>

    @FormUrlEncoded
    @POST("chart")
    fun chart(
        @Field("tahun") tahun: String
    ): Call<ChartResponse>

    @GET("export-excel")
    fun exportExcel(
        @Query("tgl_awal") tgl_awal: String,
        @Query("tgl_akhir") tgl_akhir: String
    ): Call<ExportResponse>

    @GET("export-pdf")
    fun exportPdf(
        @Query("tgl_awal") tgl_awal: String,
        @Query("tgl_akhir") tgl_akhir: String
    ): Call<ExportResponse>
*/
}