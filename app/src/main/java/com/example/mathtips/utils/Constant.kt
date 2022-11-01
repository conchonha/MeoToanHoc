package com.example.mathtips.utils

import com.example.mathtips.R
import com.example.mathtips.data.Calculation
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.data.EnumCalculation

object Constant {
    const val KEY_CALCULATION_CHILD = "KEY_CALCULATION_CHILD"
    const val KEY_COLOR = "KEY_COLOR"
    const val KEY_LEVEL = "keyLevel"
    const val EASY = "easy"
    const val MEDIUM = "medium"
    const val DIFFICULTY_LEVEL = "difficultylevel"

    const val KEY_RATE = "KEY_SCORE"
    const val KEY_TOTAL_ANSWER = "KEY_SCORE"

    val list = mutableListOf(
        Calculation(
            color = R.color.plus,
            R.drawable.plus,
            "Phép Cộng",
            list = listOf(
                CalculationChild(
                    id = EnumCalculation.PLUSH,
                    name = "Cộng thuộc lòng",
                    listImage = listOf(R.drawable.congthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.PLUSH_TYPE_1,
                    "Cộng Các Số Gần Hàng Trăm",
                    listImage = listOf(R.drawable.conghangtram1, R.drawable.conghangtram2)
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
                    listImage = listOf(R.drawable.truthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.MINUS_TYPE_1, "Trừ Các Số Gần Hàng Trăm",
                    listImage = listOf(R.drawable.truphantram1, R.drawable.truphantram2)
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
                    listImage = listOf(R.drawable.nhan_11_1, R.drawable.nhan_11_2)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_1,
                    "Nhân các số có hai chữ số cùng chữ số hàng chục và đơn vị cộng lại bằng 10",
                    listImage = listOf(R.drawable.nhan_type_1, R.drawable.nhan_type_2)
                ),
            )
        ),
        Calculation(
            color = R.color.purple_500,
            R.drawable.ic_bang_cuu_chuong,
            "Bảng Cửu Chương",
            list = listOf(
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_2,
                    "Bảng Cửu Chương 2",
                    listImage = listOf(R.drawable.bang_cuu_chuong_2)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_3,
                    "Bảng Cửu Chương 3",
                    listImage = listOf(R.drawable.bang_cuu_chuong_3)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_4,
                    "Bảng Cửu Chương 4",
                    listImage = listOf(R.drawable.bang_cuu_chuong_4)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_5,
                    "Bảng Cửu Chương 5",
                    listImage = listOf(R.drawable.bang_cuu_chuong_5)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_6,
                    "Bảng Cửu Chương 6",
                    listImage = listOf(R.drawable.bang_cuu_chuong_6)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_7,
                    "Bảng Cửu Chương 7",
                    listImage = listOf(R.drawable.bang_cuu_chuong_7)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_8,
                    "Bảng Cửu Chương 8",
                    listImage = listOf(R.drawable.bang_cuu_chuong_8)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_9,
                    "Bảng Cửu Chương 9",
                    listImage = listOf(R.drawable.bang_cuu_chuong_9)
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
                    listImage = listOf(R.drawable.binhphuong_type_1_1, R.drawable.binhphuong_type_1_2)
                ),
                CalculationChild(
                    EnumCalculation.SQUARING_TYPE_1, "bình phương các số từ 10 - 19",
                    listImage = listOf(R.drawable.binhphuong_type_2_1, R.drawable.binhphuong_type_2_2)
                )
            )
        )
    )
    val lisSquaring = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
    val lisMinusPlushType2Level1 = listOf(100,200,300)
    val lisMinusPlushType2Level2 = listOf(300,400,500)
    val lisMinusPlushType2Level3 = listOf(600,700,800)
}