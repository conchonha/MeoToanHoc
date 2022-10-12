package com.example.mathtips.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.mathtips.R
import com.example.mathtips.databinding.DialogFinishQuestionAnswerBinding
import com.example.mathtips.ui.viewmodel.MainViewModel
import com.sangtb.androidlibrary.utils.DialogLibrary

class DialogQuestionAnswerFinish : DialogLibrary<DialogFinishQuestionAnswerBinding>() {
    override val layout: Int
        get() = R.layout.dialog_finish_question_answer
    private val viewModel1 by activityViewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel1
        }

        binding.apply {
            img1.setOnClickListener {
                activity?.finish()
            }

            img2.setOnClickListener {
                activity?.finishAffinity()

            }
        }
    }
}