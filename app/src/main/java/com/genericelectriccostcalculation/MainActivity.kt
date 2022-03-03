package com.genericelectriccostcalculation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.genericelectriccostcalculation.databinding.MainActivityBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var binding: MainActivityBinding? = null
    lateinit var database: ReadingDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        database = Room.databaseBuilder(applicationContext, ReadingDatabase::class.java, "readingDb").build()

        binding?.btnCalculate?.setOnClickListener(View.OnClickListener {
            val unitsConsumed = binding?.edtUnits?.text
            val serviceNumber = binding?.edtServiceNumber?.text.toString()
            val priceOne = binding?.edtPriceOne?.text.toString()
            val priceTwo = binding?.edtPriceTwo?.text.toString()
            val priceThree = binding?.edtPriceThree?.text.toString()

            if (!unitsConsumed?.isEmpty()!! && !serviceNumber.isEmpty() && !priceOne.isEmpty() && !priceTwo.isEmpty() && !priceThree.isEmpty()) {
                if (unitsConsumed.toString().toInt() == 250 || unitsConsumed.toString().toInt() == 630) {
                    calculateSpecialCost(unitsConsumed.toString().toInt(), serviceNumber, priceOne.toLong(), priceTwo.toLong(), priceThree.toLong())
                } else {
                    calculateCost(unitsConsumed.toString().toInt(), serviceNumber, priceOne.toLong(), priceTwo.toLong(), priceThree.toLong())
                }
            }
        })
    }

    fun saveReading(serviceNumber: String, date: String, billAmount: String, unitsConsumed: String) {
        GlobalScope.launch {
            if (!database.readingDao().checkExist(serviceNumber)) {
                database.readingDao()
                    .insertReading(Reading(0, serviceNumber, date, billAmount, unitsConsumed))
                showToast("Record Added")
            } else {
                showToast("Service Number Already Exist")
            }
        }
    }

    fun showToast(msg: String) {
        Handler(Looper.getMainLooper()).post({
            Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show()
        })
    }

    fun calculateCost(unitsConsumed: Int, serviceNumber: String, p1: Long, p2: Long, p3: Long) {
        var result: Long = 0
        if (unitsConsumed >= 1 && unitsConsumed <= 100) {
            result = unitsConsumed * p1
            binding?.tvResult?.text = result.toString()
        } else if (unitsConsumed >= 101 && unitsConsumed <= 500) {
            result = unitsConsumed * p2
            binding?.tvResult?.text = result.toString()
        } else {
            result = unitsConsumed * p3
            binding?.tvResult?.text = result.toString()
        }
        saveReading(serviceNumber, getCompleteDate().toString(), result.toString(), unitsConsumed.toString())
    }

    fun calculateSpecialCost(unitsConsumed: Int, serviceNumber: String, p1: Long, p2: Long, p3: Long) {
        var result: Long = 0
        if (unitsConsumed == 250) {
            val reading1 = (250 - 150) * p1 // 100
            val reading2 = (250 - 100) * p2 // 150
            result = (reading1 + reading2)
            binding?.tvResult?.text = result.toString()
        } else if (unitsConsumed == 630) {
            val reading1 = (630 - 530) * p1 // 100
            val reading2 = (630 - 230) * p2 // 400
            val reading3 = (630 - 500) * p3 // 130
            result = (reading1 + reading2 + reading3)
            binding?.tvResult?.text = result.toString()
        }
        saveReading(serviceNumber, getCompleteDate().toString(), result.toString(), unitsConsumed.toString())
    }

    fun getCompleteDate(): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

}