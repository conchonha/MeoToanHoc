package com.example.mathtips.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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

class MainViewModel(application: Application) : AndroidViewModel(application), KeyboardListener {
    private val sharePrefs = SharePrefsIplm()
    private var level: String = ""

    val content = MutableLiveData<String>()
    val progress = MutableLiveData(100)
    val rateCount = MutableLiveData(3)
    val totalAnswer = MutableLiveData(0)
    val score = MutableLiveData(0)
    val typeSquaring = MutableLiveData(false)
    val levelCalculation = MutableLiveData("")
    val mtbListCalculation = MutableLiveData<MutableList<Calculation>>()
    val showDialog = SingleLiveEvent<Boolean>()

    private var indexTypeKeyBoard = 0
    private lateinit var _calculationChild: CalculationChild
    var timeRepeat: Long = 100

    var job: Job? = null
    var answerKQ: Int = 0
    var color by Delegates.notNull<Int>()

    val symbolCalculation = MutableLiveData<String>()
    val a = MutableLiveData<Int?>()
    val b = MutableLiveData<Int?>()

    //function chỉ được gọi ở màn hình chính (MainActivity) Open ListItem(Cộng/Trừ/Nhân/Chia)
    fun onDropDow() {
        val listTmp = getList().map {
            it.apply {
                check = true
            }
        }.toMutableList()

        mtbListCalculation.postValue(listTmp)
    }

    //function chỉ được gọi ở màn hình chính (MainActivity) drop ListItem(Cộng/Trừ/Nhân/Chia)
   fun onDropUp(){
       val listTmp = getList().map {
           it.apply {
               check = false
           }
       }.toMutableList()

       mtbListCalculation.postValue(listTmp)
   }

    //Chương trình chạy chủ yếu được quản lý và handle bởi List này dựa vào các constant define cho mỗi level (data display dc defined ở mục model)
    fun getList() = Constant.list.map {
        it.list.map { element->
            element.totalAnswer = sharePrefs.getTotalAnswer(element.id.name)
            element
        }
        it
    }.toMutableList()

    //Mỗi Chắc năng có 3 level (Khó dễ trungbinhg)/ func này dùng để lấy điểm của mỗi chức năng dựa vào
    // key : EnumCalculation và value là số điểm người dùng chơi được biến: score
    fun getSoreFromIdAndLevel(calculationChild: CalculationChild, level: String) =
        sharePrefs.getTotalScoreFromIdAndLevel("${calculationChild.id.name}$level")

    //Mỗi Chắc năng có 3 level (Khó - dễ - trungbinh)/ func này dùng để lấy số sao của mỗi chức năng dựa vào
    // key : EnumCalculation + Constant.KEY_RATE và value là số sao người dùng chơi được biến: rateCount
    fun getRateFromIdAndLevel(calculationChild: CalculationChild, level: String) =
        sharePrefs.getTotalScoreFromIdAndLevel("${calculationChild.id.name}$level${Constant.KEY_RATE}")

    // func dùng để start processBar func này chỉ chạy ở màn CalculationActivity
    // for 100 đại diện 100 số điểm mỗi câu hỏi max = 100
    //  delay(timeRepeat) đại diện thời gian cho mỗi câu hỏi là bao nhiêu giây biến: timeRepeat
    //rateCount.value <= 0 chứng minh số sao tối đa cho 1 vòng đã hết end game
    private fun startProgressbar() {
        job = CoroutineScope(Dispatchers.IO).launch {
            for (i in 100 downTo 0) {
                delay(timeRepeat)
                progress.postValue(i)
                if (i == 0) {
                    rateCount.postValue(rateCount.value!! - 1)
                    if(rateCount.value!! <= 0){
                        job?.cancel()
                        finishQuestionAndAnswer()
                    }
                }
            }
        }
    }

