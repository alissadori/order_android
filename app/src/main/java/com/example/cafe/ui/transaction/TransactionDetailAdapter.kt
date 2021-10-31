package com.example.cafe.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.retrofit.response.transactiondetail.TransactionDetail
import com.example.cafe.utils.idrFormat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_transaction_detail.view.*

class TransactionDetailAdapter(var details: ArrayList<TransactionDetail>) :
    RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_transaction_detail,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = details[position]
        holder.view.text_title.text = detail.nama_produk
        holder.view.text_price.text = "Rp ${idrFormat(detail.harga.toInt())} x ${detail.jumlah}"
        Picasso.get()
            .load(detail.gambar_produk)
            .into(holder.view.image_product)
    }

    override fun getItemCount() = details.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<TransactionDetail>) {
        details.clear()
        details.addAll(data)
        notifyDataSetChanged()
    }

}