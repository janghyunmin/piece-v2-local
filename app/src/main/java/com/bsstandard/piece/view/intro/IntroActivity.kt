package com.bsstandard.piece.view.intro

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.ActivityIntroBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.join.JoinActivity
import com.bsstandard.piece.view.main.MainActivity
import com.bsstandard.piece.widget.utils.Division
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


/**
 *packageName    : com.bsstandard.piece.ui.intro
 * fileName       : IntroActivity
 * author         : piecejhm
 * date           : 2022/05/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/04        piecejhm       최초 생성
 */


@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var introBinding: ActivityIntroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        introBinding = DataBindingUtil.setContentView(this,R.layout.activity_intro)
        introBinding.lifecycleOwner = this@IntroActivity
        introBinding.activity = this@IntroActivity

        // statusBar 공통 - jhm 2022/06/13
        setStatusBar()
        Glide.with(this).load(R.drawable.hello_lopping).into(introBinding.hello)

        // 권한체크 - jhm 2022/06/13
        checkPermission()

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                introBinding.iButton.setOnClickListener {
                    val intent = Intent(this, JoinActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }

                introBinding.pieceText.setOnClickListener {
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            } else {
//                DialogManager.openNetWorkChk(this@IntroActivity)

                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun setStatusBar(){
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    fun checkPermission() {
        // 권한 승인상태 가져오기
        var permissionList = ArrayList<String>()
        for(permission in Division.REQUEST_PER){
            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                // 만약 권한이 없다면
                permissionList.add(permission)
            }
        }

        // 거절된 퍼미션이 있다면
        if(permissionList.isNotEmpty()){
            val array = arrayOfNulls<String>(permissionList.size)
            ActivityCompat.requestPermissions(this,permissionList.toArray(array), Division.PERMISSIONS_REQUEST_CODE)
        }
    }

    // 권한 요청 결과 처리 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            Division.PERMISSIONS_REQUEST_CODE -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //권한 획득 실패

                                // 권한 체크 로직 변경해야함 - jhm 2022/11/02
                            LogUtil.logE( "권한 거부함 + $permission")
//                            finish()
                        }
                    }
                }
            }
        }
    }
}
