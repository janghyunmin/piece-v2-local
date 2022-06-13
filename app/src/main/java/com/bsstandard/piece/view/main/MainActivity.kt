package com.bsstandard.piece.view.main

import android.os.Bundle
import androidx.activity.viewModels
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityMainBinding
import com.bsstandard.piece.widget.utils.DeviceInfoUtil
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint

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
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = mainViewModel

            LogUtil.logE("deviceId : " + DeviceInfoUtil.getDeviceId(this@MainActivity))

        }

    }


}
