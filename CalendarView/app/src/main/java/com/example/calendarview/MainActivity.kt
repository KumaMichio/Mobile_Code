package com.example.calendarview

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.isGone
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var etFirst: EditText
    private lateinit var etLast: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etBirthday: EditText
    private lateinit var btnSelect: Button
    private lateinit var calendarView: CalendarView
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button

    private val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFirst = findViewById(R.id.etFirstName)
        etLast = findViewById(R.id.etLastName)
        rgGender = findViewById(R.id.rgGender)
        etBirthday = findViewById(R.id.etBirthday)
        btnSelect = findViewById(R.id.btnSelect)
        calendarView = findViewById(R.id.calendarView)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)

        // --- Toggle CalendarView ---
        btnSelect.setOnClickListener {
            calendarView.visibility =
                if (calendarView.visibility == View.GONE) View.VISIBLE else View.GONE
        }


        // --- Chọn ngày -> fill vào EditText và ẩn lịch ---
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            etBirthday.setText(fmt.format(cal.time))
            calendarView.visibility = CalendarView.GONE
        }

        // --- Register validation ---
        btnRegister.setOnClickListener { validateAndSubmit() }
    }

    private fun validateAndSubmit() {
        // Reset về nền xám mặc định
        val inputs = listOf(etFirst, etLast, etBirthday, etAddress, etEmail)
        inputs.forEach { it.setBackgroundResource(R.drawable.bg_input_gray) }

        var ok = true
        fun markError(et: EditText) {
            et.setBackgroundResource(R.drawable.bg_input_error)
            ok = false
        }

        if (etFirst.text.isNullOrBlank()) markError(etFirst)
        if (etLast.text.isNullOrBlank()) markError(etLast)
        if (etBirthday.text.isNullOrBlank()) markError(etBirthday)
        if (etAddress.text.isNullOrBlank()) markError(etAddress)
        if (etEmail.text.isNullOrBlank()) markError(etEmail)

        if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
            ok = false
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please agree to Terms of Use", Toast.LENGTH_SHORT).show()
            ok = false
        }

        if (ok) {
            Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()
        }
    }
}
