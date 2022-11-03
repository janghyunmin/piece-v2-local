package com.bsstandard.piece.view.portfolioDetail

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.WindowInsetsController
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.PortfolioNotificationViewModel
import com.bsstandard.piece.databinding.ActivityPortfoliodetailBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailCompositionAdapter
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailEvidenceAdapter
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.purchase.PurchaseActivity
import com.bsstandard.piece.view.webview.MagazineDetailWebView
import com.bsstandard.piece.widget.utils.ConvertMoney
import com.bsstandard.piece.widget.utils.IndentLeadingMarginSpan
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *packageName    : com.bsstandard.piece.view.portfolioDetail
 * fileName       : TestActivity
 * author         : piecejhm
 * date           : 2022/07/21
 * description    : 포트폴리오 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/21        piecejhm       최초 생성
 */

@AndroidEntryPoint
class PortfolioDetailActivity :
    BaseActivity<ActivityPortfoliodetailBinding>(R.layout.activity_portfoliodetail) {

    val args: PortfolioDetailActivityArgs by navArgs()
    var portfolioId: String = "";
    var portfolioPath: String = "";
    private lateinit var vm: PortfolioDetailViewModel
    private lateinit var pvm: PortfolioNotificationViewModel

    private var disposable: Disposable? = null
    private var selectedItemPosition = -1
    private var selectedLayout: ConstraintLayout? = null
    private lateinit var portfolioDetailCompositionAdapter: PortfolioDetailCompositionAdapter

    // 포트폴리오 자세히보기 클릭시 넘겨줄 값 - jhm 2022/10/18
    private var magazineId: String = ""

    // 구매할때 필요한 변수 값 - jhm 2022/10/20
    private var expectationProfitRate: String = ""
    private var totalPieceVolume: String = ""
    private var minPurchaseAmount: String = ""
    private var maxPurchaseAmount: String = ""
    private var portfolioTitle: String = ""

    // 포트폴리오 알림 조회 여부 상태값 - jhm 2022/11/02
    private var notificationYn: String = ""

    // 포트폴리오 알림 상태 변경 - jhm 2022/11/03
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            TransitionInflater.from(applicationContext).inflateTransition(android.R.transition.move)

            binding.lifecycleOwner = this@PortfolioDetailActivity
            binding.activity = this@PortfolioDetailActivity




            portfolioId = args.portfolioId
            LogUtil.logE("넘겨받은 portfolioId : $portfolioId")

            portfolioPath = args.portfolioImagePath
            LogUtil.logE("넘겨받은 portfolioImagePath $portfolioPath")

            ViewCompat.setTransitionName(portfolioImg, portfolioPath)

            pvm =
                ViewModelProvider(this@PortfolioDetailActivity)[PortfolioNotificationViewModel::class.java]
            // 포트폴리오 알림 여부 조회 - jhm 2022/11/02
            binding.portfolioNotiViewModel = pvm

            // 포트폴리오 상세 조회 VIEWMODEL - jhm 2022/11/03
            vm =
                ViewModelProvider(this@PortfolioDetailActivity)[PortfolioDetailViewModel::class.java]


            binding.notiRadius.visibility = View.GONE
            binding.notiView.visibility = View.GONE
            binding.unionLayout.visibility = View.GONE
            binding.portfolioBtn.visibility = View.GONE
            binding.buyPortfolioBtn.visibility = View.GONE
            binding.allSellBtn.visibility = View.GONE

            // 포트폴리오 상세 슬라이드시 toolbar icon 색상 변경 로직 - jhm 2022/08/16
            // 현재 접힌 상태에서의 BottomSheet 귀퉁이의 둥글기 저장
            val cornerRadius = bottomSheet.radius
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.setCardBackgroundColor(resources.getColor(R.color.c_ffffff))
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    if (newState == STATE_EXPANDED) {
                        LogUtil.logE("완전히 펼쳐진 상태")

                        // UI Setting 최종 - jhm 2022/09/14
                        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
                        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
                        setNaviBarIconColor(true) // 네비게이션 true : 검정색
                        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

                        binding.topLayout.setBackgroundColor(resources.getColor(R.color.c_ffffff))
                        Glide.with(applicationContext).load(R.drawable.arrow_left)
                            .into(binding.backImg)
                        Glide.with(applicationContext).load(R.drawable.share_icon)
                            .into(binding.shareImg)


                    } else if (newState == STATE_COLLAPSED) {
                        LogUtil.logE("접혀있는 상태")
                        binding.topLayout.setBackgroundColor(resources.getColor(R.color.trans))
                        Glide.with(applicationContext).load(R.drawable.arrow_left_white)
                            .into(binding.backImg)
                        Glide.with(applicationContext).load(R.drawable.share_icon_white)
                            .into(binding.shareImg)
                    }
                }

                override fun onSlide(bottomSheetView: View, slideOffset: Float) {
                    // slideOffset 접힘 -> 펼쳐짐: 0.0 ~ 1.0
                    if (slideOffset >= 0) {
                        // 둥글기는 펼칠수록 줄어들도록
                        binding.bottomSheet.radius = cornerRadius - (cornerRadius * slideOffset)
                        // 내용의 투명도도 같이 조절...
//                        binding.fragment.alpha = Math.min(slideOffset * 2F, 1F)
                    }
                }
            })
        }

        Glide.with(this@PortfolioDetailActivity)
            .load(portfolioPath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.portfolioImg)



        pvm.getPortfolioNotification(portfolioId)
        pvm.detailResponse.observe(this@PortfolioDetailActivity, Observer {
            if (it.data.notificationYn.isNotEmpty()) {
                LogUtil.logE("notificationYn ${it.data.notificationYn}")
                notificationYn = it.data.notificationYn
                if (notificationYn == "N") {
                    binding.unionLayout.visibility = View.VISIBLE
                    binding.notiView.visibility = View.VISIBLE
                    binding.notiRadius.background =
                        applicationContext.getDrawable(R.drawable.layout_round_border_eaecf0)
                } else {
                    binding.unionLayout.visibility = View.GONE
                    binding.notiView.visibility = View.GONE
                    binding.notiRadius.background =
                        applicationContext.getDrawable(R.drawable.noti_call)
                }
            }
        })

        vm.getPortfolioDetail(portfolioId)
        vm.detailResponse.observe(this@PortfolioDetailActivity, Observer {

            LogUtil.logE("portfolio Detail response : ${it.data.createdAt}")

            if (it.data.magazineId != null) {
                magazineId = it.data.magazineId.toString()
            }


            // 판매 현황 상태값 - jhm 2022/08/19
            when (it.data.recruitmentState) {
                "PRS0101" -> {
                    binding.notiRadius.visibility = View.VISIBLE
                    binding.notiView.visibility = View.VISIBLE
                    binding.unionLayout.visibility = View.VISIBLE
                    binding.portfolioBtn.visibility = View.VISIBLE
                    binding.buyPortfolioBtn.visibility = View.GONE
                    binding.allSellBtn.visibility = View.GONE

                    val year = it.data.recruitmentBeginDate.substring(0, 4)
                    val month = it.data.recruitmentBeginDate.substring(5, 7)
                    val day = it.data.recruitmentBeginDate.substring(8, 10)
                    val hour = it.data.recruitmentBeginDate.substring(11, 13)
                    val minute = it.data.recruitmentBeginDate.substring(14, 16)

                    val date: LocalDate =
                        LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                    val dayOfWeek: DayOfWeek = date.dayOfWeek

                    val dayFormat = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
                    val newDate = "$month.$day($dayFormat)$hour:$minute 오픈예정"

                    if (notificationYn == "N") {
                        binding.unionLayout.visibility = View.VISIBLE
                        binding.notiView.visibility = View.VISIBLE
                        binding.notiRadius.background =
                            applicationContext.getDrawable(R.drawable.layout_round_border_eaecf0)
                    } else {
                        binding.unionLayout.visibility = View.GONE
                        binding.notiView.visibility = View.GONE
                        binding.notiRadius.background =
                            applicationContext.getDrawable(R.drawable.noti_call)
                    }

                    binding.notiRadius.setOnClickListener {
                        LogUtil.logE("오픈 알림 신청 OnClick..")
                        binding.notiRadius.isSelected = !binding.notiRadius.isSelected

                        if (notificationYn == "N") {
                            response?.postPortfolioNotification(
                                "Bearer $accessToken",
                                deviceId,
                                memberId,
                                portfolioId
                            )?.enqueue(object : Callback<BaseDTO> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onResponse(
                                    call: Call<BaseDTO>,
                                    response: Response<BaseDTO>
                                ) {
                                    try {
                                        if (!response.body().toString().isEmpty()) {
                                            LogUtil.logE("포트폴리오 알림 설정 정보 success " + response.body()?.status)
                                            pvm.getPortfolioNotification(portfolioId)

                                            binding.unionLayout.visibility = View.GONE
                                            binding.notiView.visibility = View.GONE
                                            binding.notiRadius.background =
                                                applicationContext.getDrawable(R.drawable.noti_call)
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        LogUtil.logE("포트폴리오 알림 설정 정보 변경 catch Error! " + ex.message)
                                    }
                                }

                                override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                    t.printStackTrace()
                                    LogUtil.logE("포트폴리오 알림 설정 정보 변경 fail.. " + t.message)
                                }
                            })
                        }
                        // 포트폴리오 알림 설정 해제 - jhm 2022/11/03
                        else {
                            response?.deletePortfolioNotification(
                                "Bearer $accessToken",
                                deviceId,
                                memberId,
                                portfolioId
                            )?.enqueue(object : Callback<BaseDTO> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onResponse(
                                    call: Call<BaseDTO>,
                                    response: Response<BaseDTO>
                                ) {
                                    try {
                                        if (!response.body().toString().isEmpty()) {
                                            LogUtil.logE("포트폴리오 알림 설정 해제 Success")
                                            pvm.getPortfolioNotification(portfolioId)

                                            binding.unionLayout.visibility = View.VISIBLE
                                            binding.notiView.visibility = View.VISIBLE
                                            binding.notiRadius.background =
                                                applicationContext.getDrawable(R.drawable.layout_round_border_eaecf0)
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        LogUtil.logE("포트폴리오 알림 상태 해제 catch ${ex.message}")
                                    }
                                }

                                override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                    t.printStackTrace()
                                    LogUtil.logE("포트폴리오 알림 상태 해제 fail" + t.message)
                                }
                            })
                        }


                    }

                    // 알림 신청 말풍선 LAYOUT - jhm 2022/11/02
                    binding.closeIcon.setOnClickListener {
                        binding.unionLayout.visibility = View.GONE
                    }


                    binding.status.text = "오픈예정"
                    // 바텀 하단 오픈 예정일때 - jhm 2022/10/19
                    binding.portfolioBtn.text = newDate


                    binding.character.setAnimation("run_start.json")
                    binding.character.loop(false);


                }
                "PRS0102" -> {
                    binding.notiRadius.visibility = View.GONE
                    binding.notiView.visibility = View.GONE
                    binding.unionLayout.visibility = View.GONE
                    binding.portfolioBtn.visibility = View.GONE
                    binding.buyPortfolioBtn.visibility = View.VISIBLE
                    binding.allSellBtn.visibility = View.GONE


                    binding.buyPortfolioBtn.setOnClickListener {
                        // 로그인 전이라면 - jhm 2022/10/19
                        if (PrefsHelper.read("memberId", "").equals("")) {
                            val intent = Intent(
                                this@PortfolioDetailActivity,
                                LoginChkActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            // 로그인 되어있다면 - jhm 2022/10/19
                            val intent = Intent(
                                this@PortfolioDetailActivity,
                                PurchaseActivity::class.java
                            )
                            intent.putExtra(
                                "expectationProfitRate",
                                expectationProfitRate
                            ) // 포트폴리오 수익률 - jhm 2022/10/20
                            intent.putExtra(
                                "totalPieceVolume",
                                totalPieceVolume
                            ) // 현재 수량 - jhm 2022/10/20
                            intent.putExtra(
                                "minPurchaseAmount",
                                minPurchaseAmount
                            ) // 최소 주문 금액 - jhm 2022/10/20
                            intent.putExtra(
                                "maxPurchaseAmount",
                                maxPurchaseAmount
                            ) // 최대 주문 금액 - jhm 2022/10/20
                            intent.putExtra(
                                "portfolioTitle",
                                portfolioTitle
                            ) // 포트폴리오 이름 - jhm 2022/10/20
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }


                    startAnimation()

                    binding.status.text = "판매 중"
                    binding.character.setAnimation("run_looping.json")
                    binding.character.loop(true);
                    binding.character.playAnimation()
                    binding.character.addAnimatorListener(object :
                        Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            LogUtil.logE("start")
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            LogUtil.logE("end")
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            LogUtil.logE("cancel")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            LogUtil.logE("repeat")
                        }
                    })
                }
                "PRS0103" -> {
                    binding.notiRadius.visibility = View.GONE
                    binding.notiView.visibility = View.GONE
                    binding.unionLayout.visibility = View.GONE
                    binding.portfolioBtn.visibility = View.GONE
                    binding.buyPortfolioBtn.visibility = View.GONE
                    binding.allSellBtn.visibility = View.VISIBLE

                    binding.status.text = "조각완판"
                    val decimal = DecimalFormat("#,###")
                    var depositText: String = ""
                    depositText = decimal.format(it.data.recruitmentAmount.toInt())
                    binding.sellPrice.text = "$depositText 원"

                    binding.percent.text = it.data.recruitmentAmount.toInt()
                        .div(it.data.totalPieceVolume.toInt().times(100)).toString() + "%"

                    startAnimation()
//                        //오른쪽 이동 애니메이션
//                        val aniRight = TranslateAnimation(
//                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
//                            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
//                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
//                            TranslateAnimation.RELATIVE_TO_PARENT, 0f
//                        )
//                        aniRight.duration = 1000


                    binding.character.setAnimation("run_end.json")
                    binding.character.loop(false);
                    binding.character.playAnimation()
                    binding.character.addAnimatorListener(object :
                        Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            LogUtil.logE("start")
                            binding.characterLayout.animation =
                                AnimationUtils.loadAnimation(
                                    this@PortfolioDetailActivity,
                                    R.anim.character_move
                                )
                            // 애니메이션이 끝나도 위치 유지 : true - 유지 / false - 유지 안함 - jhm 2022/10/24
                            binding.characterLayout.animation.fillAfter = true
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            LogUtil.logE("end")
                            binding.characterLayout.animation =
                                AnimationUtils.loadAnimation(
                                    this@PortfolioDetailActivity,
                                    R.anim.character_move
                                )
                            binding.characterLayout.animation.fillAfter = true
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            LogUtil.logE("cancel")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            LogUtil.logE("repeat")
                        }
                    })

                    // 퍼센트 - jhm 2022/10/18
