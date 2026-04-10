package com.example.simplepersonalexpensetracker

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplepersonalexpensetracker.database.ExpenseDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var db: ExpenseDatabaseHelper
    private var selectedDate = ""
    private var editId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = ExpenseDatabaseHelper(this)

        val edtTitle   = findViewById<EditText>(R.id.edtTitle)
        val edtAmount  = findViewById<EditText>(R.id.edtAmount)
        val btnSave    = findViewById<Button>(R.id.btnSave)
        val spType     = findViewById<Spinner>(R.id.spType)
        val btnPickDate= findViewById<Button>(R.id.btnPickDate)
        val txtDate    = findViewById<TextView>(R.id.txtDate)

        val types = listOf("expense", "income")
        spType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        val format   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        selectedDate = format.format(calendar.time)
        txtDate.text = "Ngày: $selectedDate"

        // Nhận dữ liệu nếu là chế độ sửa
        editId = intent.getIntExtra("id", -1)
        if (editId != -1) {
            edtTitle.setText(intent.getStringExtra("title"))
            // amount lưu âm cho expense → hiển thị dương
            val rawAmount = intent.getDoubleExtra("amount", 0.0)
            edtAmount.setText(Math.abs(rawAmount).toLong().toString())

            selectedDate = intent.getStringExtra("date") ?: selectedDate
            txtDate.text = "Ngày: $selectedDate"

            val type = intent.getStringExtra("type") ?: "expense"
            spType.setSelection(if (type == "income") 1 else 0)
        }

        btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                cal.set(y, m, d)
                selectedDate = format.format(cal.time)
                txtDate.text = "Ngày: $selectedDate"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnSave.setOnClickListener {
            val t    = edtTitle.text.toString()
            val a    = edtAmount.text.toString().toDoubleOrNull()
            val type = spType.selectedItem.toString()

            if (t.isNotEmpty() && a != null) {
                if (editId == -1) {
                    db.insertTransaction(t, a, type, selectedDate)
                    Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show()
                } else {
                    db.updateTransaction(editId, t, a, type, selectedDate)
                    Toast.makeText(this, "Đã sửa", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Nhập thiếu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}