package com.example.mathtips.data

import com.example.mathtips.utils.SharePrefsIplm
import java.io.Serializable


data class CalculationChild(val id: EnumCalculation, val name: String, var totalAnswer : Int = 0, val listImage : List<Int>) : Serializable{
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val object1 = other as CalculationChild
        return object1.name == this.name && object1.totalAnswer == this.totalAnswer && listImage == object1.listImage
    }
}

data class Calculation(
    val color: Int,
    var symbol : Int,
    val name: String,
    var check: Boolean = false,
    var list: List<CalculationChild>
) {
    override fun hashCode(): Int {
        return color.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val object1 = other as Calculation
        return object1.symbol == this.symbol && object1.name == this.name && object1.check == this.check && object1.list == this.list
    }
}