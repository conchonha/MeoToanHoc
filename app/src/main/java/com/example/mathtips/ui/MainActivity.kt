package com.example.mathtips.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mathtips.R
import com.example.mathtips.databinding.ActivityMainBinding
import com.example.mathtips.ui.adapter.MainAdapter
import com.example.mathtips.ui.viewmodel.MainViewModel
import com.example.mathtips.utils.Constant

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding : ActivityMainBinding
    private  val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        setColor(R.color.plus)

        binding.recyclerMain.adapter = adapter.apply {
            listener = {view, item, position ->
                window.statusBarColor = ContextCompat.getColor(baseContext,item.color)
                setColor(item.color)
            }

            listenerChild = {value->
                startActivity(Intent(applicationContext,SecondActivity::class.java).apply {
                    putExtra(Constant.KEY_CALCULATION_CHILD,value.second)
                    putExtra(Constant.KEY_COLOR,value.first)
                })
            }
        }

        viewModel.mtbListCalculation.observe(this){
            adapter.updateItems(it)
        }
    }

    override fun onResume() {
        adapter.updateItems(viewModel.getList())
        super.onResume()
    }

    private fun setColor(color : Int){
        binding.linearGrop.setBackgroundColor(ContextCompat.getColor(baseContext,color))
    }
}