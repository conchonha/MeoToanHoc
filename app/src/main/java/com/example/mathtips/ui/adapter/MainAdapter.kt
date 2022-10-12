package com.example.mathtips.ui.adapter

import android.util.Log
import com.example.mathtips.R
import com.example.mathtips.data.Calculation
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.databinding.ItemCaculationBinding
import com.example.mathtips.databinding.ItemCaculationChildBinding
import com.example.mathtips.utils.SharePrefsIplm
import com.sangtb.androidlibrary.base.BaseRecyclerViewAdapter

class MainAdapter : BaseRecyclerViewAdapter<Calculation, ItemCaculationBinding>() {
    override val layoutId: Int
        get() = R.layout.item_caculation

    var listenerChild: ((Pair<Int,CalculationChild>)->Unit)? = null

    override fun onBindViewHolder(holder: BaseViewHolder<ItemCaculationBinding>, position: Int) {
        val calculation = items[position]
        holder.binding.calculation = calculation

        holder.binding.apply {
            root.setOnClickListener {
                calculation.check = !calculation.check
                notifyItemChanged(position)
                listener?.invoke(it,calculation,position)
            }

            recyclerItemCalculation.adapter = ItemChildAdapter(calculation.color).apply {
                updateItems(calculation.list as MutableList<CalculationChild>)
            }
        }
    }

    inner class ItemChildAdapter(private val color1: Int) :
        BaseRecyclerViewAdapter<CalculationChild, ItemCaculationChildBinding>() {
        override val layoutId: Int
            get() = R.layout.item_caculation_child

        override fun onBindViewHolder(
            holder: BaseViewHolder<ItemCaculationChildBinding>,
            position: Int
        ) {
            val calculationChild = items[position]
            holder.binding.apply {
                index = "0${position+ 1}."
                color = color1
                calculation = calculationChild

                txtTitle.setOnClickListener {
                    listenerChild?.invoke(Pair(color1,calculationChild))
                }
            }
        }
    }
}