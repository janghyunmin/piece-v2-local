package com.bsstandard.piece.view.main.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.PopupViewModel
import com.bsstandard.piece.databinding.SlideupEventBinding
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


/**
 *packageName    : com.bsstandard.piece.view.main.dialog
 * fileName       : EventSheet
 * author         : piecejhm
 * date           : 2022/07/07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/07        piecejhm       최초 생성
 */

class EventSheet(context: Context) : BottomSheetDialogFragment() {
    private lateinit var popVm: PopupViewModel
    lateinit var binding: SlideupEventBinding;
    private val mContext: Context = context
    private var strSDFormatDay: String = "0"
    private var imgPath: String = ""

    // 팝업 타입 - jhm 2022/11/04
    private var popType: String = ""

    // 팝업 타입에 따른 Id - jhm 2022/11/04
    private var popupLinkUrl: String = ""


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        binding = SlideupEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this




        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View);
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        //bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    // do on STATE_EXPANDED
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    // do on STATE_COLLAPSED
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do on slide
            }
        })


        popVm = ViewModelProvider(this)[PopupViewModel::class.java]
        popVm.getPopup("POP0102")
        popVm.popupResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                LogUtil.logE("팝업 조회 popupId : ${it.data.popupId}")
                LogUtil.logE("팝업 조회 popupTitle : ${it.data.popupTitle}")
                LogUtil.logE("팝업 조회 popupType : ${it.data.popupType}")
                LogUtil.logE("팝업 조회 popupTypeName : ${it.data.popupTypeName}")
                LogUtil.logE("팝업 조회 popupImagePath : ${it.data.popupImagePath}")
                LogUtil.logE("팝업 조회 popupLinkType : ${it.data.popupLinkType}")
                LogUtil.logE("팝업 조회 popupLinkUrl : ${it.data.popupLinkUrl}")

                try {
                    Handler(Looper.getMainLooper()).postDelayed({
                        // 팝업 이미지 - jhm 2022/11/04
                        Glide.with(this@EventSheet)
                            .load(it.data.popupImagePath)
                            .into(binding.img)

                        roundTop(binding.img,32f)
                    }, 200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }



                // 팝업 타입 조회 - jhm 2022/11/04
                popType = it.data.popupType
                // 팝업 타입에 따른 Id - jhm 2022/11/04
                popupLinkUrl = it.data.popupLinkUrl

                /**
                 * popupType
                 * POP0201 : 포트폴리오 상세
                 * POP0202 : 라운지 상세
                 * POP0203 : 공지사항 상세
                 * POP0204 : 이벤트 상세
                 * POP0205 : 알림 설정 상세
                 * POP0206 : 내 정보 상세
                 * POP0207 : 웹뷰
                 * POP0208 : 팝업 버튼 타입 1 - 오늘은 보지 않기
                 * POP0209 : 팝업 버튼 타입 2 - 오늘은 보지 않기
                 * POP0210 : 팝업 버튼 타입 3 - 앱 다운로드
                 * POP0211 : 팝업 버튼 타입 4 - 앱 다운로드
                 * **/


            }
        })


        // '오늘 그만 보기' 기능을 위한 날짜 획득
        val CurrentTime = System.currentTimeMillis() // 현재 시간을 msec 단위로 얻음
        val TodayDate = Date(CurrentTime) // 현재 시간 Date 변수에 저장
        val SDFormat = SimpleDateFormat("dd")
        strSDFormatDay = SDFormat.format(TodayDate) // 'dd' 형태로 포맷 변경

        binding.today.setOnClickListener { toDayDismiss() }
        binding.close.setOnClickListener { onDismiss() }




        binding.img.setOnClickListener {
            listener?.onItemClick(
                view,
                ""
            )
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, tag: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    // 오늘은 보지 않기 이벤트 - jhm 2022/07/07
    fun toDayDismiss() {
        LogUtil.logE("오늘은 보지 않기 onClick..")
        PrefsHelper.write("Day", strSDFormatDay)
        dismiss()
    }

    // 닫기 이벤트 - jhm 2022/07/07
    fun onDismiss() {
        LogUtil.logE("닫기 onClick..")
        dismiss()
    }



    // 이미지 상단만 둥글게 처리 로직 - jhm 2022/11/04
    fun roundTop(iv: ImageView, curveRadius : Float)  : ImageView {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            iv.outlineProvider = object : ViewOutlineProvider() {

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height+curveRadius).toInt(), curveRadius)
                }
            }

            iv.clipToOutline = true
        }
        return iv
    }


}

