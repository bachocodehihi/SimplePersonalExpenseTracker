package com.example.simplepersonalexpensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplepersonalexpensetracker.database.ExpenseDatabaseHelper
import com.example.simplepersonalexpensetracker.model.Transaction

class MainActivity : AppCompatActivity() {

    private lateinit var db: ExpenseDatabaseHelper
    private lateinit var listView: ListView
    private lateinit var txtTotal: TextView
    private lateinit var spYear: Spinner
    private lateinit var spMonth: Spinner
    private lateinit var spDay: Spinner


    private val transactionList = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = ExpenseDatabaseHelper(this)

        listView  = findViewById(R.id.listView)
        txtTotal  = findViewById(R.id.txtTotal)
        spYear    = findViewById(R.id.spYear)
        spMonth   = findViewById(R.id.spMonth)
        spDay     = findViewById(R.id.spDay)

        val years = mutableListOf("All")
        years.addAll((2020..2030).map { it.toString() })
        spYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)

        val months = mutableListOf("All")
        months.addAll((1..12).map { String.format("%02d", it) })
        spMonth.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)

        buildDaySpinner()

        val listener = { reload() }

        spYear.onItemSelectedListener  = SimpleItemSelectedListener {
            buildDaySpinner()
            reload()
        }
        spMonth.onItemSelectedListener = SimpleItemSelectedListener {
            buildDaySpinner()
            reload()
        }
        spDay.onItemSelectedListener   = SimpleItemSelectedListener { reload() }

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }

    private fun buildDaySpinner() {
        val days = mutableListOf("All")
        val month = spMonth.selectedItem?.toString() ?: "All"
        val year  = spYear.selectedItem?.toString()?.toIntOrNull() ?: 2024

        val maxDay = when (month) {
            "All" -> 31
            "02"  -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            in listOf("04", "06", "09", "11") -> 30
            else  -> 31
        }
        days.addAll((1..maxDay).map { String.format("%02d", it) })

        val prev = spDay.selectedItem?.toString() ?: "All"
        spDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        val idx = days.indexOf(prev)
        if (idx >= 0) spDay.setSelection(idx)
    }

    private fun reload() {
        val year  = spYear.selectedItem?.toString()  ?: return
        val month = spMonth.selectedItem?.toString() ?: return
        val day   = spDay.selectedItem?.toString()   ?: return

        when {
            year  == "All"          -> loadByAll()
            month == "All"          -> loadByYear(year)
            day   == "All"          -> loadByMonth(year, month)
            else                    -> loadByDay(year, month, day)
        }
    }

    private fun loadByAll() {
        val allYears = (2020..2030).map { it.toString() }

        var totalAll = 0.0

        val displayList = allYears.map { year ->
            val list = db.getByYear(year)
            val total = list.sumOf { it.amount }
            totalAll += total
            "Năm $year: ${total.toLong()} VND"
        }

        txtTotal.text = "Tổng tất cả: ${totalAll.toLong()} VND"
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)

        listView.setOnItemClickListener(null)
        listView.setOnItemLongClickListener(null)
    }

    private fun loadByYear(year: String) {
        val list = db.getByYear(year)

        val monthMap = (1..12).associateWith { 0.0 }.toMutableMap()
        for (item in list) {
            val m = item.date.substring(5, 7).toInt()
            monthMap[m] = monthMap[m]!! + item.amount
        }

        val totalYear = monthMap.values.sum()

        val displayList = (1..12).map { m ->
            "Tháng $m: ${monthMap[m]!!.toLong()} VND"
        }

        txtTotal.text = "Tổng năm $year: ${totalYear.toLong()} VND"
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)

        listView.setOnItemClickListener(null)
        listView.setOnItemLongClickListener(null)
    }

    private fun loadByMonth(year: String, month: String) {
        transactionList.clear()
        transactionList.addAll(db.getByMonth(year, month))

        val total = transactionList.sumOf { it.amount }

        val displayList = transactionList.map { "${it.title}: ${it.amount.toLong()} VND" }

        txtTotal.text = "Tổng tháng $month/$year: ${total.toLong()} VND"
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)

        listView.setOnItemClickListener { _, _, position, _ ->
            openEdit(transactionList[position])
        }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            db.deleteTransaction(transactionList[position].id)
            loadByMonth(year, month)
            true
        }
    }
    private fun loadByDay(year: String, month: String, day: String) {
        transactionList.clear()
        transactionList.addAll(db.getByDay(year, month, day))

        val total = transactionList.sumOf { it.amount }

        val displayList = transactionList.map { "${it.title}: ${it.amount.toLong()} VND" }

        txtTotal.text = "Tổng ngày $day/$month/$year: ${total.toLong()} VND"
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)

        listView.setOnItemClickListener { _, _, position, _ ->
            openEdit(transactionList[position])
        }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            db.deleteTransaction(transactionList[position].id)
            loadByDay(year, month, day)
            true
        }
    }

    private fun openEdit(item: Transaction) {
        val intent = Intent(this, AddExpenseActivity::class.java)
        intent.putExtra("id",     item.id)
        intent.putExtra("title",  item.title)
        intent.putExtra("amount", item.amount)
        intent.putExtra("type",   item.type)
        intent.putExtra("date",   item.date)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        reload()
    }
}