package com.mazpiss.skripsi.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.mazpiss.skripsi.ui.aboutApp.AboutActivity
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ShorofTheme {
                    HomeScreen(
                        onNavigateToAbout = {
                            startActivity(Intent(requireActivity(), AboutActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
