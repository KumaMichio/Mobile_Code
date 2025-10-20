package com.example.demo_bt_android

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_bt_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Sử dụng ViewBinding để dễ dàng thao tác với các phần tử UI
    private lateinit var binding: ActivityMainBinding
    private var currentInput: String = "0"
    private var previousInput: String = ""
    private var operator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout XML và set nó vào Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ánh xạ các button và TextView trong layout
        val display: TextView = binding.display

        // Cài đặt sự kiện cho các button
        binding.button1.setOnClickListener { appendNumber("1", display) }
        binding.button2.setOnClickListener { appendNumber("2", display) }
        binding.button3.setOnClickListener { appendNumber("3", display) }
        binding.button4.setOnClickListener { appendNumber("4", display) }
        binding.button5.setOnClickListener { appendNumber("5", display) }
        binding.button6.setOnClickListener { appendNumber("6", display) }
        binding.button7.setOnClickListener { appendNumber("7", display) }
        binding.button8.setOnClickListener { appendNumber("8", display) }
        binding.button9.setOnClickListener { appendNumber("9", display) }
        binding.button0.setOnClickListener { appendNumber("0", display) }

        binding.buttonCE.setOnClickListener { clearInput(display) }
        binding.buttonC.setOnClickListener { clearAll(display) }
        binding.buttonBS.setOnClickListener { backspace(display) }

        binding.buttonPlus.setOnClickListener { setOperator("+", display) }
        binding.buttonMinus.setOnClickListener { setOperator("-", display) }
        binding.buttonMultiply.setOnClickListener { setOperator("x", display) }
        binding.buttonDivide.setOnClickListener { setOperator("/", display) }

        binding.buttonEqual.setOnClickListener { calculateResult(display) }
        binding.buttonPlusMinus.setOnClickListener { toggleSign(display) }
    }

    // Hàm để thêm số vào chuỗi hiển thị
    private fun appendNumber(number: String, display: TextView) {
        if (currentInput == "0") {
            currentInput = number
        } else {
            currentInput += number
        }
        display.text = currentInput
    }

    // Hàm để thiết lập toán tử (+, -, x, /)
    private fun setOperator(op: String, display: TextView) {
        if (operator != null) {
            calculateResult(display)
        }
        previousInput = currentInput
        operator = op
        currentInput = "0"
    }

    // Hàm để tính toán kết quả
    private fun calculateResult(display: TextView) {
        val firstNumber = previousInput.toDoubleOrNull()
        val secondNumber = currentInput.toDoubleOrNull()

        if (firstNumber != null && secondNumber != null && operator != null) {
            val result = when (operator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "x" -> firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else "Error"
                else -> null
            }
            currentInput = result.toString()
            display.text = currentInput
            operator = null
            previousInput = ""
        }
    }

    // Hàm để xóa một ký tự
    private fun backspace(display: TextView) {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) {
                currentInput = "0"
            }
            display.text = currentInput
        }
    }

    // Hàm để xóa tất cả
    private fun clearAll(display: TextView) {
        currentInput = "0"
        previousInput = ""
        operator = null
        display.text = currentInput
    }

    // Hàm để xóa tất cả và làm mới
    private fun clearInput(display: TextView) {
        currentInput = "0"
        display.text = currentInput
    }

    // Hàm để thay đổi dấu của số
    private fun toggleSign(display: TextView) {
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.removePrefix("-")
        } else {
            currentInput = "-$currentInput"
        }
        display.text = currentInput
    }
}
