package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRegModel
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRemoveModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.BookMarkViewModel
import com.bsstandard.piece.data.viewmodel.MagazineViewModel
import com.bsstandard.piece.databinding.FragmentMagazineBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.adapter.magazine.MagazineAdapter
import com.bsstandard.piece.view.bookmark.BookMarkActivity
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.webview.MagazineDetailWebView
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMagazine
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 매거진 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@AndroidEntryPoint
class FragmentMagazine : Fragment() {

    private var _binding: FragmentMagazineBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: MagazineViewModel
    private lateinit var bvm: BookMarkViewModel

    private var disposable: Disposable? = null

    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    var memberMap = HashMap<String, String>()


    companion object {
        fun newInstance(): FragmentMagazine {
            return FragmentMagazine()
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_magazine, container, false)

//        // 상단 시계바 투명처리 - jhm 2022/10/27
//        requireActivity().setStatusBarTransparent()

        getStatusBarHeight(requireContext())
//        binding.motionlayout.setPadding(0, getStatusBarHeight(requireContext()), 0, 0)
        binding.motionlayout.setPadding(0, 0, 0, 0)



        vm = ViewModelProvider(this)[MagazineViewModel::class.java]
        vm.viewInit(binding.magazineRv)
        binding.magazine = vm

        bvm = ViewModelProvider(this)[BookMarkViewModel::class.java]
        binding.bookmarkVM = bvm
        binding.lifecycleOwner = viewLifecycleOwner


        // 비회원으로 매거진 들어왔을때 - jhm 2022/08/30
        if (PrefsHelper.read("memberId","").equals("")) {
            vm.getNoMemberMagazine("") // 타입 = 전체는 공백 - jhm 2022/08/30
            binding.allBookmarkCount.text = "0"
        } else {
            // 매거진 (라운지) 전체 리스트 뷰모델 참조 - jhm 2022/08/31
            vm.getMagazine("")

            // 회원 북마크 조회 - jhm 2022/09/01
            bvm.getBookMark(accessToken, deviceId, memberId)
        }

        binding.tabs.setBackgroundColor(Color.parseColor("#FFFFFF"))
        binding.tabs.addTab(binding.tabs.newTab().setText("전체"))
        binding.tabs.addTab(binding.tabs.newTab().setText("포트폴리오"))
        binding.tabs.addTab(binding.tabs.newTab().setText("핀테크 트렌드"))
        binding.tabs.addTab(binding.tabs.newTab().setText("핫 플레이스"))
        binding.tabs.addTab(binding.tabs.newTab().setText("쿨 피플"))
        binding.tabs.addTab(binding.tabs.newTab().setText("잘알못 칼럼"))
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                LogUtil.logE("tab 재선택")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    LogUtil.logE("tab 선택" + tab.contentDescription)
                    when (tab.contentDescription) {
                        "전체" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("")
                            } else {
                                vm.getMagazine("")
                            }

                        }
                        "포트폴리오" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("MZT0201")
                            } else {
                                vm.getMagazine("MZT0201")
                            }
                        }
                        "핀테크 트렌드" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("MZT0101")
                            } else {
                                vm.getMagazine("MZT0101")
                            }
                        }
                        "핫 플레이스" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("MZT0102")
                            } else {
                                vm.getMagazine("MZT0102")
                            }
                        }
                        "쿨 피플" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("MZT0103")
                            } else {
                                vm.getMagazine("MZT0103")
                            }
                        }
                        "잘알못 칼럼" -> {
                            if (PrefsHelper.read("memberId","").equals("")) {
                                vm.getNoMemberMagazine("MZT0104")
                            } else {
                                vm.getMagazine("MZT0104")
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                LogUtil.logE("tab 선택 안함")
            }
        })


        // 최상단 북마크 클릭시 로그인 여부 판별 - jhm 2022/08/31
        binding.allBookmark.setOnClickListener { view ->
            // 로그인이 안되어 있다면 로그인 이동하라는 페이지로 이동 - jhm 2022/08/31
            if (PrefsHelper.read("memberId","").equals("")) {
                val intent = Intent(context, LoginChkActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            }
            // 그게 아니라면 회원 북마크 페이지로 이동 - jhm 2022/08/31
            else {
                val intent = Intent(context, BookMarkActivity::class.java)
                context?.startActivity(intent)
            }
        }
        vm.isFavorite.observe(viewLifecycleOwner, Observer {

        })
        vm.magazineAdapter.setOnItemClickListener(object : MagazineAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View,
                tag: String,
                magazineId: String,
                magazineImagePath: String,
                isFavorite: Boolean,
                smallTitle: String,
                pos: Int
            ) {

                when (tag) {
                    "webView" -> {
                        LogUtil.logE("매거진 상세 웹뷰 호출..")
                        val intent = Intent(context, MagazineDetailWebView::class.java)
                        intent.putExtra("magazineId", magazineId)
                        intent.putExtra("isFavorite", isFavorite)
                        intent.putExtra("pos", pos)
                        startActivity(intent)
                        activity?.overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        );
                    }

                    "bookMark" -> {
                        LogUtil.logE("북마크 select..")

                        // 북마크 Post 요청시 필요 모델 - jhm 2022/08/29
                        val memberBookmarkRegModel = MemberBookmarkRegModel(memberId, magazineId)
                        // 북마크 Delete 요청시 필요 모델 - jhm 2022/08/29
                        val memberBookmarkRemoveModel =
                            MemberBookmarkRemoveModel(memberId, magazineId)

                        // 북마크 저장 - jhm 2022/08/29
                        if (isFavorite) {
                            LogUtil.logE("isFavorite success : $isFavorite")
                            //bvm.getBookMark()
                            response?.updateBookMark(
                                "Bearer $accessToken",
                                deviceId,
                                memberId,
                                memberBookmarkRegModel
                            )
                                ?.enqueue(object : Callback<BaseDTO> {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onResponse(
                                        call: Call<BaseDTO>,
                                        response: Response<BaseDTO>
                                    ) {
                                        LogUtil.logE("bookmark post call success.." + response.body()?.status)
                                        try {
                                            if (!response.body().toString().isEmpty()) {
                                                LogUtil.logE("bookmark status : " + response.body()?.status)
                                                LogUtil.logE("post smallTitle : $smallTitle")

                                                bvm.getBookMark(accessToken, deviceId, memberId)

                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            LogUtil.logE("bookmark post catch error ! " + ex.message)
                                        }
                                    }

                                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                        t.printStackTrace()
                                        LogUtil.logE("bookmark post call fail.." + t.message)
                                    }
                                })
                        } else {
                            response?.deleteBookMark(
                                "Bearer $accessToken",
                                deviceId,
                                memberId,
                                memberBookmarkRemoveModel
                            )
                                ?.enqueue(object : Callback<BaseDTO> {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onResponse(
                                        call: Call<BaseDTO>,
                                        response: Response<BaseDTO>
                                    ) {
                                        LogUtil.logE("bookmark delete call success.." + response.body()?.status)
                                        try {
                                            if (!response.body().toString().isEmpty()) {
                                                LogUtil.logE("bookmark status : " + response.body()?.status)
                                                LogUtil.logE("delete smallTitle : $smallTitle")
                                                bvm.getBookMark(accessToken, deviceId, memberId)

                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            LogUtil.logE("북마크 삭제 catch " + ex.message)
                                        }
                                    }

                                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                        t.printStackTrace()
                                        LogUtil.logE("bookmark post call fail.." + t.message)

                                    }
                                })
                        }
                    }
                }

            }
        })

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 회원 북마크 갯수 - jhm 2022/09/01
        bvm.liveData.observe(viewLifecycleOwner, Observer { bookMarkDto ->
            bookMarkDto?.let {
                binding.allBookmarkCount.text = bookMarkDto.size.toString()
                bvm.bookMarkAdapter.notifyDataSetChanged()
            }
        })

        //requireActivity().setStatusBarTransparent() // 상태 바 투명으로 설정(전체화면)
    }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        vm.magazineAdapter.notifyDataSetChanged()


        // 회원 북마크 갯수 - jhm 2022/09/01
        bvm.liveData.observe(viewLifecycleOwner, Observer { bookMarkDto ->
            bookMarkDto?.let {
                binding.allBookmarkCount.text = bookMarkDto.size.toString()
                bvm.bookMarkAdapter.notifyDataSetChanged()
            }
        })


    }

    fun Activity.setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    // 메모리에 올라갔을때 - jhm 2022/07/15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


}