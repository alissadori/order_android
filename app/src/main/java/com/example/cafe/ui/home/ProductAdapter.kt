package com.example.cafe.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.retrofit.response.product.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_product.view.*

class ProductAdapter(var products: ArrayList<Product>, var listener: OnAdapterListener) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val TAG = "ProductAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_product,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val product = products[position]
        holder.view.tv_name_product.text = product.nama_produk
        Log.d(TAG, "imageProduct: ${product.gambar_produk}")
        Picasso.get()
            .load(product.gambar_produk)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .into(holder.view.image_product)
        holder.view.setOnClickListener {
            listener.onClick(product)
        }
//      holder.view.text_price.text = "Rp ${Helper.idrFormat(product.harga.toInt())}"
        /*holder.view.setOnLongClickListener {
            listener.onClick(product)
            true
        }*/
    }

    override fun getItemCount() = products.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newTrailer: ArrayList<Product>) {
        products.clear()
        products.addAll(newTrailer)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(product: Product)
    }

}