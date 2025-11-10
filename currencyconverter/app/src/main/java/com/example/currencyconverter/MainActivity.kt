package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    // Tỷ giá cố định so với USD (base = 1.0). Bạn có thể chỉnh tùy ý.
    private val rates = linkedMapOf(
        "USD" to 1.00,
        "EUR" to 0.92,
        "JPY" to 151.0,
        "GBP" to 0.79,
        "VND" to 24500.0,
        "CNY" to 7.2,
        "KRW" to 1350.0,
        "AUD" to 1.53,
        "CAD" to 1.37,
        "SGD" to 1.35,
        "THB" to 36.0,
        "INR" to 83.0
    )

    private lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText

    // Cờ chống vòng lặp khi 2 EditText cập nhật lẫn nhau
    private var isUpdatingFrom = false
    private var isUpdatingTo = false
    private var lastEdited: LastEdited = LastEdited.FROM

    private enum class LastEdited { FROM, TO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spFrom = findViewById(R.id.spFrom)
        spTo = findViewById(R.id.spTo)
        etFrom = findViewById(R.id.etFrom)
        etTo = findViewById(R.id.etTo)

        val symbols = rates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, symbols)
        spFrom.adapter = adapter
        spTo.adapter = adapter

        // Mặc định: USD -> VND
        spFrom.setSelection(symbols.indexOf("USD").coerceAtLeast(0))
        spTo.setSelection(symbols.indexOf("VND").coerceAtLeast(0))

        // Watchers cho 2 EditText
        etFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdatingFrom) return
                lastEdited = LastEdited.FROM
                convertFromTo()
            }
        })

        etTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdatingTo) return
                lastEdited = LastEdited.TO
                convertToFrom()
            }
        })

        // Khi đổi Spinner -> tính lại dựa trên EditText vừa sửa lần cuối
        val onSpinnerChanged = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                when (lastEdited) {
                    LastEdited.FROM -> convertFromTo()
                    LastEdited.TO -> convertToFrom()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spFrom.onItemSelectedListener = onSpinnerChanged
        spTo.onItemSelectedListener = onSpinnerChanged
    }

    private fun getRate(symbol: String) = rates[symbol] ?: 1.0

    private fun convert(amount: Double, fromSym: String, toSym: String): Double {
        val usd = amount / getRate(fromSym)     // -> USD
        return usd * getRate(toSym)             // USD -> đích
    }

    private fun parseDouble(s: String): Double? =
        s.replace(',', '.').trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull()

    private fun convertFromTo() {
        val fromSym = spFrom.selectedItem.toString()
        val toSym = spTo.selectedItem.toString()
        val v = parseDouble(etFrom.text.toString())
        val out = if (v != null) convert(v, fromSym, toSym) else null

        isUpdatingTo = true
        etTo.setText(out?.let { formatSmart(it) } ?: "")
        etTo.setSelection(max(0, etTo.text.length))
        isUpdatingTo = false
    }

    private fun convertToFrom() {
        val fromSym = spFrom.selectedItem.toString()
        val toSym = spTo.selectedItem.toString()
        val v = parseDouble(etTo.text.toString())
        val out = if (v != null) convert(v, toSym, fromSym) else null

        isUpdatingFrom = true
        etFrom.setText(out?.let { formatSmart(it) } ?: "")
        etFrom.setSelection(max(0, etFrom.text.length))
        isUpdatingFrom = false
    }

    // Định dạng gọn: không nhồi số 0 thập phân dư
    private fun formatSmart(x: Double): String {
        val s = String.format("%.6f", x) // 6 chữ số thập phân
        return s.trimEnd('0').trimEnd('.')
    }
}
