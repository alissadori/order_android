package com.example.cafe.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.utils.idrFormat
import com.example.cafe.retrofit.response.transaction.Transaction
import kotlinx.android.synthetic.main.adapter_transaction.view.*

class TransactionAdapter (var transactions: ArrayList<Transaction>, var listener: OnAdapterListener):
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_transaction,
                parent, false
            )
        )

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.view.text_date.text = transaction.created_at
        holder.view.text_no.text = transaction.no_transaksi
        holder.view.text_cashier.text = transaction.username
        holder.view.text_total.text = "Rp ${idrFormat(transaction.total.toInt())}"
        holder.view.container_adapter.setOnClickListener {
            listener.onClick(transaction)
        }

    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    fun setData(data: List<Transaction>) {
        transactions.clear()
        transactions.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(category: Transaction)
    }

}