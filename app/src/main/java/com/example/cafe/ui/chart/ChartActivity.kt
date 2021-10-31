package com.example.cafe.ui.chart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cafe.R
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.chart.ChartResponse
import com.example.cafe.utils.CalendarUtil
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.lazday.pointofsales.page.chart.AxisDateFormatter
import kotlinx.android.synthetic.main.activity_chart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartActivity : AppCompatActivity() {
    private val api by lazy { ApiService.owner }

    private var transactionEntry = ArrayList<BarEntry>()
    private var monthValue = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        listChart()
    }

    private fun setupView() {
        supportActionBar!!.title = "Grafik Panjualan"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun listChart() {
        val calendar = CalendarUtil
        loadingChart(true)
        api.chart(calendar.year.toString())
            .enqueue(object : Callback<ChartResponse> {
                override fun onResponse(
                    call: Call<ChartResponse>,
                    response: Response<ChartResponse>
                ) {
                    loadingChart(false)
                    if (response.isSuccessful) {
                        response.body()?.let { chartResponse ->
                            var index: Int = 0
                            for (chart in chartResponse.data) {
                                index++
                                transactionEntry.add(
                                    BarEntry(
                                        index.toFloat(),
                                        chart.data.toFloat()
                                    )
                                )
                                monthValue.add(chart.nama_bulan)
                            }
                            barChart(transactionEntry, monthValue)
                        }
                    }
                }

                override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                    loadingChart(false)
                }
            })
    }

    private fun loadingChart(loading: Boolean) {
        when (loading) {
            true -> pb_chart.visibility = View.VISIBLE
            false -> pb_chart.visibility = View.GONE
        }
    }

    private fun barChart(transactionEntry: ArrayList<BarEntry>, dateValue: ArrayList<String>) {

        val legend = bar_chart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

        val barDataSet = BarDataSet(transactionEntry, "Penjualan 2021")
        barDataSet.color = Color.CYAN

        bar_chart.description.isEnabled = false
        bar_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bar_chart.data = BarData(barDataSet)
        bar_chart.animateXY(100, 500)
        bar_chart.setDrawGridBackground(false)

        val dateArray = AxisDateFormatter(dateValue.toArray(arrayOfNulls<String>(dateValue.size)))
        bar_chart.xAxis?.valueFormatter = dateArray
    }

    override fun onSupportNavigateUp(): Boolean {
        //finish()
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}