    //có 3 loại bàn phím: default : display bàn phím số
    // indexTypeKeyBoard = 1: bàn phím giữa := 2 bàn phím true false (mỗi index tương đương constant được define ở keyBoard KeyBoardType.TYPE_KEYBOARD)
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
        }else{
            clearTextKeyboard?.invoke()
        }
        indexTypeKeyBoard++
    }

    //trường hợp keyBoard type = true or false: kết quả xẽ được random dựa vào 2 trường hợp đúng hoặc sai (kết quả random được lưu listOf)
    //it.shuffled()
    private fun textChangeTypeTrueFalse(){
        listOf(answerKQ, Random.nextInt(answerKQ, answerKQ + 19)).let {
            if (keyBoardType.value == KeyBoardType.TYPE_TRUE_FALSE){
                textChange.value =  it.shuffled().random().toString()
            }
        }
    }

    //Mỗi View (Cộng-trừ-nhân-bình phương) 1 object lưu trữ giá trị: CalculationChild - màn LevelCalculation lưu level
    // func này dùng để truyền dữ liệu từ CalculationActivity ->ViewModel để handle logic code
    fun setCalculationChild(value: CalculationChild, level1: String, color1: Int) {
        _calculationChild = value
        level = level1
        color = color1
        levelCalculation.value = when (level) {
            Constant.EASY -> "Dễ"
            Constant.MEDIUM -> "Trung bình"
            else -> "Khó"
        }

        checkType()
    }

    //func chịu trách nhiệm sử lý chính bao gồm handle logic ở mỗi level (Khó - dễ - trung bình)
    // answerKQ là kết quả đúng giữa mỗi câu hỏi (timeRepead = 150 => 15s)
    // Random.nextInt tự tạo ra 1 số bất kỳ
    //EnumCalculation. đại diện cho type(phân biệt các phép tính, mỗi phép có 1 type)
    // content tiêu đề của màn cuối mỗi loại phép tính dc gán = mỗi tên khác nhau
    // symbolCalculation ký hiệu của phép tính (+/-/*/^)
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
                        a.value = Random.nextInt(300)
                        b.value = Constant.lisMinusPlushType2Level1.random()
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        a.value = Random.nextInt(300,500)
                        b.value = Constant.lisMinusPlushType2Level2.random()
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        a.value = Random.nextInt(500,800)
                        b.value = Constant.lisMinusPlushType2Level3.random()
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
                        a.value = Constant.lisMinusPlushType2Level1.random()
                        b.value = Random.nextInt(0,a.value!!-1)
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 100
                        a.value = Constant.lisMinusPlushType2Level2.random()
                        b.value = Random.nextInt(250,a.value!!-1)
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 50
                        a.value = Constant.lisMinusPlushType2Level2.random()
                        b.value = Random.nextInt(550,a.value!!-1)
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
                        val value = getValueMultiplicationABFromType2(10, 30)
                        a.value = value.first
                        b.value = value.second
                    }
                    Constant.MEDIUM -> {
                        timeRepeat = 70
                        val value = getValueMultiplicationABFromType2(10, 50)
                        a.value = value.first
                        b.value = value.second
                    }
                    Constant.DIFFICULTY_LEVEL -> {
                        timeRepeat = 40
                        val value = getValueMultiplicationABFromType2(10, 100)
                        a.value = value.first
                        b.value = value.second
                    }
                }
                answerKQ = a.value!! * b.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            EnumCalculation.MULTIPLICATION_TYPE_2 -> multiplicationTable(2)
            EnumCalculation.MULTIPLICATION_TYPE_3 -> multiplicationTable(3)
            EnumCalculation.MULTIPLICATION_TYPE_4 -> multiplicationTable(4)
            EnumCalculation.MULTIPLICATION_TYPE_5 -> multiplicationTable(5)
            EnumCalculation.MULTIPLICATION_TYPE_6 -> multiplicationTable(6)
            EnumCalculation.MULTIPLICATION_TYPE_7 -> multiplicationTable(7)
            EnumCalculation.MULTIPLICATION_TYPE_8 -> multiplicationTable(8)
            EnumCalculation.MULTIPLICATION_TYPE_9 -> multiplicationTable(9)
            EnumCalculation.SQUARING -> {
                symbolCalculation.postValue("^2")
                typeSquaring.postValue(true)
                setTimeRepeat()
                a.value = Constant.lisSquaring.shuffled().random()
                answerKQ = a.value!! * a.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
            else -> {
                typeSquaring.postValue(true)
                symbolCalculation.postValue("^2")
                setTimeRepeat()
                a.value = Random.nextInt(10, 19)
                answerKQ = a.value!! * a.value!!
                answer.value = answerKQ
                textChangeTypeTrueFalse()
            }
        }
    }

    // set thời gian mỗi câu hỏi 100 = 10s / 70 = 7s / 40 = 4s
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

    //show dialog và lưu lại các kết quả khi kết thúc game (lưu sao / điểm / totalAnswer đã trả lời dc)
    private fun finishQuestionAndAnswer() {
        sharePrefs.put(_calculationChild.id.name, totalAnswer.value!!)
        sharePrefs.put("${_calculationChild.id.name}$level", score.value!!)
        sharePrefs.put(
            "${_calculationChild.id.name}$level${Constant.KEY_RATE}",
            rateCount.value ?: 0
        )
        showDialog.postValue(true)
    }

    //get A và B của Phép nhân loại 2 số có hai chữ số cùng chữ...
    private fun getValueMultiplicationABFromType2(from: Int, util: Int): Pair<Int, Int> {
        while (true) {
            val a = Random.nextInt(from, util)
            val b = Random.nextInt(from, util)

            val c = a.toString().substring(1, 2).toInt()
            val d = b.toString().substring(1, 2).toInt()

            var e = a.toString().substring(0, 1).toInt()
            val f = b.toString().substring(0, 1).toInt()

            if (c + d == 10 && e == f) {
                return Pair(a, b)
            }
        }
    }


    //dùng đẻ display data trên màn hình : textChange->giá trị của phép tính (kết quả cuối)
    //keyBoardType: loại bàn phím hiển thị
    //answer dùng để gửi vào bàn phím để sửa lý (keyboard)
    override val answer: MutableLiveData<Number> = MutableLiveData(5)
    override val keyBoardType: MutableLiveData<KeyBoardType> =
        MutableLiveData(KeyBoardType.TYPE_KEYBOARD)
    override val textChange: MutableLiveData<String> = MutableLiveData("?")

    //khi người dùng nhập bàn phím xẽ bắn sự kiện sang func này
    // func này kiểm tra xem kết quả đúng hay không -> nếu xóa hết => display ?
    // value == answerKQ => đã nhập kết quả đúng
    // totalAnswer.value == 10: số lượng câu hỏi đúng đạt 10 => finishQuestionAndAnswer() kết thúc game
    // số lượng câu hỏi đúng < 10 => tiếp tục game
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

    //type keyboard true / false -> chọn đúng thì func này dc gọi
    // thực hiện kiểm tra giống onTextChange
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

    // dùng để remove text của keyboard
    override var clearTextKeyboard: (() -> Unit)? = null

    // handle bảng cửu chương 2->9
    // giống func checkType
    private fun multiplicationTable(int: Int){
            content.postValue("Bảng Cửu Chương $int")
            symbolCalculation.postValue("x")
            when (level) {
                Constant.EASY -> {
                    timeRepeat = 150
                    a.value = Random.nextInt(1,9)
                    b.value = int
                }
                Constant.MEDIUM -> {
                    timeRepeat = 100
                    a.value = Random.nextInt(1,9)
                    b.value = int
                }
                Constant.DIFFICULTY_LEVEL -> {
                    timeRepeat = 50
                    a.value = Random.nextInt(1,9)
                    b.value = int
                }
            }
            answerKQ = a.value!! * b.value!!
            answer.value = answerKQ
            textChangeTypeTrueFalse()
        }
}