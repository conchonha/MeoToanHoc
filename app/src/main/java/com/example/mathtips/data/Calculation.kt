package com.example.mathtips.data

import java.io.Serializable


data class CalculationChild(val id: EnumCalculation, val name: String,val totalAnswer : Int = 0,val listImage : List<Int>) : Serializable{}

data class Calculation(
    val color: Int,
    var symbol : Int,
    val name: String,
    var check: Boolean = false,
    var list: List<CalculationChild>
) {}