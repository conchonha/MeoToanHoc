package com.example.mathtips.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mathtips.R
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.databinding.ActivityCalculationBinding
import com.example.mathtips.ui.dialog.DialogQuestionAnswerFinish
import com.example.mathtips.ui.viewmodel.MainViewModel
import com.example.mathtips.utils.Constant

class CalculationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCalculationBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculation)

        val data = intent.getSerializableExtra(Constant.KEY_CALCULATION_CHILD) as CalculationChild
        val level = intent.getStringExtra(Constant.KEY_LEVEL)
        val color1 = intent.getIntExtra(Constant.KEY_COLOR,R.color.plus)

        if (level != null) {
            viewModel.setCalculationChild(data,level,color1)
        }

        window.statusBarColor = ContextCompat.getColor(baseContext,color1)

        binding.apply {
            viewModel = this@CalculationActivity.viewModel
            lifecycleOwner = this@CalculationActivity
            color = color1
        }

        viewModel.showDialog.observe(this){
            if(it){
                DialogQuestionAnswerFinish().show(supportFragmentManager,"DialogQuestionAnswerFinish")
            }
        }
    }

    fun backScreen(v : View){
        finish()
    }
}