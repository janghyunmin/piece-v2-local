package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionInflater
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.FragmentHomeBinding
import com.bsstandard.piece.retrofit.WebSocketListener
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
import com.bsstandard.piece.view.alarm.AlarmActivity
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.main.dialog.EventSheet
import com.bsstandard.piece.widget.utils.Division
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.portfolio_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*


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


/**
 * 포트폴리오 조회 웹소켓 데이터 리스트
 * **/
data class Portfolio(
    val purchaseTotalAmount: Int,
    val portfolioId: String,
    val purchasePieceVolume: Int,
    val recruitmentState: String
)


@AndroidEntryPoint
class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: PortfolioViewModel
    private lateinit var client: OkHttpClient
    private var disposable: Disposable? = null
    var strSDFormatDay: String = ""

    private val portfolioWsList: ArrayList<Portfolio> = arrayListOf()


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


    @SuppressLint("UseRequireInsteadOfGet", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity !=null && isAdded)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        vm = ViewModelProvider(this)[PortfolioViewModel::class.java]
        binding.home = vm
        binding.lifecycleOwner = viewLifecycleOwner

        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(false) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

//        // statusBar Height - jhm 2022/10/28
//        getStatusBarHeight(requireContext())
//        binding.pLayout.setPadding(0, getStatusBarHeight(requireContext()), 0, 0)

        val CurrentTime = System.currentTimeMillis() // 현재 시간을 msec 단위로 얻음
        val TodayDate = Date(CurrentTime) // 현재 시간 Date 변수에 저장
        val SDFormat = SimpleDateFormat("dd")
        strSDFormatDay = SDFormat.format(TodayDate) // 'dd' 형태로 포맷 변경

        LogUtil.logE("strSDFormatDay $strSDFormatDay")
        // 오늘하루 안보기 이벤트 날짜 비교 - jhm 2022/10/28
        activity?.runOnUiThread {
            if(activity != null) {
                if(strSDFormatDay.toInt().minus(PrefsHelper.read("Day","0").toInt()) != 0) {
                    val eventSheet = EventSheet(activity!!)
                    Handler(Looper.getMainLooper()).postDelayed({
                        eventSheet.show(childFragmentManager, eventSheet.tag)
                    }, 1500)
                }
            }

        }

        startWebSocket()

        val networkConnection = NetworkConnection(context!!)
        networkConnection.observe(viewLifecycleOwner) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {

                vm.viewInit(binding.portfolioRv)
                vm.getPortfolio(500)

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




                vm.portfolioAdapter.setOnItemClickListener(object :
                    PortfolioAdapter.OnItemClickListener {
                    override fun onItemClick(
                        v: View,
                        portfolioId: String,
                        portfolioImagePath: String
                    ) {
                        LogUtil.logE("portfolioId $portfolioId")
                        LogUtil.logE("portfolioImagePath $portfolioImagePath")

                        val extras = FragmentNavigatorExtras(
                            portfolio_img to portfolioImagePath
                        )
                        val action = FragmentHomeDirections.navToPortoflioDetailActivity(
                            portfolioId,
                            portfolioImagePath
                        )
                        val inflater = TransitionInflater.from(requireContext())
                        exitTransition = inflater.inflateTransition(R.transition.change_bounds)
                        NavHostFragment.findNavController(this@FragmentHome)
                            .navigate(action, extras)



                       /* findNavController().navigate(action, extras)

                        val change_image_transform = TransitionInflater.from(requireContext())
                            .inflateTransition(R.transition.image_shared_element_transition)
                        val change_bounds = TransitionInflater.from(requireContext())
                            .inflateTransition(R.transition.image_shared_element_transition)

                        sharedElementEnterTransition = change_image_transform
                        sharedElementReturnTransition = change_image_transform

//                    findNavController().navigate(R.id.PortfolioDetailActivity,null,null,extras)

                        val intent = Intent(context, PortfolioDetailActivity::class.java)
                        intent.putExtra("portfolio_img", portfolioImagePath)

                        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                            activity,
                            portfolio_img,
                            portfolioImagePath
                        )
                        val options =
                            activity?.let {
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    it,
                                    UtilPair.create(portfolio_img, portfolioImagePath)
                                )
                            }
                        val extras = ActivityNavigatorExtras(options)
                        view?.findNavController()?.navigate(
                            R.id.PortfolioDetailActivity,
                            null, // Bundle of args
                            null, // NavOptions
                            extras
                        )


                        NavHostFragment.findNavController(this@FragmentHome)
                            .navigate(action, extras)
                        ViewCompat.setTransitionName(portfolio_img, portfolioImagePath)
                        findNavController().navigate(action, extras)


                        var fadeTransition: Transition =
                            TransitionInflater.from(requireContext())
                                .inflateTransition(R.transition.change_bounds)

                        sharedElementEnterTransition = fadeTransition
                        sharedElementReturnTransition = fadeTransition

                        val bundle = Bundle()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val bundle = ActivityOptions.makeSceneTransitionAnimation(
                                this,
                                portfolio_img, portfolioImagePath
                            )
                        }

                        findNavController().navigate(action, extras)
*/

                    }
                })


            } else {
                val intent = Intent(context, NetworkActivity::class.java)
                startActivity(intent)
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.logE("화면 파괴")
        _binding = null
//        vm.stopWebSocket()
        client.dispatcher().cancelAll()
    }


    // 포트폴리오 WebSocket - jhm 2022/10/28
    fun startWebSocket(): String {
        val listener: WebSocketListener = WebSocketListener()

        portfolioWsList.clear()

        GlobalScope.launch(Dispatchers.IO) {
            client = OkHttpClient()

            val request: Request = Request.Builder()
                .url(Division.PIECE_WS_PORTFOLIO)
                .build()

            client.newWebSocket(request, listener)
            client.dispatcher().executorService().shutdown()

        }


//        val list = JSONObject(listener.liveData.value.toString())
//        val jsonArray = list.optJSONArray("data")
//        var i = 0
//        while (i < jsonArray.length()) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            portfolioWsList.add(
//                Portfolio(
//                    jsonObject.getInt("purchaseTotalAmount"),
//                    jsonObject.getString("portfolioId"),
//                    jsonObject.getInt("purchasePieceVolume"),
//                    jsonObject.getString("recruitmentState")
//                )
//            )
//        }
//
//        for(i in 0 until portfolioWsList.size) {
//            LogUtil.logE("리스트 변환 : " + portfolioWsList[i].portfolioId)
//        }


        return listener.liveData.value.toString()
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


    // statusbar height 추가
    open fun getStatusBarHeight(context: Context): Int {
        val screenSizeType = context.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK
        var statusbar = 0
        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusbar = context.resources.getDimensionPixelSize(resourceId)
            }
        }
        return statusbar
    }

    // statusbar height 추가
    fun getStatusBarHeightKt(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    fun getNaviBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

}

