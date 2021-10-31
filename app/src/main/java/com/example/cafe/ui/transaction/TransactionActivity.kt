package com.example.cafe.ui.transaction

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafe.R
import com.example.cafe.preference.PrefManager
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.cashier.Cashier
import com.example.cafe.retrofit.response.cashier.CashierResponse
import com.example.cafe.retrofit.response.export.ExportResponse
import com.example.cafe.retrofit.response.transaction.Transaction
import com.example.cafe.retrofit.response.transaction.TransactionResponse
import com.example.cafe.ui.BaseActivity
import com.example.cafe.ui.chart.ChartActivity
import com.example.cafe.ui.login.LoginActivity
import com.example.cafe.utils.CalendarUtil
import kotlinx.android.synthetic.main.activity_transaction.*
import retrofit2.Call
import retrofit2.Response

const val TAG = "TransactionActivity"
const val transactionByDate = 0
const val transactionByCashier = 1

class TransactionActivity : BaseActivity() {
    private val pref by lazy { PrefManager(this) }
    private val api by lazy { ApiService.owner }

    private var dateStart: String = ""
    private var dateEnd: String = ""
    private var usernameCashier: String = ""
    private var currentTransactionBy: Int = 0

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var cashierAdapter: CashierAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setupView()
        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadingTransaction(false)
        listCashier()
    }

    private fun setupView() {
        supportActionBar!!.title = "Laporan Transaksi"
        viewTransactionBy(transactionByDate)
    }

    private fun viewTransactionBy(transactionBy: Int) {
        currentTransactionBy = transactionBy
        when (transactionBy) {
            transactionByDate -> {
                rb_date.isChecked = true
                et_date_start.visibility = View.VISIBLE
                et_date_end.visibility = View.VISIBLE
                list_cashier.visibility = View.GONE
            }
            transactionByCashier -> {
                rb_date.isChecked = true
                et_date_start.visibility = View.GONE
                et_date_end.visibility = View.GONE
                list_cashier.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListener() {
        rg_filter_by.setOnCheckedChangeListener { group, checkedId ->
            val rb: RadioButton = group.findViewById(checkedId)
            when (rb.id) {
                R.id.rb_date -> {
                    viewTransactionBy(transactionByDate)
                }
                R.id.rb_cashier -> {
                    viewTransactionBy(transactionByCashier)
                }
            }
        }
        et_no_transaction.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listTransaction(currentTransactionBy)
                true
            }
            false
        }
        et_date_start.setOnClickListener {
            val calender = CalendarUtil
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    dateStart = "$year-${(month + 1)}-$day"
                    et_date_start.setText(dateStart)
                    listTransaction(transactionByDate)
                },
                calender.year,
                calender.month,
                calender.day
            )
            datePickerDialog.show()
        }
        et_date_end.setOnClickListener {
            val calender = CalendarUtil
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    dateEnd = "$year-${(month + 1)}-$day"
                    et_date_end.setText(dateEnd)
                    listTransaction(transactionByDate)
                },
                calender.year,
                calender.month,
                calender.day
            )
            datePickerDialog.show()
        }
    }

    private fun setupRecyclerView() {
        cashierAdapter =
            CashierAdapter(
                arrayListOf(),
                object :
                    CashierAdapter.OnAdapterListener {
                    override fun onClick(cashier: Cashier) {
                        usernameCashier = cashier.username
                        listTransaction(transactionByCashier)
                    }
                })
        list_cashier.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = cashierAdapter
        }
        transactionAdapter =
            TransactionAdapter(arrayListOf(), object : TransactionAdapter.OnAdapterListener {
                override fun onClick(transaction: Transaction) {
                    val bundle = Bundle()
                    bundle.putSerializable("arg_transaction", transaction)
                    val transactionDetailFragment = TransactionDetailFragment()
                    transactionDetailFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_transaction, transactionDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            })
        list_transaction.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = transactionAdapter
        }
    }

    private fun listCashier() {
        api.kasir().enqueue(object : retrofit2.Callback<CashierResponse> {
            override fun onFailure(call: Call<CashierResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CashierResponse>,
                response: Response<CashierResponse>
            ) {
                if (response.isSuccessful) {
                    cashierResponse(response.body()!!)
                }
            }
        })
    }

    private fun cashierResponse(cashierResponse: CashierResponse) {
        cashierAdapter.setData(cashierResponse.data)
    }

    private fun listTransaction(transactionBy: Int) {
        var call: Call<TransactionResponse>? = null
        when (transactionBy) {
            transactionByDate -> {
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    call = api.transaksiDate(dateStart, dateEnd, et_no_transaction.text.toString())
                } else {
                    Toast.makeText(
                        applicationContext, "Lengkapi tanggal pencarian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            transactionByCashier -> {
                call = api.transaksiKasir(usernameCashier, et_no_transaction.text.toString())
            }
        }
        loadingTransaction(true)
        call?.let {
            it.enqueue(object : retrofit2.Callback<TransactionResponse> {
                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    loadingTransaction(false)
                }

                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    loadingTransaction(false)
                    if (response.isSuccessful) {
                        transactionResponse(response.body()!!)
                    }
                }
            })
        }
    }

    private fun loadingTransaction(loading: Boolean) {
        when (loading) {
            true -> {
                pb_transaction.visibility = View.VISIBLE
            }
            false -> {
                pb_transaction.visibility = View.GONE
            }
        }
    }

    private fun transactionResponse(transactionResponse: TransactionResponse) {
        transactionAdapter.setData(transactionResponse.data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_chart -> {
                startActivity(Intent(this, ChartActivity::class.java))
                true
            }
            R.id.action_export_excel -> {
                viewTransactionBy(transactionByDate)
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    export("excel")
                } else {
                    Toast.makeText(
                        applicationContext, "Lengkapi tanggal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            R.id.action_export_pdf -> {
                viewTransactionBy(transactionByDate)
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    export("pdf")
                } else {
                    Toast.makeText(
                        applicationContext, "Lengkapi tanggal pencarian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun export(exportType: String) {
        var call: Call<ExportResponse>? = null
        when (exportType) {
            "excel" -> call = api.exportExcel(dateStart, dateEnd)
            "pdf" -> call = api.exportPdf(dateStart, dateEnd)
        }
        call?.let {
            Toast.makeText(applicationContext, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
            call.enqueue(object : retrofit2.Callback<ExportResponse> {
                override fun onResponse(
                    call: Call<ExportResponse>,
                    response: Response<ExportResponse>
                ) {
                    if (response.isSuccessful) {
                        exportResponse(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ExportResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Export Gagal", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun exportResponse(exportResponse: ExportResponse) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(exportResponse.data)
        startActivity(openURL)
    }
}