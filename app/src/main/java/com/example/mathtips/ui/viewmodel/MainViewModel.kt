package com.example.mathtips.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathtips.R
import com.example.mathtips.data.Calculation
import com.example.mathtips.data.CalculationChild
import com.example.mathtips.data.EnumCalculation
import com.example.mathtips.utils.Constant
import com.example.mathtips.utils.KeyBoardType
import com.example.mathtips.utils.KeyboardListener
import com.example.mathtips.utils.SharePrefsIplm
import com.sangtb.androidlibrary.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import kotlin.random.Random

class MainViewModel : ViewModel(), KeyboardListener {
    private val sharePrefs = SharePrefsIplm()
    val content = MutableLiveData<String>()
    val progress = MutableLiveData(100)
    val rateCount = MutableLiveData(3)
    val totalAnswer = MutableLiveData(0)
    val score = MutableLiveData(0)
    val typeSquaring = MutableLiveData(false)
    val levelCalculation = MutableLiveData("")
    val mtbListCalculation = MutableLiveData< MutableList<Calculation>>()
    val showDialog = SingleLiveEvent<Boolean>()

    private var level: String = ""
    private var indexTypeKeyBoard = 0
    private lateinit var _calculationChild: CalculationChild
    var timeRepeat: Long = 100
    var job: Job? = null
    var answerKQ: Int = 0
    var color by Delegates.notNull<Int>()

    val symbolCalculation = MutableLiveData<String>()
    private val lisSquaring = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
    val a = MutableLiveData<Int?>()
    val b = MutableLiveData<Int?>()


    fun onDropDow() {
        val listTmp = getList().map {
            it.apply {
                check = true
            }
        }.toMutableList()

        mtbListCalculation.postValue(listTmp)
    }

   fun onDropUp(){
       val listTmp = getList().map {
           it.apply {
               check = false
           }
       }.toMutableList()

       mtbListCalculation.postValue(listTmp)
   }

