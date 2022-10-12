package com.example.mathtips.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.mathtips.R

@BindingAdapter(value = ["index","rate"], requireAll =true)
fun setUrlImage(imageView: ImageView, index : Int,rate : Int) {
    if(index <= rate){
        imageView.setImageResource(R.drawable.ic_rating)
    }else{
        imageView.setImageResource(R.drawable.ic_rating_normal)
    }
}