package com.example.simplepersonalexpensetracker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.simplepersonalexpensetracker.model.Transaction

class ExpenseDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "expense_db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "amount REAL," +
                    "type TEXT," +
                    "date TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }

    // ➕ INSERT
    fun insertTransaction(title: String, amount: Double, type: String, date: String) {
        val db = writableDatabase

        val finalAmount = if (type == "expense") -kotlin.math.abs(amount)
        else kotlin.math.abs(amount)

        val values = ContentValues().apply {
            put("title", title)
            put("amount", finalAmount)
            put("type", type)
            put("date", date)
        }

        db.insert("transactions", null, values)
    }

    // ✏️ UPDATE
    fun updateTransaction(id: Int, title: String, amount: Double, type: String, date: String) {
        val db = writableDatabase

        val finalAmount = if (type == "expense") -kotlin.math.abs(amount)
        else kotlin.math.abs(amount)

        val values = ContentValues().apply {
            put("title", title)
            put("amount", finalAmount)
            put("type", type)
            put("date", date)
        }

        db.update("transactions", values, "id=?", arrayOf(id.toString()))
    }

    fun getByMonth(year: String, month: String): MutableList<Transaction> {
        val list = mutableListOf<Transaction>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM transactions WHERE strftime('%Y', date)=? AND strftime('%m', date)=?",
            arrayOf(year, month)
        )

        while (cursor.moveToNext()) {
            list.add(
                Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getByYear(year: String): MutableList<Transaction> {
        val list = mutableListOf<Transaction>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM transactions WHERE strftime('%Y', date)=?",
            arrayOf(year)
        )

        while (cursor.moveToNext()) {
            list.add(
                Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getByDay(year: String, month: String, day: String): MutableList<Transaction> {
        val list = mutableListOf<Transaction>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM transactions WHERE strftime('%Y', date)=? AND strftime('%m', date)=? AND strftime('%d', date)=?",
            arrayOf(year, month, day)
        )
        while (cursor.moveToNext()) {
            list.add(
                Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
        }
        cursor.close()
        return list
    }

    fun deleteTransaction(id: Int) {
        writableDatabase.delete("transactions", "id=?", arrayOf(id.toString()))
    }
}