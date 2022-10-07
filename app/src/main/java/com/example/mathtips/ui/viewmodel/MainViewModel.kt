package com.example.mathtips.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mathtips.R
import com.example.mathtips.data.Calculation
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.data.EnumCalculation
import com.example.mathtips.utils.SharePrefsIplm

class MainViewModel : ViewModel() {
    private val sharePrefs = SharePrefsIplm()

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
                    listOf(R.drawable.conghangtram1,R.drawable.conghangtram2)
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
                    listOf(R.drawable.truphantram1,R.drawable.truphantram2)
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
                    listOf(R.drawable.nhan_11_1,R.drawable.nhan_11_2)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_1,
                    "Nhân các số có hai chữ số cùng chữ số hàng chục và đơn vị cộng lại bằng 10",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MULTIPLICATION_TYPE_1.name),
                    listOf(R.drawable.nhan_type_1,R.drawable.nhan_type_2)
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
                    listOf(R.drawable.binhphuong_type_1_1,R.drawable.binhphuong_type_1_2)
                ),
                CalculationChild(
                    EnumCalculation.SQUARING_TYPE_1, "bình phương các số từ 10 - 19",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.SQUARING_TYPE_1.name),
                    listOf(R.drawable.binhphuong_type_2_1,R.drawable.binhphuong_type_2_2)
                )
            )
        )
    )

    fun getTotalAnswerFromId(enum: EnumCalculation) = sharePrefs.getTotalAnswer(enum.name)
}