//                        binding.percentProgress.progress =
                }
                "PRS0104" -> binding.status.text = "매각대기"
                "PRS0105" -> binding.status.text = "매각진행"
                "PRS0106" -> {
                    binding.status.text = "매각완료"
                }
                "PRS0107" -> binding.status.text = "정산중"
                "PRS0108" -> binding.status.text = "분배완료"
                "PRS0109" -> binding.status.text = "일시중지"
                "PRS0110" -> binding.status.text = "기한만료"
                "PRS0111" -> binding.status.text = "수익분배"
            }

            binding.characterLayout.bringToFront()

            // 총 판매 금액 - jhm 2022/08/19
            val amount = it.data.recruitmentAmount
            val toLongAmount: Long = amount.toLong()
            LogUtil.logE("toLong " + ConvertMoney().getNumKorString(toLongAmount))
            binding.price.text = ConvertMoney().getNumKorString(toLongAmount) + "만원"


            // 판매 기간 - jhm 2022/08/19
            val startDate = it.data.recruitmentBeginDate
            val startFormatDate = getStartFormatDate(startDate)
            val endFormatDate = getEndDateFormatDate(startDate)




            binding.date.text = "$startFormatDate ~ $endFormatDate"


            // 구매 포인트 RecyclerView 연결 - jhm 2022/08/18
            vm.viewInitHorizontal(binding.purchaseGuidesRv, 1)


            // 판매 정보 - jhm 2022/08/19
            // 판매 정보 - 운용 기간 - jhm 2022/08/19
            val operDate = DateBetweenEndAndStart(
                it.data.recruitmentBeginDate,
                it.data.dividendsExpecatationDate
            )
            var yearFormat = ""
            if (operDate < 365.toString()) {
                yearFormat = operDate + "일"
                LogUtil.logE("yearFormat $yearFormat")

            } else if (operDate >= 365.toString()) {
                yearFormat = "1년"
            }
            binding.operDate.text = yearFormat


            // 판매 정보 - 예상 수익률 - jhm 2022/08/19
            binding.rateText.text = it.data.expectationProfitRate + "%"


            // 구매화면으로 넘겨줄 데이터 - jhm 2022/10/21
            expectationProfitRate = it.data.expectationProfitRate // 수익률 - jhm 2022/10/21
            totalPieceVolume = it.data.totalPieceVolume // 현재 구매가능 갯수 - jhm 2022/10/21
            minPurchaseAmount = it.data.minPurchaseAmount // 최소 구매 가능 금액 - jhm 2022/10/21
            maxPurchaseAmount = it.data.maxPurchaseAmount // 최대 구매 가능 금액 - jhm 2022/10/21


            // 판매 정보 - 판매 수량 - jhm 2022/08/19
            binding.amountText.text = it.data.totalPieceVolume + " 피스"


            // 판매 정보 - 판매 단위 - jhm 2022/08/19
            binding.unitText.text = StringDiv(
                it.data.recruitmentAmount.toInt(),
                it.data.totalPieceVolume.toInt()
            ).toString() + " 원"


            // 안정성 등급 점수 String -> Int 형변환 후 set - jhm 2022/08/18
            val stabilityPoint = it.data.stabilityPoint
            val IntStaPoint: Int = stabilityPoint.toInt()

            // 환금성 등급 점수 String -> Int 형변환 후 set - jhm 2022/08/18
            val cashabilityPoint = it.data.cashabilityPoint
            val IntCashPoint = cashabilityPoint.toInt()

            // 수익성 등급 점수 String -> Int 형 변환 후 set - jhm 2022/08/18
            val profitabilityPoint = it.data.profitabilityPoint
            val IntProPoint = profitabilityPoint.toInt()

            // 포트폴리오 등급 set text - jhm 2022/08/19
            binding.grade.text = it.data.generalGrade + "등급"

            // 종합등급 progressbar setting - jhm 2022/08/19
            binding.progressbar1.progress = IntStaPoint
            binding.progressbar2.progress = IntCashPoint
            binding.progressbar3.progress = IntProPoint
            binding.score1.text = stabilityPoint
            binding.score2.text = cashabilityPoint
            binding.score3.text = profitabilityPoint


            // 포트폴리오 구성 - jhm 2022/08/21
            binding.infoTitle.text = it.data.title // 포트폴리오 제목 - jhm 2022/08/21
            portfolioTitle = it.data.title
            vm.viewInitVertical(binding.productsRv) // 포트폴리오 구성 - jhm 2022/08/22


            // 총 판매 금액 - jhm 2022/08/22
            binding.allAmount.text = ConvertMoney().getNumKorString(toLongAmount) + "만원"


            // 구매 가능 금액 - jhm 2022/08/22
            val maxAmount = it.data.maxPurchaseAmount
            val toLongMaxAmout: Long = maxAmount.toLong()
            binding.purchaseAmount.text =
                "최소 " + it.data.minPurchaseAmount + "원 ~ 최대 " + ConvertMoney().getNumKorString(
                    toLongMaxAmout
                ) + "만 원"
            var pieceAmount =
                StringDiv(
                    it.data.recruitmentAmount.toInt(),
                    it.data.totalPieceVolume.toInt()
                )
            var min = StringDiv(it.data.minPurchaseAmount.toInt(), pieceAmount)
            var max = StringDiv(it.data.maxPurchaseAmount.toInt(), pieceAmount)
            binding.purchaseAmountCount.text = "최소 " + min + "피스 ~ 최대 " + max + "피스"

            // 운용기간 - jhm 2022/08/22
            binding.operDateDetail.text = "$yearFormat (조기 분배 가능)"

            // 포트폴리오 구성 RecyclerView 연결 - jhm 2022/08/18
            //vm.viewInitHorizontal(binding.compositionRv, 2)

            // 포트폴리오 구성 썸네일 있는부분 - jhm 2022/08/22
            // 썸네일 radius - jhm 2022/08/24
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(
                CenterCrop(),
                RoundedCorners(20)
            )
            for (i in 0 until it.data.products.size) {
                Glide.with(applicationContext)
                    .load(it.data.products[i].representThumbnailImagePath)
                    .apply(requestOptions)
                    .into(binding.imagePath)
                binding.productionYear.text = it.data.products[i].productionYear
                binding.productTitle.text = it.data.products[i].title
                binding.author.text = it.data.products[i].author
                binding.productMaterial.text = it.data.products[i].productMaterial
                binding.productSize.text = it.data.products[i].productSize
            }

            // 포트폴리오 구성 - jhm 2022/08/25
            val adapter =
                PortfolioDetailCompositionAdapter(vm, context = applicationContext)
            binding.compositionRv.adapter = adapter
            binding.compositionRv.layoutManager =
                LinearLayoutManager(application, RecyclerView.HORIZONTAL, false)

            adapter.setItemClickListener(object :
                PortfolioDetailCompositionAdapter.OnItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(v: View, position: Int) {
                    LogUtil.logE("position $position")

                    Glide.with(applicationContext)
                        .load(it.data.products[position].representThumbnailImagePath)
                        .apply(requestOptions)
                        .into(binding.imagePath)
                    binding.productionYear.text = it.data.products[position].productionYear
                    binding.productTitle.text = it.data.products[position].title
                    binding.author.text = it.data.products[position].author
                    binding.productMaterial.text =
                        it.data.products[position].productMaterial
                    binding.productSize.text = it.data.products[position].productSize

                    adapter.notifyDataSetChanged()
                }
            })

            val documentAdapter =
                PortfolioDetailEvidenceAdapter(vm, context = applicationContext)
            binding.evidenceRv.adapter = documentAdapter
            binding.evidenceRv.layoutManager =
                LinearLayoutManager(application, RecyclerView.HORIZONTAL, false)

        })


        // 유의사항 text 정렬 - jhm 2022/08/25
        binding.contentText.text =
            SpannableStringBuilder(applicationContext.getText(R.string.portfolio_detail_notice_text)).apply {
                setSpan(IndentLeadingMarginSpan(), 0, length, 0)
            }


        // 포트폴리오 자세히 보기 - jhm 2022/10/18
        binding.iButton.setOnClickListener {
            LogUtil.logE("포트폴리오 자세히 보기 OnClick..")

            val intent = Intent(this@PortfolioDetailActivity, MagazineDetailWebView::class.java)
            intent.putExtra("magazineId", magazineId)
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            startActivity(intent)
        }

        // 하단 종모양 알림 lottieView - jhm 2022/10/18
        binding.notiView.setAnimation("alarm_set.json")
        binding.notiView.loop(true);
        binding.notiView.playAnimation()
        binding.notiView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                LogUtil.logE("start")
            }

            override fun onAnimationEnd(animation: Animator) {
                LogUtil.logE("end")
            }

            override fun onAnimationCancel(animation: Animator) {
                LogUtil.logE("cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                LogUtil.logE("repeat")
            }
        })


    }


    // progressbar animation - jhm 2022/10/19
    private fun startAnimation() {
        binding.run {
            val currentProgress = progress.progress
            getInterval().subscribe {
                progress.progress = currentProgress + it.toInt()
            }

        }
    }

    private fun getInterval(): Observable<Long> =
        Observable.interval(8L, TimeUnit.MILLISECONDS).map { interval ->
            LogUtil.logE("interval : $interval")
            interval + 1
        }.take(100)


    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winAttr = window.attributes
        winAttr.flags = if (on) winAttr.flags or bits else winAttr.flags and bits.inv()
        window.attributes = winAttr
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        LogUtil.logE("PortfolioDetailActivity onStart..")
    }

    // 포트폴리오 상세 상단 뒤로가기 화살표 onClick - jhm 2022/08/12
    fun onBackButton() {
        LogUtil.logE("뒤로가기 onClick..")
        finish()
    }

    // 포트폴리오 상세 상단 공유하기 onClick - jhm 2022/08/12
    fun onShareButton() {
        LogUtil.logE("공유하기 onClick..")
    }


    // 포트폴리오 판매 시작 날짜 format - jhm 2022/08/21
    @SuppressLint("SimpleDateFormat")
    fun getStartFormatDate(strDate: String): String {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")//old format
            val dateFormat2 = SimpleDateFormat("yyyy.MM.dd")//require new formate
            val objDate = dateFormat.parse(strDate)

            return dateFormat2.format(objDate)
        } catch (e: Exception) {
            return ""
        }
    }

    // 포트폴리오 판매 종료 날짜 format - jhm 2022/08/21
    fun getEndDateFormatDate(endDate: String): String {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")//old format
            val objDate = dateFormat.parse(endDate)

            // 시작날짜로부터 +3일 - jhm 2022/08/21
            val cal = Calendar.getInstance()
            cal.time = objDate
            val df: DateFormat = SimpleDateFormat("yyyy.MM.dd")
            cal.add(Calendar.DATE, +3)
            df.format(cal.time)

            return df.format(cal.time)
        } catch (e: Exception) {
            return ""
        }
    }


    private fun DateBetweenEndAndStart(start: String, end: String): String {
        try {
            val dateFormat = SimpleDateFormat("yyyyMMdd")

            val startDate = dateFormat.parse(start).time
            val endDate = dateFormat.parse(end).time
            val operDate = (endDate - startDate) / (24 * 60 * 60 * 1000)

            return operDate.toString()
        } catch (ex: Exception) {
            return ""
        }
    }

    // 판매단위 - jhm 2022/08/21
    fun StringDiv(amount: Int, volume: Int): Int {
        LogUtil.logE("amount : $amount")
        LogUtil.logE("volume : $volume")
        var result = (amount).div(volume)
        LogUtil.logE("result : $result")
        return result
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
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