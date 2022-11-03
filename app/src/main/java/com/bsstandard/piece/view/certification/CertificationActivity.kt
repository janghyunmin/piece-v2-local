package com.bsstandard.piece.view.certification

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.local.room.AppDatabase
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.databinding.ActivityAccessBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.deleteMember.DeleteMemberActivity
import com.bsstandard.piece.view.intro.IntroActivity
import com.bsstandard.piece.view.passcode.PassCodeActivity
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.DialogManager
import com.bsstandard.piece.widget.utils.DialogManager.openAuthDialog
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

/**
 *packageName    : com.bsstandard.piece.view.certification
 * fileName       : Certification
 * author         : piecejhm
 * date           : 2022/09/15
 * description    : 인증 및 보안 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/15        piecejhm       최초 생성
 */

@AndroidEntryPoint
class CertificationActivity : BaseActivity<ActivityAccessBinding>(R.layout.activity_access) {

    companion object {
        const val TAG: String = "CertificationActivity"
    }

    val mContext: Context = this@CertificationActivity
    private var status: Int? = null

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isFido: String? = PrefsHelper.read("isFido", "N");


    private val mv by viewModels<GetUserViewModel>()

    // 싱글톤 - jhm 2022/09/18
    val db = AppDatabase.getInstance(mContext)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@CertificationActivity

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            // 내정보 API - jhm 2022/09/03
            memberVm = mv

