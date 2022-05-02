package com.example.computertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

/* 加減乘除等號 **/
enum class OperationType {

    Add, Subtract, Multiply, Divide, None, Percent
}

class MainActivity : AppCompatActivity() {

    lateinit var numberTextView: AppCompatTextView
    lateinit var signTextView: AppCompatTextView
    lateinit var recyclerView: RecyclerView
    var buttonAdapter: ButtonAdapter? = null

    // 放目前畫面上的數字，計算用，初始值為０
    var numberOnScreen: Double = 0.0

    // 放被覆蓋之前的數字，計算用，初始值為０
    var previousNumber: Double = 0.0

    // 紀錄狀態，判斷目前是否於計算的狀態，初始值為false
    var isCalculation = false

    // 建立物件(操作)，遵從enum > 判斷是否有點擊過計算的Item，預設為 .none
    var operation = OperationType.None

    //確認是否已點擊小數點
    var isPoint = false

    // Bool變數，是否重啟新的計算，避免上一次計算結果影響新的計算
    var newStart = true

    var numberArray = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberTextView = findViewById(R.id.numberTextView)
        signTextView = findViewById(R.id.signTextView)
        recyclerView = findViewById(R.id.recyclerView)
        buttonAdapter = ButtonAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 4)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = buttonAdapter

        /* 點擊鍵盤 */
        buttonAdapter!!.setItemSeleted(object : onPressListener {
            override fun onItemSelected(position: Int) {
                when (position) {
                    0 -> {      // 清除
                        clear()
                    }
                    1 -> {      // 去位數
                        cutNumber()
                    }
                    4, 5, 6, 8, 9, 10, 12, 13, 14, 17 -> {   // 數字
                        addNumber(position)
                    }
                    2 -> {
                        setPercent()    // 百分比
                    }
                    3 -> {
                        operation = OperationType.Divide
                        signTextView.text = "/"
                        isCalculation = true
                        previousNumber = numberOnScreen
                        numberTextView.text = "0"
                        numberArray.clear()
                    }
                    7 -> {
                        operation = OperationType.Multiply
                        signTextView.text = "x"
                        isCalculation = true
                        previousNumber = numberOnScreen
                        numberTextView.text = "0"
                        numberArray.clear()
                    }
                    11 -> {
                        operation = OperationType.Subtract
                        signTextView.text = "-"
                        isCalculation = true
                        previousNumber = numberOnScreen
                        numberTextView.text = "0"
                        numberArray.clear()
                    }
                    15 -> {
                        operation = OperationType.Add
                        signTextView.text = "+"
                        isCalculation = true
                        previousNumber = numberOnScreen
                        numberTextView.text = "0"
                        numberArray.clear()
                    }
                    18 -> {     // 小數點
                        setPoint()
                    }
                    19 -> {     // 等號
                        getAnswer()
                    }
                }
            }
        })
    }

    /* 顯示數字 */
    fun addNumber(position: Int) {
        var inputNumber = 0
        when (position) {
            4 -> inputNumber = 7
            5 -> inputNumber = 8
            6 -> inputNumber = 9
            8 -> inputNumber = 4
            9 -> inputNumber = 5
            10 -> inputNumber = 6
            12 -> inputNumber = 1
            13 -> inputNumber = 2
            14 -> inputNumber = 3
            17 -> inputNumber = 0
        }
        if (numberTextView.text.isNotBlank()) {
            if (newStart) {     // 重啟計算(計算完畢) 為 true時，下次輸入數字時重啟計算
                numberArray.clear()
                numberTextView.text = "$inputNumber"
                numberArray.add(inputNumber)
                newStart = false
            } else {            // 否則繼續當前的計算
                if (numberTextView.text == "0") {
                    // 顯示的字串為0，則輸入數字時覆蓋掉，避免計算時會計算到運算符導致錯誤
                    numberTextView.text = "$inputNumber"
                    numberArray.add(inputNumber)
                } else {
                    // 已有數字，取得該數字顯示再加上點擊的數字
                    numberTextView.text = "${numberTextView.text}$inputNumber"
                    numberArray.add(inputNumber)
                }
            }
            numberOnScreen = "${numberTextView.text}".toDouble()
        }
    }

    /* 小數點 */
    fun setPoint() {
        if (!numberTextView.text.contains(".")) {
            isPoint = true
            newStart = false
            if (numberTextView.text == "0" && numberArray.size == 0) {
                numberArray.add(0)
                numberArray.add(".")
                numberTextView.text = "${numberTextView.text}."
            } else {
                numberTextView.text = "${(numberOnScreen).toInt()}."
                numberArray.add(".")
            }
            numberOnScreen = "${numberTextView.text}".toDouble()
        }
    }

    /* 百分比 */
    fun setPercent() {
        signTextView.text = "%"
        operation = OperationType.Percent
        isCalculation = true
        previousNumber = numberOnScreen
    }

    /* 清除 > 狀態改為初始值 **/
    fun clear() {
        numberTextView.text = "0"
        signTextView.text = ""
        numberOnScreen = 0.0
        previousNumber = 0.0
        numberArray.clear()
        isCalculation = false
        isPoint = false
        operation = OperationType.None
        newStart = true
    }

    /* 顯示正確的數字字串 > 等號呼叫 **/
    // 例如：整數的話去除小數點等等
    fun makeOkNumberString(number: Double) {
        // 最後要呈現的字串
        var finalText: String
        if (floor(number) == number ) {   // 無條件進位後的數值等於 原本的數值
            finalText = "${number.toInt()}"         // 轉為Int (去除小數點)
        } else {
            // 否則不去除小數點
            finalText = number.toString()
            // 只顯示小數點後7位數
            if (finalText.length >= 9) {
                finalText = finalText.substring(IntRange(0, 8))
            }
        }
        numberTextView.text = finalText
        signTextView.text = ""
    }

    /* 等號 */
    fun getAnswer() {
        if (isCalculation) {
            when (operation) {
                OperationType.Add -> {
                    numberOnScreen = (BigDecimal(previousNumber + numberOnScreen)).setScale(7,BigDecimal.ROUND_DOWN).toDouble()
                    makeOkNumberString(numberOnScreen)
                }
                OperationType.Subtract -> {
                    numberOnScreen = (BigDecimal(previousNumber - numberOnScreen)).setScale(7,BigDecimal.ROUND_DOWN).toDouble()
                    makeOkNumberString(numberOnScreen)
                }
                OperationType.Multiply -> {
                    numberOnScreen = (BigDecimal(previousNumber * numberOnScreen)).setScale(7,BigDecimal.ROUND_DOWN).toDouble()
                    makeOkNumberString(numberOnScreen)
                }
                OperationType.Divide -> {
                    numberOnScreen = (BigDecimal(previousNumber / numberOnScreen)).setScale(7,BigDecimal.ROUND_DOWN).toDouble()
                    makeOkNumberString(numberOnScreen)
                }
                OperationType.None -> {
                    numberTextView.text = "0"
                }
                OperationType.Percent -> {
                    numberOnScreen = (BigDecimal(previousNumber) * BigDecimal(0.01)).toDouble()
                    makeOkNumberString(numberOnScreen)
                }
            }
            /// 最後再將運算狀態改為false
            isCalculation = false
            /// 重啟新的計算，避免相沖
            newStart = true
        }
    }

    /* 去位數 */
    fun cutNumber() {
        if (!newStart) {
            if (numberTextView.text != "0" && numberArray.size != 0) {
                var numString : String? = ""
                if (numberArray.size >= 2) {
                    numberArray.removeAt(numberArray.size-1)
                    for (i in 0 until numberArray.size) {
                        numString += numberArray[i]
                    }
                } else {
                    numberArray.clear()
                    numberOnScreen = 0.0
                    numString = "${numberOnScreen.toInt()}"
                }
                numberTextView.text = numString
            }
        }
    }
}