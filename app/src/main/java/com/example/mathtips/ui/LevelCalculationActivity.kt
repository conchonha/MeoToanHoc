package com.example.mathtips.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mathtips.R
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.databinding.ActivityLevelCalculationBinding
import com.example.mathtips.ui.viewmodel.MainViewModel
import com.example.mathtips.utils.Constant
import kotlin.properties.Delegates

class LevelCalculationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLevelCalculationBinding
    private var  color1 by Delegates.notNull<Int>()
    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_level_calculation)

        val data = intent.getSerializableExtra(Constant.KEY_CALCULATION_CHILD) as CalculationChild
        color1 = intent.getIntExtra(Constant.KEY_COLOR,R.color.plus)
        window.statusBarColor = ContextCompat.getColor(baseContext,color1)

        binding.apply {
            color = color1
            itemLevelCalculation1.cardBg.setOnClickListener {
               sendIntent(Constant.EASY)
            }

            itemLevelCalculation2.cardBg.setOnClickListener {
                sendIntent(Constant.MEDIUM)
            }

            itemLevelCalculation3.cardBg.setOnClickListener {
                sendIntent(Constant.DIFFICULTY_LEVEL)
            }
        }
    }

    fun backScreen(v : View){
        finish()
    }

    private fun sendIntent(level : String){
        startActivity(Intent(this@LevelCalculationActivity,CalculationActivity::class.java).apply {
            putExtra(Constant.KEY_CALCULATION_CHILD,data)
            putExtra(Constant.KEY_COLOR,color1)
            putExtra(Constant.KEY_LEVEL,level)
        })
    }

}