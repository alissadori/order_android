package com.example.cafe.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.retrofit.response.cart.Cart
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_cart.view.*
import kotlinx.android.synthetic.main.adapter_product.view.image_product

class CartAdapter(
    val context: Context,
    var carts: ArrayList<Cart>,
    val listener: OnAdapterListener
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val TAG = "CartAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_cart,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = carts[position]
        holder.view.tv_title.text = cart.nama_produk
        Picasso.get()
            .load(cart.gambar_produk)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .into(holder.view.image_product)

        holder.view.tv_price.text = cart.harga.toString()
        holder.view.tv_qty.text = cart.jumlah.toString()

       cart.total = cart.harga * cart.jumlah

        (context as CartActivity).liveTotal()
        holder.view.tv_plus.setOnClickListener {
            cart.jumlah++
            cart.total = cart.harga * cart.jumlah
            holder.view.tv_qty.text = cart.jumlah.toString()
            (context as CartActivity).liveTotal()
            listener.onUpdate(cart)
        }
        holder.view.tv_minus.setOnClickListener {
            if (cart.jumlah > 1) {
                cart.jumlah--
                cart.total = cart.harga * cart.jumlah
                holder.view.tv_qty.text = cart.jumlah.toString()
                (context as CartActivity).liveTotal()
                listener.onUpdate(cart)
            } else {
                listener.onDelete(cart, position)
            }
        }

    }

    override fun getItemCount() = carts.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: ArrayList<Cart>) {
        carts.clear()
        carts.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onUpdate(cart: Cart)
        fun onDelete(cart: Cart, position: Int)
    }

    fun delete(position: Int) {
        carts.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, carts.size)
        (context as CartActivity).liveTotal()
    }

}