package com.example.numberconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.floor
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var etLimit: EditText
    private lateinit var lv: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: ArrayAdapter<Int>

    private lateinit var rbOdd: RadioButton
    private lateinit var rbPrime: RadioButton
    private lateinit var rbPerfect: RadioButton
    private lateinit var rbEven: RadioButton
    private lateinit var rbSquare: RadioButton
    private lateinit var rbFibo: RadioButton

    private lateinit var radios: List<RadioButton>
    private var isMutatingChecks = false // tránh vòng lặp khi ép uncheck các nút khác

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLimit = findViewById(R.id.etLimit)
        lv = findViewById(R.id.lvNumbers)
        tvEmpty = findViewById(R.id.tvEmpty)

        rbOdd = findViewById(R.id.rbOdd)
        rbPrime = findViewById(R.id.rbPrime)
        rbPerfect = findViewById(R.id.rbPerfect)
        rbEven = findViewById(R.id.rbEven)
        rbSquare = findViewById(R.id.rbSquare)
        rbFibo = findViewById(R.id.rbFibo)

        radios = listOf(rbOdd, rbPrime, rbPerfect, rbEven, rbSquare, rbFibo)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lv.adapter = adapter

        // Mặc định giống ảnh: chọn "Số lẻ"
        rbOdd.isChecked = true

        // Bảo đảm chỉ chọn 1 trong 6 RadioButton dù không dùng RadioGroup
        radios.forEach { rb ->
            rb.setOnCheckedChangeListener { button, isChecked ->
                if (isMutatingChecks) return@setOnCheckedChangeListener
                if (isChecked) {
                    isMutatingChecks = true
                    radios.filter { it != button }.forEach { it.isChecked = false }
                    isMutatingChecks = false
                    refreshList()
                } else {
                    // Nếu người dùng chạm để bỏ chọn nút đang bật → bật lại để luôn có 1 lựa chọn
                    if (radios.none { it.isChecked }) {
                        isMutatingChecks = true
                        (button as RadioButton).isChecked = true
                        isMutatingChecks = false
                    }
                }
            }
        }

        // Cập nhật ngay khi thay đổi giá trị N
        etLimit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshList()
            }
        })

        refreshList()
    }

    private fun refreshList() {
        val n = etLimit.text.toString().toIntOrNull()
        if (n == null || n <= 0) {
            showEmpty(); return
        }

        val data = when {
            rbOdd.isChecked    -> gen(n) { it % 2 != 0 }
            rbEven.isChecked   -> gen(n) { it % 2 == 0 }
            rbPrime.isChecked  -> gen(n) { isPrime(it) }
            rbSquare.isChecked -> gen(n) { isSquare(it) }
            rbPerfect.isChecked-> gen(n) { isPerfect(it) }
            rbFibo.isChecked   -> genFiboLessThan(n)
            else -> emptyList()
        }

        if (data.isEmpty()) showEmpty() else showList(data)
    }

    private fun showEmpty() {
        adapter.clear()
        adapter.notifyDataSetChanged()
        tvEmpty.visibility = View.VISIBLE
        lv.visibility = View.GONE
    }

    private fun showList(items: List<Int>) {
        tvEmpty.visibility = View.GONE
        lv.visibility = View.VISIBLE
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun gen(n: Int, pred: (Int) -> Boolean): List<Int> =
        (1 until n).filter(pred)

    private fun isPrime(x: Int): Boolean {
        if (x < 2) return false
        if (x % 2 == 0) return x == 2
        val r = floor(sqrt(x.toDouble())).toInt()
        var i = 3
        while (i <= r) {
            if (x % i == 0) return false
            i += 2
        }
        return true
    }

    private fun isSquare(x: Int): Boolean {
        if (x < 0) return false
        val r = floor(sqrt(x.toDouble())).toInt()
        return r * r == x
    }

    private fun isPerfect(x: Int): Boolean {
        if (x < 2) return false
        var sum = 1
        val r = floor(sqrt(x.toDouble())).toInt()
        var i = 2
        while (i <= r) {
            if (x % i == 0) {
                sum += i
                val pair = x / i
                if (pair != i) sum += pair
            }
            i++
        }
        return sum == x
    }

    private fun genFiboLessThan(n: Int): List<Int> {
        val list = mutableListOf<Int>()
        var a = 0
        var b = 1
        while (a < n) {
            if (a > 0) list.add(a)
            val c = a + b
            a = b
            b = c
        }
        return list
    }
}
