package com.example.bt28102025

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private lateinit var tv: TextView

    private var acc: Long = 0L             // tích lũy
    private var cur: Long = 0L             // toán hạng hiện tại
    private var pending: Op? = null        // phép toán chờ
    private var entering: Boolean = true   // đang nhập số cho cur

    enum class Op { ADD, SUB, MUL, DIV }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tvDisplay)

        // số
        intArrayOf(
            R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
            R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9
        ).forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                val d = (it as Button).text.toString().toInt()
                onDigit(d)
            }
        }

        // phép toán
        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOp(Op.ADD) }
        findViewById<Button>(R.id.btnSub).setOnClickListener { onOp(Op.SUB) }
        findViewById<Button>(R.id.btnMul).setOnClickListener { onOp(Op.MUL) }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { onOp(Op.DIV) }
        findViewById<Button>(R.id.btnEq).setOnClickListener { onEq() }

        // C, CE, BS
        findViewById<Button>(R.id.btnC).setOnClickListener { onClearAll() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { onClearEntry() }
        findViewById<Button>(R.id.btnBS).setOnClickListener { onBackspace() }

        updateDisplay(cur)
    }

    private fun onDigit(d: Int) {
        // giới hạn số chữ số để tránh tràn hiển thị
        if (cur.absoluteValue > 9_999_999_999L) return
        cur = cur * 10 + d
        entering = true
        updateDisplay(cur)
    }

    private fun onOp(op: Op) {
        if (entering) {
            applyPending()
            acc = cur
        } else {
            // đổi phép khi chưa nhập số mới
            pending = op
            return
        }
        pending = op
        entering = false
        cur = 0L
        updateDisplay(acc)
    }

    private fun onEq() {
        applyPending()
        pending = null
        entering = false
        updateDisplay(cur)
    }

    private fun onBackspace() {
        // BS: xóa chữ số hàng đơn vị của toán hạng hiện tại
        cur /= 10
        entering = true
        updateDisplay(cur)
    }

    private fun onClearEntry() {
        // CE: xóa giá trị toán hạng hiện tại về 0
        cur = 0L
        entering = true
        updateDisplay(cur)
    }

    private fun onClearAll() {
        // C: xóa phép toán, nhập lại từ đầu
        acc = 0L
        cur = 0L
        pending = null
        entering = true
        updateDisplay(cur)
    }

    private fun applyPending() {
        if (pending == null) return
        val a = acc
        val b = cur
        cur = when (pending) {
            Op.ADD -> a + b
            Op.SUB -> a - b
            Op.MUL -> a * b
            Op.DIV -> {
                if (b == 0L) {
                    // xử lý chia 0 (theo Windows Calculator: hiển thị lỗi)
                    tv.text = "Cannot divide by zero"
                    acc = 0L; cur = 0L; pending = null; entering = true
                    return
                } else a / b
            }
            else -> cur
        }
    }

    private fun updateDisplay(value: Long) {
        tv.text = value.toString()
    }
}