package com.bsstandard.piece.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.fragment.navigation.KeepStateNavigator
import com.bsstandard.piece.view.main.MainActivity
import com.bsstandard.piece.widget.utils.LogUtil

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMore
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 더보기 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

class FragmentMore : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        return view
    }
}