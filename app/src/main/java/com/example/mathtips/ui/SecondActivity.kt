package com.example.mathtips.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mathtips.R
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.databinding.ActivitySecondBinding
import com.example.mathtips.utils.Constant

class SecondActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)

        val data = intent.getSerializableExtra(Constant.KEY_CALCULATION_CHILD) as CalculationChild
        val color1 = intent.getIntExtra(Constant.KEY_COLOR,R.color.plus)
        window.statusBarColor = ContextCompat.getColor(baseContext,color1)

        binding.apply {
            color = color1
            calculationChild = data

            data.listImage.forEach { element->
                linearGrop.addView(ImageView(baseContext).apply {
                    layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1200)
                    setBackgroundResource(element)
                })
            }

            nextPage.setOnClickListener {
                startActivity(Intent(this@SecondActivity,LevelCalculationActivity::class.java).apply {
                    putExtra(Constant.KEY_CALCULATION_CHILD,data)
                    putExtra(Constant.KEY_COLOR,color1)
                })
            }
        }

    }

    fun backScreen(v : View){
        finish()
    }
}