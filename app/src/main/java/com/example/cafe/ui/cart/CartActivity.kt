package com.example.cafe.ui.cart

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafe.R
import com.example.cafe.preference.PrefManager
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.SubmitResponse
import com.example.cafe.retrofit.response.cart.Cart
import com.example.cafe.retrofit.response.cart.CartResponse
import com.example.cafe.ui.print.PrintActivity
import com.example.cafe.utils.idrFormat
import kotlinx.android.synthetic.main.activity_cart.*
import retrofit2.Call
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private val api by lazy { ApiService.cashier }
    private val pref by lazy { PrefManager(this) }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setupView()
        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        listCart()
    }

    private fun setupView() {
        supportActionBar!!.apply {
            title = "Pesanan"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(this, arrayListOf(), object : CartAdapter.OnAdapterListener {
            override fun onUpdate(cart: Cart) {
                updateCart(cart.id_keranjang, cart.jumlah)
            }

            override fun onDelete(cart: Cart, position: Int) {
                val alertDialog = AlertDialog.Builder(this@CartActivity)
                alertDialog.apply {
                    setTitle("Konfirmasi Hapus")
                    setMessage("Yakin hapus ${cart.nama_produk} dari keranjang?")
                    setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Hapus") { dialog, _ ->
                        dialog.dismiss()
                        deleteCart(cart.id_keranjang)
                        cartAdapter.delete(position)
                    }
                }
                alertDialog.show()
            }

        })
        list_cart.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = cartAdapter
        }
    }

    private fun setupListener() {
        btn_checkout.setOnClickListener {
            if (cartAdapter.carts.size > 0 && et_customername.text.isNotEmpty() &&
                et_customertable.text.isNotEmpty()
            ) {
                checkout()
            } else {
                Toast.makeText(
                    applicationContext, "Isi data dengan benar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun listCart() {
        loadingCart(true)
        api.keranjang(pref.getString("pref_user_username")!!)
            .enqueue(object : retrofit2.Callback<CartResponse> {
                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    loadingCart(false)
                }

                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    loadingCart(false)
                    if (response.isSuccessful) {
                        cartResponse(response.body()!!)
                    }
                }
            })
    }

    private fun updateCart(id: String, amount: Int) {
        Log.d("CartActivity", "amount:$amount")
        api.updateKeranjang(id, amount)
            .enqueue(object : retrofit2.Callback<SubmitResponse> {
                override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                    loadingCart(false)
                }

                override fun onResponse(
                    call: Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {
                }
            })
    }

    private fun deleteCart(id_keranjang: String) {
        api.deleteKeranjang(id_keranjang)
            .enqueue(object : retrofit2.Callback<SubmitResponse> {
                override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {

                }

            })
    }

    private fun loadingCart(loading: Boolean) {
        when (loading) {
            true -> {
                pb_cart.visibility = View.VISIBLE
            }
            false -> {
                pb_cart.visibility = View.GONE
            }
        }
    }

    private fun cartResponse(cartResponse: CartResponse) {
        cartAdapter.setData(cartResponse)
    }

    fun liveTotal() {
        var total = 0
        for (cart in cartAdapter.carts) {
            total += cart.total
        }
        tv_totalprice.text = "Rp. ${idrFormat(total)}"
    }

    private fun checkout() {
        loadingCheckout(true)
        api.checkout(
            pref.getString("pref_user_username")!!,
            et_customername.text.toString(),
            et_customertable.text.toString(),
            et_customernote.text.toString()
        )
            .enqueue(object : retrofit2.Callback<SubmitResponse> {
                override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                    loadingCheckout(false)
                }

                override fun onResponse(
                    call: Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {
                    loadingCheckout(false)
                    if (response.isSuccessful) {
                        checkoutResponse(response.body()!!)
                    }
                }

            })
    }

    private fun loadingCheckout(loading: Boolean) {
        when (loading) {
            true -> {
                btn_checkout.isEnabled = false
                btn_checkout.text = "Menyimpan..."
            }
            false -> {
                btn_checkout.isEnabled = true
                btn_checkout.text = "Bayar"
            }
        }
    }

    private fun checkoutResponse(submitResponse: SubmitResponse) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("${submitResponse.message}!")
            setMessage("Cetak bukti pembayaran?")
            setNegativeButton("Tutup", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                finish()
            })
            setPositiveButton("Cetak", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                startActivity(Intent(this@CartActivity, PrintActivity::class.java))
                //val intent = Intent(this@CartActivity, PrintActivity::class.java)
                /*startActivity(
                    intent
                        .putExtra("intent_table", et_customertable.text.toString())
                        .putExtra("intent_name", et_customername.text.toString())
                        .putExtra("intent_total", tv_totalprice.text.toString())
                        .putExtra("intent_cart", cartAdapter.carts)
                )*/
                finish()
            })
        }
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}