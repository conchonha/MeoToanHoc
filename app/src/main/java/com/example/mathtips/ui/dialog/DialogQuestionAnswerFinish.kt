package com.example.mathtips.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import com.example.mathtips.R
import com.example.mathtips.databinding.DialogFinishQuestionAnswerBinding
import com.example.mathtips.ui.viewmodel.MainViewModel
import com.sangtb.androidlibrary.utils.DialogLibrary

//Hộp thoại dialog display
class DialogQuestionAnswerFinish : DialogLibrary<DialogFinishQuestionAnswerBinding>() {
    //layout dialog
    override val layout: Int
        get() = R.layout.dialog_finish_question_answer
    //viewmodel chịu trách nhiệm xử lý logic
    override val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DialogQuestionAnswerFinish.viewModel
        }

        binding.apply {
            // khi dialog show -> im1 = quay lại img2 = đóng app
            img1.setOnClickListener {
                activity?.finish()
            }

            img2.setOnClickListener {
                activity?.finishAffinity()
            }
        }
    }
}