package com.example.simplepersonalexpensetracker.model

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Double,
    val type: String,
    val date: String
)