package com.example.simplepersonalexpensetracker

import android.view.View
import android.widget.AdapterView

class SimpleItemSelectedListener(val onChange: () -> Unit) :
    AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        onChange()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}