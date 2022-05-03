package com.bsstandard.piece.ui.main

import androidx.activity.viewModels
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.piece.ui.main
 * fileName       : MainActivity
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : Dagger hilt 적용완료
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */



@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding> (R.layout.activity_main) {
    @Inject lateinit var mainViewModel: MainViewModel

    override fun init() {

    }
}
