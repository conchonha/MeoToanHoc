package com.example.mathtips.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathtips.R
import com.example.mathtips.data.Calculation
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.data.EnumCalculation
import com.example.mathtips.utils.KeyBoardType
import com.example.mathtips.utils.KeyboardListener
import com.example.mathtips.utils.SharePrefsIplm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), KeyboardListener {
    private val sharePrefs = SharePrefsIplm()
    private val _textChange = MutableLiveData<String>()
    private val _content = MutableLiveData<String>()
    val progress = MutableLiveData(100)
    private lateinit var _calculationChild: CalculationChild

    init {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 100 downTo 0){
                delay(100)
                progress.postValue(i)
            }
        }
    }

    val list = mutableListOf(
        Calculation(
            color = R.color.plus,
            R.drawable.plus,
            "Phép Cộng",
            list = listOf(
                CalculationChild(
                    EnumCalculation.PLUSH, "Cộng thuộc lòng",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.PLUSH.name),
                    listOf(R.drawable.congthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.PLUSH_TYPE_1,
                    "Cộng Các Số Gần Hàng Trăm",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.PLUSH_TYPE_1.name),
                    listOf(R.drawable.conghangtram1, R.drawable.conghangtram2)
                ),
            )
        ),
        Calculation(
            color = R.color.minus,
            R.drawable.minus,
            "Phép Trừ",
            list = listOf(
                CalculationChild(
                    EnumCalculation.MINUS, "Trừ thuộc lòng",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MINUS.name),
                    listOf(R.drawable.truthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.MINUS_TYPE_1, "Trừ Các Số Gần Hàng Trăm",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MINUS_TYPE_1.name),
                    listOf(R.drawable.truphantram1, R.drawable.truphantram2)
                )
            )
        ),
        Calculation(
            color = R.color.multiplication,
            R.drawable.nhan,
            "Phép Nhân",
            list = listOf(
                CalculationChild(
                    EnumCalculation.MULTIPLICATION, "Nhân số có 2 chữ số với 11",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MULTIPLICATION.name),
                    listOf(R.drawable.nhan_11_1, R.drawable.nhan_11_2)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_1,
                    "Nhân các số có hai chữ số cùng chữ số hàng chục và đơn vị cộng lại bằng 10",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MULTIPLICATION_TYPE_1.name),
                    listOf(R.drawable.nhan_type_1, R.drawable.nhan_type_2)
                )
            )
        ),
        Calculation(
            color = R.color.squaring,
            R.drawable.ic_squaring,
            "Phép Bình Phương",
            list = listOf(
                CalculationChild(
                    EnumCalculation.SQUARING, "bình phương các số kết thúc bằng 5",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.SQUARING.name),
                    listOf(R.drawable.binhphuong_type_1_1, R.drawable.binhphuong_type_1_2)
                ),
                CalculationChild(
                    EnumCalculation.SQUARING_TYPE_1, "bình phương các số từ 10 - 19",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.SQUARING_TYPE_1.name),
                    listOf(R.drawable.binhphuong_type_2_1, R.drawable.binhphuong_type_2_2)
                )
            )
        )
    )

    fun getTotalAnswerFromId(enum: EnumCalculation) = sharePrefs.getTotalAnswer(enum.name)

    fun setCalculationChild(value: CalculationChild) {
        _calculationChild = value
        val value = when (_calculationChild.id) {
            EnumCalculation.PLUSH,
            EnumCalculation.PLUSH_TYPE_1 -> "Phép Cộng"
            EnumCalculation.MINUS,
            EnumCalculation.MINUS_TYPE_1 -> "Phép trừ"
            EnumCalculation.MULTIPLICATION,
            EnumCalculation.MULTIPLICATION_TYPE_1->"Phép Nhân"
            else -> "Bình phương"
        }
        _content.postValue(value)
    }


    override val answer: LiveData<Number> = MutableLiveData(30)
    override val keyBoardType: LiveData<KeyBoardType> = MutableLiveData(KeyBoardType.TYPE_KEYBOARD)
    override val textChange: LiveData<String> = _textChange

    override fun onTextChange(value: String) {
        super.onTextChange(value)
        _textChange.postValue(value)
    }

    override fun answerCorrect() {
    }
}