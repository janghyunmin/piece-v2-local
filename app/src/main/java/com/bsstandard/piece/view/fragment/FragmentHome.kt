package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bsstandard.piece.R
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.FragmentHomeBinding
import com.bsstandard.piece.retrofit.WebSocketListener
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
import com.bsstandard.piece.view.alarm.AlarmActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.gson.JsonArray
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.portfolio_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMain
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 홈화면 탭이며 포트폴리오 목록 조회를 함 , homeViewModel 의존성 주입
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@AndroidEntryPoint
class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: PortfolioViewModel
//    private lateinit var svm: WebSocketViewModel
    private var disposable: Disposable? = null



    companion object {
        fun newInstance(): FragmentHome {
            return FragmentHome()
        }
    }




    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }

    }




    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

//        svm = WebSocketViewModel()
        vm = ViewModelProvider(this)[PortfolioViewModel::class.java]
        vm.viewInit(binding.portfolioRv)
        vm.getPortfolio(100)


//        binding.socketVm = svm
        binding.home = vm
        binding.lifecycleOwner = viewLifecycleOwner


        vm.portfolioAdapter.setOnItemClickListener(object : PortfolioAdapter.OnItemClickListener {
            override fun onItemClick(v: View, portfolioId: String, portfolioImagePath: String) {
                LogUtil.logE("portfolioId $portfolioId")
                LogUtil.logE("portfolioImagePath $portfolioImagePath")

                val extras = FragmentNavigatorExtras(
                    portfolio_img to id.toString()
                )
                val action = FragmentHomeDirections.navToPortoflioDetailActivity(
                    portfolioId,
                    portfolioImagePath
                )
                val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_top)

                sharedElementEnterTransition = animation
                sharedElementReturnTransition = animation

                findNavController().navigate(action, extras)

            }
        })


        // 알림 및 혜택 페이지 이동 - jhm 2022/10/16
        binding.notiIcon.setOnClickListener {
            LogUtil.logE("알림 및 혜택 페이지 이동..")
            val intent = Intent(context, AlarmActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
        }

//        svm.runStomp()

//        SocketThread(false)

        startWebSocket()

        return binding.root
    }

    fun startWebSocket() : JsonArray? {
        var client: OkHttpClient

        // 포트폴리오 웹소켓 thread - jhm 2022/10/19
        var listener = WebSocketListener()

        GlobalScope.launch(Dispatchers.IO) {
            client = OkHttpClient()
            val request = Request.Builder()
//                .url("ws://192.168.0.39:10000/portfolio")
                .url("wss://fdev-websocket.piece.la/portfolio")
                .build()


            client.newWebSocket(request, listener)
            client.dispatcher().executorService().shutdown()

        }

        return listener.liveData.value?.asJsonArray
    }

    // 메모리에 올라갔을때 - jhm 2022/07/15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        SocketThread(false)
    }

    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setStatusBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            activity?.window?.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // minSdk 6.0부터 사용 가능
            activity?.window?.decorView?.systemUiVisibility = if (isBlack) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 기존 uiVisibility 유지
                activity?.window?.decorView!!.systemUiVisibility
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
            activity?.window?.statusBarColor = Color.parseColor(colorHexValue)

        } // end if
    }

    /**
     * 내비바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setNaviBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            activity?.window?.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 아이콘 색상이 8.0부터 가능하므로 커스텀은 동시에 진행해야 하므로 조건 동일 처리.
            activity?.window?.decorView!!.systemUiVisibility =
                if (isBlack) {
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

                } else {
                    // 기존 uiVisibility 유지
                    // -> 0으로 설정할 경우, 상태바 아이콘 색상 설정 등이 지워지기 때문
                    activity?.window?.decorView!!.systemUiVisibility

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
            activity?.window?.navigationBarColor = Color.parseColor(colorHexValue)

        } // end if
    }

}