            // 생체인증 시작시 판별 - jhm 2022/09/16
            status = BiometricManager.from(mContext).canAuthenticate()
            if (status == BiometricManager.BIOMETRIC_SUCCESS) {
                // 생체인증 사용 가능 - jhm 2022/09/16
                isChecked()
                LogUtil.logE("사용 가능")
            } else {
                // 생체인증 사용 불가능 - jhm 2022/09/16
                isChecked()
                LogUtil.logE("사용 불가능")
            }
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                executor = ContextCompat.getMainExecutor(mContext)
                biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            PreferenceUtil(context = mContext).setString("isFido", "N")
                            LogUtil.logE("error : $errString")
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)
                            PreferenceUtil(context = mContext).setString("isFido", "Y")
                            LogUtil.logE("success : $result")
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            PreferenceUtil(context = mContext).setString("isFido", "N")
                            LogUtil.logE("failed..")
                        }
                    })

                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build()


                binding.switchBtn.isChecked =
                    PreferenceUtil(context = mContext).getString("isFido", "N") != "N"

                binding.switchBtn.setOnClickListener {
                    LogUtil.logE("등록시 OnClick..")
                    if (binding.switchBtn.isChecked) {
                        // 실패시 비밀번호 재설정 - jhm 2022/07/05
                        val customDialogListener: CustomDialogListener =
                            object : CustomDialogListener {
                                override fun onCancelButtonClicked() {
                                    // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                                    LogUtil.logE("닫기 OnClick..")
                                    binding.switchBtn.isChecked = false
                                    PreferenceUtil(context = mContext).setString("isFido", "N")
                                }

                                @RequiresApi(Build.VERSION_CODES.R)
                                override fun onOkButtonClicked() {
                                    LogUtil.logE("생체인증 등록 OnClick..")
                                    binding.switchBtn.isChecked = true
                                    PreferenceUtil(context = mContext).setString("isFido", "Y")
                                    authenticateToEncrypt()
                                }
                            }
                        openAuthDialog(
                            mContext,
                            "생체인증 등록이 필요해요",
                            "생체인증 등록을 진행해 주세요.",
                            customDialogListener,
                            "생체인증 등록",
                            binding.switchBtn.isChecked
                        )
                    } else {
                        LogUtil.logE("해제시 OnClick..")
                        // 실패시 비밀번호 재설정 - jhm 2022/07/05
                        val customDialogListener: CustomDialogListener =
                            object : CustomDialogListener {
                                override fun onCancelButtonClicked() {
                                    // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                                    LogUtil.logE("생체인증 해제 확인 OnClick..")
                                    binding.switchBtn.isChecked = false
                                    PreferenceUtil(context = mContext).setString("isFido", "N")
                                }

                                override fun onOkButtonClicked() {
                                    LogUtil.logE("생체인증 해제 취소 OnClick..")
                                    binding.switchBtn.isChecked = true
                                    PreferenceUtil(context = mContext).setString("isFido", "Y")
                                }
                            }
                        openAuthDialog(
                            mContext,
                            "생체인증 해제",
                            "생체인증을 사용하지 않도록 설정할까요?",
                            customDialogListener,
                            "생체인증 해제",
                            binding.switchBtn.isChecked
                        )
                    }
                }

                // 간편 비밀번호 변경 layout Onclick - jhm 2022/09/16
                binding.passwordLayout.setOnClickListener {
                    val intent = Intent(this, PassCodeActivity::class.java)
                    intent.putExtra("Step", "3")
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }


                binding.logoutLayout.setOnClickListener {
                    val customDialogListener: CustomDialogListener =
                        object : CustomDialogListener {
                            @RequiresApi(Build.VERSION_CODES.R)
                            override fun onOkButtonClicked() {
                                LogUtil.logE("로그아웃 OnClick..")
                                PrefsHelper.removeKey(mContext, "memberId");

                                val intent = Intent(mContext, IntroActivity::class.java)
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                startActivity(intent)
                                finishAffinity() // 이전 앱 화면 모두 제거 및 앱스택 제거 - jhm 2022/08/31
                            }

                            override fun onCancelButtonClicked() {
                                LogUtil.logE("취소 OnClick..")
                            }
                        }

                    DialogManager.openTwoBtnDialog(
                        mContext,
                        "로그아웃",
                        "일부메뉴는 이용하지 못할 수 도 있어요.\n그래도 로그아웃 할까요?",
                        customDialogListener,
                        "로그아웃"
                    )
                }

                binding.withdrawLayout.setOnClickListener {
                    val intent = Intent(this, DeleteMemberActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 생체인증 사용 Y/N - jhm 2022/09/16
    private fun isChecked() {
//        isFido = db!!.userDao().getIsFido(PrefsHelper.read("name",""),isFido).toString()
        isFido = PreferenceUtil(context = mContext).getString("isFido", "N")
        LogUtil.logE("isFido : $isFido")

        when (isFido) {
            "Y" -> {
                binding.switchBtn.isChecked = true
            }
            "N" -> {
                binding.switchBtn.isChecked = false
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "registerForActivityResult - RESULT_OK")
            PreferenceUtil(context = mContext).setString("isFido", "Y")
            authenticateToEncrypt()  //생체 인증 가능 여부확인 다시 호출
        } else {
            PreferenceUtil(context = mContext).setString("isFido", "N")
            binding.switchBtn.isChecked = false
            Log.d(TAG, "registerForActivityResult - NOT RESULT_OK")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
           * 생체 인식 인증을 사용할 수 있는지 확인
           * */
    @RequiresApi(Build.VERSION_CODES.R)
    fun authenticateToEncrypt() = with(binding) {

        Log.d(TAG, "authenticateToEncrypt() ")

        var textStatus = ""
        val biometricManager = BiometricManager.from(this@CertificationActivity)
//        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

            //생체 인증 가능
            BiometricManager.BIOMETRIC_SUCCESS -> textStatus =
                "App can authenticate using biometrics."

            //기기에서 생체 인증을 지원하지 않는 경우
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> textStatus =
                "No biometric features available on this device."

            //현재 생체 인증을 사용할 수 없는 경우
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> textStatus =
                "Biometric features are currently unavailable."

            //생체 인식 정보가 등록되어 있지 않은 경우
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                textStatus = "Prompts the user to create credentials that your app accepts."

                val dialogBuilder = AlertDialog.Builder(this@CertificationActivity)
                dialogBuilder
                    .setTitle("PIECE")
                    .setMessage("지문 등록이 필요합니다. 지문등록 설정화면으로 이동하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        goBiometricSettings()
                    }
                    .setNegativeButton("취소") { dialog, which ->
                        dialog.cancel()
//                        isFido = "Y"
                        PreferenceUtil(context = mContext).setString("isFido", "N")
//                        db!!.userDao().updateByIsFido("N",PrefsHelper.read("name","")) // update - jhm 2022/09/19
                        binding.switchBtn.isChecked = false
                    }
                dialogBuilder.show()
            }

            //기타 실패
            else -> textStatus = "Fail Biometric facility"

        }

        //인증 실행하기
        goAuthenticate()
    }

    /*
    * 생체 인식 인증 실행
    * */
    private fun goAuthenticate() {
        Log.d(TAG, "goAuthenticate - promptInfo : $promptInfo")
        promptInfo.let {
            PreferenceUtil(context = mContext).setString("isFido", "Y")
//            isFido = "Y"
//            db!!.userDao().updateByIsFido("Y",PrefsHelper.read("name","")) // update - jhm 2022/09/19
            biometricPrompt.authenticate(it);  //인증 실행
        }
    }


    /*
   * 지문 등록 화면으로 이동
   * */
    @RequiresApi(Build.VERSION_CODES.R)
    fun goBiometricSettings() {
        val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
        }
        PreferenceUtil(context = mContext).setString("isFido", "Y")

//        isFido = "Y"
        db!!.userDao().updateByIsFido("Y", PrefsHelper.read("name", "")) // update - jhm 2022/09/19
        startActivityForResult(enrollIntent, 0)
//        loginLauncher.launch(enrollIntent)
    }

    class PreferenceUtil(context: Context) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences("isFido", Context.MODE_PRIVATE)

        fun setString(key: String, str: String) {
            prefs.edit().putString(key, str).apply()
        }

        fun getString(key: String, defValue: String): String {
            return prefs.getString(key, defValue).toString()
        }
    }


    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setStatusBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // minSdk 6.0부터 사용 가능
            window.decorView.systemUiVisibility = if (isBlack) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 기존 uiVisibility 유지
                window.decorView.systemUiVisibility
            }

        } // end if

    }

    /**
     * 상태바 배경 색상 지정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setStatusBarBgColor(colorHexValue: String) {

        // 상태바 배경색은 5.0부터 가능하나, 아이콘 색상은 6.0부터 변경 가능
        // -> 아이콘/배경색 모두 바뀌어야 의미가 있으므로 6.0으로 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.parseColor(colorHexValue)

        } // end if
    }

    /**
     * 내비바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setNaviBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 아이콘 색상이 8.0부터 가능하므로 커스텀은 동시에 진행해야 하므로 조건 동일 처리.
            window.decorView.systemUiVisibility =
                if (isBlack) {
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

                } else {
                    // 기존 uiVisibility 유지
                    // -> 0으로 설정할 경우, 상태바 아이콘 색상 설정 등이 지워지기 때문
                    window.decorView.systemUiVisibility

                } // end if

        } // end if
    }

    /**
     * 내비바 배경 색상 설정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setNaviBarBgColor(colorHexValue: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 배경색은 8.0부터 지원한다.
            window.navigationBarColor = Color.parseColor(colorHexValue)

        } // end if

    }

}