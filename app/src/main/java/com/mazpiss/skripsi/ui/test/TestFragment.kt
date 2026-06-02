package com.mazpiss.skripsi.ui.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.mazpiss.skripsi.ui.faq.FaqActivity
import com.mazpiss.skripsi.ui.quiz.QuizActivity
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ShorofTheme {
                    TestScreen(
                        onStartQuiz = {
                            startActivity(Intent(requireActivity(), QuizActivity::class.java))
                        },
                        onViewRules = {
                            startActivity(Intent(requireActivity(), FaqActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