    fun getList() =  mutableListOf(
        Calculation(
            color = R.color.plus,
            R.drawable.plus,
            "Phép Cộng",
            list = listOf(
                CalculationChild(
                    EnumCalculation.PLUSH, "Cộng thuộc lòng",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.PLUSH.name),
                    listOf(R.drawable.congthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.PLUSH_TYPE_1,
                    "Cộng Các Số Gần Hàng Trăm",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.PLUSH_TYPE_1.name),
                    listOf(R.drawable.conghangtram1, R.drawable.conghangtram2)
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
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MINUS.name),
                    listOf(R.drawable.truthuoclong)
                ),
                CalculationChild(
                    EnumCalculation.MINUS_TYPE_1, "Trừ Các Số Gần Hàng Trăm",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MINUS_TYPE_1.name),
                    listOf(R.drawable.truphantram1, R.drawable.truphantram2)
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
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MULTIPLICATION.name),
                    listOf(R.drawable.nhan_11_1, R.drawable.nhan_11_2)
                ),
                CalculationChild(
                    EnumCalculation.MULTIPLICATION_TYPE_1,
                    "Nhân các số có hai chữ số cùng chữ số hàng chục và đơn vị cộng lại bằng 10",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.MULTIPLICATION_TYPE_1.name),
                    listOf(R.drawable.nhan_type_1, R.drawable.nhan_type_2)
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
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.SQUARING.name),
                    listOf(R.drawable.binhphuong_type_1_1, R.drawable.binhphuong_type_1_2)
                ),
                CalculationChild(
                    EnumCalculation.SQUARING_TYPE_1, "bình phương các số từ 10 - 19",
                    totalAnswer = sharePrefs.getTotalAnswer(EnumCalculation.SQUARING_TYPE_1.name),
                    listOf(R.drawable.binhphuong_type_2_1, R.drawable.binhphuong_type_2_2)
                )
            )
        )
    )

    fun getSoreFromIdAndLevel(calculationChild: CalculationChild, level: String) =
        sharePrefs.getTotalScoreFromIdAndLevel("${calculationChild.id.name}$level")

    fun getRateFromIdAndLevel(calculationChild: CalculationChild, level: String) =
        sharePrefs.getTotalScoreFromIdAndLevel("${calculationChild.id.name}$level${Constant.KEY_RATE}")

    private fun startProgressbar() {
        job = CoroutineScope(Dispatchers.IO).launch {
            for (i in 100 downTo 0) {
                delay(timeRepeat)
                progress.postValue(i)
                if (i == 0) {
                    if (rateCount.value!! > 0) {
                        rateCount.postValue(rateCount.value!! - 1)
                    }
                }
            }
        }
    }

    fun setTypeKeyBoard() {
        if (indexTypeKeyBoard == 3) {
            indexTypeKeyBoard = 0
        }
        Log.d("SangTB", "setTypeKeyBoard: $indexTypeKeyBoard")
        val type = when (indexTypeKeyBoard) {
            0 -> KeyBoardType.TYPE_KEYBOARD
            1 -> KeyBoardType.TYPE_ANSWER
            else -> KeyBoardType.TYPE_TRUE_FALSE
        }
        keyBoardType.value = type
        if (keyBoardType.value == KeyBoardType.TYPE_TRUE_FALSE) {
            textChangeTypeTrueFalse()
        }
        indexTypeKeyBoard++
    }

    private fun textChangeTypeTrueFalse(){
        listOf(answerKQ, Random.nextInt(answerKQ, answerKQ + 19)).let {
            it.shuffled()
            textChange.value =  it.shuffled().random().toString()
        }
    }

    fun setCalculationChild(value: CalculationChild, level1: String, color1: Int) {
        _calculationChild = value
        level = level1
        color = color1
        val value = when (level) {
            Constant.EASY -> "Dễ"
            Constant.MEDIUM -> "Trung bình"
            else -> "Khó"
        }
        levelCalculation.postValue(value)
        checkType()
    }

    private fun checkType() {
        startProgressbar()
        when (_calculationChild.id) {
            EnumCalculation.PLUSH -> {
                content.postValue("Phép Cộng")
                symbolCalculation.postValue("+")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 150
                        a.value = Random.nextInt(1, 20)
                        b.value = Random.nextInt(1, 20)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 100
                        a.value = Random.nextInt(1, 50)
                        b.value = Random.nextInt(1, 50)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 50
                        a.value = Random.nextInt(1, 100)
                        b.value = Random.nextInt(1, 100)
                    }
                }
                answerKQ = a.value!! + b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.PLUSH_TYPE_1 -> {
                content.postValue("Phép Cộng")
                symbolCalculation.postValue("+")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 100
                        a.value = Random.nextInt(100)
                        b.value = Random.nextInt(100)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        a.value = Random.nextInt(200)
                        b.value = Random.nextInt(200)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        a.value = Random.nextInt(300)
                        b.value = Random.nextInt(300)
                    }
                }
                answerKQ = a.value!! + b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.MINUS -> {
                content.postValue("Phép trừ")
                symbolCalculation.postValue("-")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 100
                        a.value = Random.nextInt(20, 30)
                        b.value = Random.nextInt(20)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        a.value = Random.nextInt(50, 60)
                        b.value = Random.nextInt(50)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        a.value = Random.nextInt(100, 200)
                        b.value = Random.nextInt(100)
                    }
                }
                answerKQ = a.value!! - b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.MINUS_TYPE_1 -> {
                content.postValue("Phép trừ")
                symbolCalculation.postValue("-")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 150
                        a.value = Random.nextInt(100, 150)
                        b.value = Random.nextInt(100)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 100
                        a.value = Random.nextInt(200, 250)
                        b.value = Random.nextInt(200)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 50
                        a.value = Random.nextInt(300, 350)
                        b.value = Random.nextInt(300)
                    }
                }
                answerKQ = a.value!! - b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.MULTIPLICATION -> {
                content.postValue("Phép Nhân")
                symbolCalculation.postValue("x")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 100
                        a.value = Random.nextInt(20)
                        b.value = Random.nextInt(11)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        a.value = Random.nextInt(50)
                        b.value = Random.nextInt(11)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        a.value = Random.nextInt(100)
                        b.value = Random.nextInt(11)
                    }
                }
                answerKQ = a.value!! * b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.MULTIPLICATION_TYPE_1 -> {
                content.postValue("Phép Nhân")
                symbolCalculation.postValue("x")
                when (level) {
                    Constant.EASY -> {
                        timeRepeat = 100
                        val value = getValueAB(10, 30)
                        a.value = value.first
                        b.value = value.second
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        val value = getValueAB(10, 50)
                        a.value = value.first
                        b.value = value.second
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        val value = getValueAB(10, 100)
                        a.value = value.first
                        b.value = value.second
                    }
                }
                answerKQ = a.value!! * b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.SQUARING -> {
                symbolCalculation.postValue("^2")
                typeSquaring.postValue(true)
                setTimeRepeat()
                a.value =  lisSquaring.shuffled().random()
                answerKQ = a.value!! * a.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            else -> {
                typeSquaring.postValue(true)
                setTimeRepeat()
                a.value = Random.nextInt(10, 19)
                answerKQ = a.value!! * a.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
        }
    }

    private fun setTimeRepeat() {
        when (level) {
            Constant.EASY -> {
                timeRepeat = 100
            }
            Constant.MEDIUM -> {
                timeRepeat = 70
            }
            Constant.DIFFICULTY_LEVEL -> {
                timeRepeat = 40
            }
        }
    }

    private fun finishQuestionAndAnswer() {
        sharePrefs.put(_calculationChild.id.name, totalAnswer.value!!)
        sharePrefs.put("${_calculationChild.id.name}$level", score.value!!)
        sharePrefs.put(
            "${_calculationChild.id.name}$level${Constant.KEY_RATE}",
            rateCount.value ?: 0
        )
        showDialog.postValue(true)
    }

    private fun getValueAB(from: Int, util: Int): Pair<Int, Int> {
        while (true) {
            var a = Random.nextInt(from, util)
            var b = Random.nextInt(from, util)

            var c = a.toString().substring(1, 2).toInt()
            var d = b.toString().substring(1, 2).toInt()

            if (c + d == 10) {
                return Pair(a, b)
            }
        }
    }

    override val answer: MutableLiveData<Number> = MutableLiveData(5)
    override val keyBoardType: MutableLiveData<KeyBoardType> =
        MutableLiveData(KeyBoardType.TYPE_KEYBOARD)
    override val textChange: MutableLiveData<String> = MutableLiveData("?")

    override fun onTextChange(value: String) {
        super.onTextChange(value)
        if (value.isEmpty()) {
            textChange.postValue("?")
        } else {
            textChange.postValue(value)
        }

        if (value == answerKQ.toString()) {
            if (totalAnswer.value == 10) {
                job?.cancel()
                finishQuestionAndAnswer()
                return
            }
            job?.cancel()
            checkType()
            clearTextKeyboard?.invoke()
            totalAnswer.postValue(totalAnswer.value!! + 1)
            score.postValue(score.value!! + progress.value!!)
        }
    }

    override fun answerCorrect() {
        Log.d("SangTB", "answerCorrect: ")
        if (totalAnswer.value == 10) {
            job?.cancel()
            finishQuestionAndAnswer()
            return
        }
        job?.cancel()
        checkType()
        totalAnswer.postValue(totalAnswer.value!! + 1)
        score.postValue(score.value!! + progress.value!!)
    }

    override var clearTextKeyboard: (() -> Unit)? = null
}