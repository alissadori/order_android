package com.example.cafe.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafe.R
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.transaction.Transaction
import com.example.cafe.retrofit.response.transactiondetail.TransactionDetailResponse
import kotlinx.android.synthetic.main.fragment_transaction_detail.view.*
import retrofit2.Call
import retrofit2.Response

class TransactionDetailFragment : Fragment() {

    private val api by lazy { ApiService.owner }
    private val actionBar by lazy { (requireActivity() as TransactionActivity).supportActionBar!! }

    private lateinit var fragmentView: View
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter

    private var transactionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_transaction_detail, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        argumentsTransaction()
        detailTransaction()
    }

    private fun setupRecyclerView() {
        transactionDetailAdapter = TransactionDetailAdapter(arrayListOf())
        fragmentView.list_detail_product.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionDetailAdapter
        }
    }

    private fun argumentsTransaction() {
        val transaction = arguments!!.getSerializable("arg_transaction") as Transaction
        transactionId = transaction.id_transaksi
        fragmentView.text_table.text = transaction.no_meja
        fragmentView.text_name.text = transaction.nama_pelanggan
        fragmentView.text_note.text = transaction.catatan
    }

    private fun setupView() {
        actionBar.title = "Detail Transaksi"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun detailTransaction() {
        transactionId?.let {
            loadingDetail(true)
            api.transaksiDetail(it)
                .enqueue(object : retrofit2.Callback<TransactionDetailResponse> {
                    override fun onResponse(
                        call: Call<TransactionDetailResponse>,
                        response: Response<TransactionDetailResponse>
                    ) {
                        loadingDetail(true)
                        if (response.isSuccessful) {
                            transactionDetailResponse(response.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<TransactionDetailResponse>, t: Throwable) {
                        loadingDetail(false)
                    }
                })
        }
    }

    private fun loadingDetail(loading: Boolean) {
        when (loading) {
            true -> {
                fragmentView.pb_transaction_detail.visibility = View.VISIBLE
            }
            false -> {
                fragmentView.pb_transaction_detail.visibility = View.GONE
            }
        }
    }

    private fun transactionDetailResponse(transactionDetailResponse: TransactionDetailResponse) {
        transactionDetailAdapter.setData(transactionDetailResponse.data)
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.title = "Laporan Transaksi "
    }
}