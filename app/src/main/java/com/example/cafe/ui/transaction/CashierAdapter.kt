package com.example.cafe.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.retrofit.response.cashier.Cashier
import kotlinx.android.synthetic.main.adapter_cashier.view.*

class CashierAdapter (var cashiers: ArrayList<Cashier>, var listener: OnAdapterListener):
    RecyclerView.Adapter<CashierAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_cashier,
                parent, false
            )
        )

    override fun getItemCount() = cashiers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cashier = cashiers[position]
        holder.view.tv_cashier_name.text = cashier.nama
        holder.view.setOnClickListener {
            listener.onClick(cashier)
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    fun setData(data: List<Cashier>) {
        cashiers.clear()
        cashiers.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(cashier: Cashier)
    }

}