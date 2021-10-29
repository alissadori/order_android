package com.example.cafe.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafe.R
import com.example.cafe.preference.PrefManager
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.SubmitResponse
import com.example.cafe.retrofit.response.category.Category
import com.example.cafe.retrofit.response.category.CategoryResponse
import com.example.cafe.retrofit.response.product.Product
import com.example.cafe.retrofit.response.product.ProductResponse
import com.example.cafe.ui.cart.CartActivity
import com.example.cafe.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val api by lazy { ApiService.cashier }
    private val pref by lazy { PrefManager(this) }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    private lateinit var menuItemCount: MenuItem

    private var categoryId: Int? = null
    private var itemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        listCategory()
        listProduct()
    }

    private fun setupRecyclerView() {
        categoryAdapter =
            CategoryAdapter(arrayListOf(), object : CategoryAdapter.OnAdapterListener {
                override fun onClick(category: Category) {
                    categoryId = category.id_kategori
                    listProduct()
                }
            })
        list_category.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = categoryAdapter
        }
        productAdapter =
            ProductAdapter(arrayListOf(), object : ProductAdapter.OnAdapterListener {
                override fun onClick(product: Product) {
                    cartDialog(product)
                    Toast.makeText(applicationContext, product.nama_produk, Toast.LENGTH_SHORT)
                        .show()
//                    Log.d(TAG, "id_produk:${product.id_produk}")
                }
            })
        list_product.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = productAdapter
        }
    }

    private fun cartDialog(product: Product) {
        val cartDialog = layoutInflater.inflate(R.layout.dialog_cart, null)
        val dialog = BottomSheetDialog(this)
        dialog.apply {
            setContentView(cartDialog)
            setTitle("")
            setCancelable(false)
        }
        cartDialog.findViewById<TextView>(R.id.tv_title).text = product.nama_produk
        Picasso.get()
            .load(product.gambar_produk)
            .into(cartDialog.findViewById<ImageView>(R.id.iv_image_product))

        val editQty = cartDialog.findViewById<TextView>(R.id.tv_qty)
        cartDialog.findViewById<Button>(R.id.btn_plus).setOnClickListener {
            var qty = editQty.text.toString().toInt()
            qty += 1
            editQty.text = qty.toString()
        }
        cartDialog.findViewById<Button>(R.id.btn_min).setOnClickListener {
            var qty = editQty.text.toString().toInt()
            if (qty > 0) qty -= 1
            editQty.text = qty.toString()
        }
        cartDialog.findViewById<Button>(R.id.btn_add_cart).setOnClickListener {
            itemCount += 1
            menuItemCount.title = itemCount.toString()
            Toast.makeText(
                this,
                "${product.nama_produk} ditambahkan ke keranjang",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            addToCart(product.id_produk.toInt(), editQty.text.toString().toInt())
        }
        cartDialog.findViewById<ImageView>(R.id.iv_icon_close).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addToCart(id: Int, qty: Int) {
        api.keranjang(pref.getString("pref_user_username")!!, id, qty)
            .enqueue(object : retrofit2.Callback<SubmitResponse> {
                override fun onFailure(call: retrofit2.Call<SubmitResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: retrofit2.Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {
                    if (response.isSuccessful) {
                        val submitResponse = response.body()!!
                        Toast.makeText(
                            applicationContext,
                            submitResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun setupListener() {
        et_searchproduct.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listProduct()
                true
            } else {
                false
            }
        }
        tv_allcategory.setOnClickListener {
            categoryId = null
            listProduct()
        }
    }

    private fun listCategory() {
        api.kategori()
            .enqueue(object : retrofit2.Callback<CategoryResponse> {
                override fun onResponse(
                    call: retrofit2.Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful)
                        response.body()?.let {
                            categoryResponse(it)
                        }
                }

                override fun onFailure(call: retrofit2.Call<CategoryResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t")
                }

            })
    }

    private fun categoryResponse(categoryResponse: CategoryResponse) {
        categoryAdapter.setData(categoryResponse)
    }

    private fun listProduct() {
        loadingProduct(true)
        api.produk(et_searchproduct.text.toString(), categoryId)
            .enqueue(object : retrofit2.Callback<ProductResponse> {
                override fun onResponse(
                    call: retrofit2.Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {
                    loadingProduct(false)
                    if (response.isSuccessful)
                        response.body()?.let {
                            productResponse(it)
                        }
                }

                override fun onFailure(call: retrofit2.Call<ProductResponse>, t: Throwable) {
                    loadingProduct(false)
                    Log.d(TAG, "errorResponse: $t")
                }
            })
    }

    private fun loadingProduct(loading: Boolean) {
        when (loading) {
            true -> {
                pb_product.visibility = View.VISIBLE
            }
            false -> {
                pb_product.visibility = View.GONE
            }
        }
    }

    private fun productResponse(productResponse: ProductResponse) {
        productAdapter.setData(productResponse)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menuItemCount = menu!!.findItem(R.id.action_count)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            R.id.action_logout -> {
                pref.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}