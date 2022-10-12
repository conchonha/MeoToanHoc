package com.example.mathtips.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mathtips.R
import com.example.mathtips.databinding.AnswerViewBinding
import com.example.mathtips.databinding.KeyboardViewBinding
import com.example.mathtips.databinding.TrueFalseAnswerViewBinding
import com.example.mathtips.ui.viewmodel.MainViewModel
import kotlin.random.Random

@BindingMethods(
    BindingMethod(
        type = KeyBoard::class,
        attribute = "app:setKeyBoardType",
        method = "setKeyBoardType"
    ),
    BindingMethod(
        type = KeyBoard::class,
        attribute = "app:answer",
        method = "setAnswer"
    ),
    BindingMethod(
        type = KeyBoard::class,
        attribute = "app:setKeyboardListener",
        method = "setKeyboardListener"
    ),
    BindingMethod(
        type = KeyBoard::class,
        attribute = "app:answerTruFalseView",
        method = "setAnswerTruFalseView"
    )
)
class KeyBoard(context : Context, attributes: AttributeSet) : ConstraintLayout(context,attributes) {
    private var keyBoardBinding : KeyboardViewBinding? = null
    private var answerView : AnswerViewBinding? = null
    private var trueFalseView : TrueFalseAnswerViewBinding? = null

    private val listAnswer = arrayListOf<Any>(0,0,0,0)
    private var answer : Number = 0
    private var type = KeyBoardType.TYPE_KEYBOARD
    private var keyboardListener : MainViewModel? = null
    private var answerTruFalseView : String? = null

    private var result : String = ""
        set(value) {
            field = value
            keyboardListener?.onTextChange(result)
            println("SangTB result change: $result")
        }

    fun setKeyBoardType(keyBoardType: KeyBoardType){
        println("SangTB setKeyBoardType: $keyBoardType")
        type = keyBoardType
        loadKeyBoard()
    }

    fun setKeyboardListener(keyboard : MainViewModel){
        println("SangTB setKeyboardListener: $keyboard")
        this.keyboardListener = keyboard
        keyboardListener!!.clearTextKeyboard = {
            result.removeRange(0,result.length).let {
                result = it
                Log.d("SangTB", "clearTextKeyboard: $it")
            }
        }
    }

    fun setAnswerTruFalseView(value: String){
        answerTruFalseView = value
    }

    fun setAnswer(number: Number){
        println("SangTB setAnswer: $number")
        answer = number

        if(number is Int){
            listAnswer[0] = number
            listAnswer[1] = number + Random.nextInt(20)
            listAnswer[2] = number + Random.nextInt(20)
            listAnswer[3] = number + Random.nextInt(5)
        }else{
            listAnswer[0] = number
            listAnswer[1] = number.toDouble() + Random.nextDouble(number.toDouble())
            listAnswer[2] = number.toDouble() + Random.nextDouble(number.toDouble())
            listAnswer[3] = number.toDouble() - Random.nextDouble(number.toDouble())
        }
        listAnswer.shuffle()

        if(answerView == null){
            loadKeyBoard()
        }else{
            initAnswerView()
        }
    }

    init {
        loadKeyBoard()
    }

    private fun initAnswerView(){
        answerView?.apply {
            gropIdAnswerView.referencedIds.forEachIndexed { index, i ->
                (findViewById(i) as? AppCompatButton)?.let {
                    it.text = listAnswer[index].toString()

                    it.setOnClickListener {
                        (it as? AppCompatButton)?.let {
                            if(it.text.equals(answer.toString())){
                                keyboardListener?.answerCorrect()
                                gropIdAnswerView.referencedIds.forEach {
                                    findViewById<AppCompatButton>(it).isEnabled = true
                                }
                            }else{
                                it.isEnabled = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadKeyBoard() {
        removeAllViews()
        when(type){
            KeyBoardType.TYPE_KEYBOARD ->
                KeyboardViewBinding.inflate(LayoutInflater.from(context),this,true).also {
                    keyBoardBinding = it
                    handleKeyboard()
                }
            KeyBoardType.TYPE_ANSWER ->
                AnswerViewBinding.inflate(LayoutInflater.from(context),this,true).also {
                    answerView = it
                    initAnswerView()
                }
            else -> TrueFalseAnswerViewBinding.inflate(LayoutInflater.from(context),this,true).also {
                trueFalseView = it
                initTrueFalseView()
            }
        }
    }

    private fun initTrueFalseView() {
        trueFalseView!!.apply {
            groupId.referencedIds.forEach {
               btnAnswer1.setOnClickListener {
                   if (answerTruFalseView != "$answer"){
                       keyboardListener?.answerCorrect()
                       btnAnswer1.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                       btnAnswer2.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                   }else{
                       it.setBackgroundColor(ContextCompat.getColor(context,R.color.white_enable))
                   }
               }

                btnAnswer2.setOnClickListener {
                    if(answerTruFalseView == "$answer"){
                        keyboardListener?.answerCorrect()
                        btnAnswer1.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                        btnAnswer2.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                    }else{
                        it.setBackgroundColor(ContextCompat.getColor(context,R.color.white_enable))
                    }
                }
            }
        }
    }

    private fun handleKeyboard() {
        keyBoardBinding!!.apply {
            groupBtnNumNext.referencedIds.forEach {
                (findViewById(it) as? AppCompatButton)?.setOnClickListener {
                    (it as? AppCompatButton)?.let { button->
                        result += button.text.toString()
                    }
                }
            }

            btnBack.setOnClickListener {
                if(result.isNotEmpty())
                result = result.dropLast(1)
            }
        }
    }
}

enum class KeyBoardType{
    TYPE_KEYBOARD,
    TYPE_ANSWER,
    TYPE_TRUE_FALSE
}

interface KeyboardListener{
    var clearTextKeyboard : (()->Unit)?
    val answer : MutableLiveData<Number>
    val keyBoardType : MutableLiveData<KeyBoardType>
    val textChange : MutableLiveData<String>

    fun onTextChange(value : String){
        Log.d("SangTB", "onTextChange: $value")
    }

    fun answerCorrect()
}