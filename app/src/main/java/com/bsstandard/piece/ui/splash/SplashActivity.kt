package com.bsstandard.piece.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bsstandard.piece.ui.main.MainActivity
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.piece.ui.splash
 * fileName       : SplashActivity
 * author         : piecejhm
 * date           : 2022/04/28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/28        piecejhm       최초 생성
 */


class SplashActivity : AppCompatActivity()  {
    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelCallback()
        viewModel.goSplash()
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            goLogin.observe(this@SplashActivity, Observer{
                goLogin()
            })
        }
    }

    // SplashActivity -> LoginActivity 이동
    private fun goLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}