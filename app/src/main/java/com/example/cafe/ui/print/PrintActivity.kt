package com.example.cafe.ui.print

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cafe.R
import com.example.cafe.retrofit.response.cart.Cart
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import kotlinx.android.synthetic.main.activity_print.*

private const val TAG = "PrintActivity"

class PrintActivity : AppCompatActivity() {
    private val noTable by lazy { intent.getStringExtra("intent_table") }
    private val name by lazy { intent.getStringExtra("intent_name") }
    private val total by lazy { intent.getStringExtra("intent_total") }
    private val carts: ArrayList<Cart> by lazy { intent.getSerializableExtra("intent_cart") as ArrayList<Cart> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)
        supportActionBar!!.hide()

        Log.e(TAG, carts.toString())

        Printooth.init(this)

        btn_scan.setOnClickListener {
            startActivityForResult(
                Intent(this, ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER
            )
        }

        btn_print.setOnClickListener {
            print()
        }

        btn_close.setOnClickListener {
            finish()
        }

    }

    private fun print() {

        val printables = arrayListOf<Printable>(
            TextPrintable.Builder()
                .setText("Deall Coffee Shop")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                .setNewLinesAfter(1)
                .build(),
            TextPrintable.Builder()
                .setText("Padang, Indonesia")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setNewLinesAfter(1)
                .build(),
            TextPrintable.Builder()
                .setText("No. Meja $noTable - $name")
                .setNewLinesAfter(1)
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .build()
        )

        carts.forEach {
            printables.add(
                TextPrintable.Builder()
                    .setText("${it.nama_produk} x ${it.jumlah} : ${it.harga}")
                    .setNewLinesAfter(1)
                    .build()
            )
        }

        printables.add(
            TextPrintable.Builder()
                .setText(total)
                .setNewLinesAfter(1)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .build()
        )

        printables.add(
            TextPrintable.Builder()
                .setText("Terima kasih")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setNewLinesAfter(1)
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .build()
        )
        Printooth.printer().print(printables)
    }
}