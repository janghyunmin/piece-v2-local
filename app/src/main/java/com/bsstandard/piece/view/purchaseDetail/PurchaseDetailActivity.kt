package com.bsstandard.piece.view.purchaseDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.purchase.PurchaseRmModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.dto.PortfolioDetailDTO
import com.bsstandard.piece.data.viewmodel.PurchaseConfirmViewModel
import com.bsstandard.piece.data.viewmodel.PurchaseViewModel
import com.bsstandard.piece.databinding.ActivityPurchaseDetailBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailCompositionAdapter
import com.bsstandard.piece.view.adapter.purchase.PurchaseItemCompositionAdapter
import com.bsstandard.piece.view.deed.DeedActivity
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailActivity
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel
import com.bsstandard.piece.widget.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *packageName    : com.bsstandard.piece.view.purchaseDetail
 * fileName       : PurchaseDetailActivity
 * author         : piecejhm
 * date           : 2022/10/11
 * description    : 소유조각 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/11        piecejhm       최초 생성
 */

@AndroidEntryPoint
class PurchaseDetailActivity :
    BaseActivity<ActivityPurchaseDetailBinding>(R.layout.activity_purchase_detail) {

    // 구매 취소시 - jhm 2022/10/25
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    // 구매 확정 요청 ViewModel - jhm 2022/10/25
    private val pcvm by viewModels<PurchaseConfirmViewModel>() // 사용자 포트폴리오 구매 확정 처리 - jhm 2022/10/25


    private val args: PurchaseDetailActivityArgs by navArgs()
    var mContext: Context = this@PurchaseDetailActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    var isCoupon: String = ""
    var isConfirm: String = ""

    var purchaseAt: String = ""
    var purchasePieceVolume: String = ""
    var purchaseTotalAmount: String = ""
    var purchaseId: String = ""
    var portfolioId: String = ""
    var portfolioImagePath: String = ""


    // 소유증서 Activity 로 넘겨줄 데이터 - jhm 2022/10/14
    var pieceVolume: String = "" // 총 조각 수 - jhm 2022/10/14
    var recruitmentAmount: String = "" // 총 구매 대금 - jhm 2022/10/14
    private val documentList: ArrayList<PortfolioDetailDTO.Data.Product> =
        arrayListOf()

    private var disposable: Disposable? = null
    private lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var pvm: PortfolioDetailViewModel
    var recruitmentEndDate: String = ""
    var btnChk: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@PurchaseDetailActivity
            binding.activity = this@PurchaseDetailActivity
            binding.portfolioDetailViewModel = portfolioDetailViewModel
            binding.purchaseVm = purchaseVm

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            pvm =
                ViewModelProvider(this@PurchaseDetailActivity)[PortfolioDetailViewModel::class.java]
            purchaseViewModel =
                ViewModelProvider(this@PurchaseDetailActivity)[PurchaseViewModel::class.java]
            purchaseViewModel.getPurchaseList(accessToken, deviceId, memberId)
            purchaseConfirmVm = pcvm

            // 바텀 버튼 분기 - jhm 2022/10/25
            // isCoupon , isConfirm 분기 - jhm 2022/10/25
            isCoupon = args.isCoupon
            isConfirm = args.isConfirm

            LogUtil.logE("isCoupon : $isCoupon")
            LogUtil.logE("isConfirm : $isConfirm")
        }

        postponeEnterTransition()
        purchaseAt = args.purchaseAt
        LogUtil.logE("넘겨받은 purchaseAt : $purchaseAt")
        val localDate = purchaseAt
        val year = localDate.substring(0, 4)
        val month = localDate.substring(5, 7)
        val day = localDate.substring(8, 10)

        val newDate = "$year.$month.$day"
        binding.date.text = newDate

        purchasePieceVolume = args.purchasePieceVolume
        LogUtil.logE("넘겨받은 purchasePieceVolume : $purchasePieceVolume")
        binding.volume.text = "$purchasePieceVolume PIECE"

        purchaseTotalAmount = args.purchaseTotalAmount
        LogUtil.logE("넘겨받은 purchaseTotalAmount : $purchaseTotalAmount")

        purchaseId = args.purchaseId
        LogUtil.logE("넘겨받은 purchaseId : $purchaseId")


        portfolioId = args.portfolioId
        LogUtil.logE("넘겨받은 portfolioId : $portfolioId")

        portfolioImagePath = args.portfolioImagePath
        LogUtil.logE("넘겨받은 portfolioImagePath $portfolioImagePath")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            var requestOptions = RequestOptions()
            requestOptions = requestOptions
                .transforms(CenterCrop(), RoundedCorners(20))



            Glide.with(this@PurchaseDetailActivity)
                .load(portfolioImagePath)
                .transition(DrawableTransitionOptions.withCrossFade(2000))
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
                        isFirstResource: Boolean,
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })

                .apply(requestOptions)
                .into(binding.portfolioImg)
        }


        // 구매 일자 - jhm 2022/10/12
        binding.purchaseDate.text = newDate

        // 조각 수 - jhm 2022/10/12
        binding.purchaseCount.text = "$purchasePieceVolume 피스"

        // 총 구매 금액 - jhm 2022/10/12
        binding.purchaseBuy.text = "$purchaseTotalAmount 원"

        // 소유주 - jhm 2022/10/12
        binding.purchaseProprietor.text = PrefsHelper.read("name", "")


        pvm.getPortfolioDetail(portfolioId)
        pvm.detailResponse.observe(this@PurchaseDetailActivity, Observer {


            // 포트폴리오 제목 - jhm 2022/10/13
            binding.infoTitle.text = it.data.title
            pvm.viewInitVertical(binding.productsRv) // 포트폴리오 구성 - jhm 2022/08/22

            // 포트폴리오 소유증서 제목 - jhm 2022/10/13
            binding.document.text = it.data.title

            // 총 판매 금액 - jhm 2022/08/19
            val amount = it.data.recruitmentAmount
            val toLongAmount: Long = amount.toLong()
            LogUtil.logE("toLong " + ConvertMoney().getNumKorString(toLongAmount))
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



            pieceVolume = it.data.totalPieceVolume
            recruitmentAmount = it.data.recruitmentAmount


            documentList.clear()
            for (i in 0 until it.data!!.products.size) {
                // 포트폴리오 증빙 자료 - jhm 2022/10/13
                LogUtil.logE("타이틀 : " + it.data.products[i].title)
                documentList.add(
                    it.data.products[i]
                )

                LogUtil.logE("cj  " + documentList[i].title)
            }

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
            // 운용기간 - jhm 2022/08/22
            binding.operDateDetail.text = "$yearFormat (조기 분배 가능)"


            // 포트폴리오 구성 - jhm 2022/08/25
            val adapter =
                PortfolioDetailCompositionAdapter(pvm, context = applicationContext)
            binding.compositionRv.adapter = adapter
            binding.compositionRv.layoutManager =
                LinearLayoutManager(application, RecyclerView.HORIZONTAL, false)

            adapter.setItemClickListener(object :
                PortfolioDetailCompositionAdapter.OnItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(v: View, position: Int) {
                    LogUtil.logE("position $position")


                    adapter.notifyDataSetChanged()
                }
            })


            // 증빙 자료 구성 - jhm 2022/10/12
            val docuAdapter =
                PurchaseItemCompositionAdapter(pvm, context = applicationContext)
            binding.documentRv.adapter = docuAdapter
            binding.documentRv.layoutManager =
                LinearLayoutManager(application, RecyclerView.HORIZONTAL, false)

            docuAdapter.setOnItemClickListener(object :
                PurchaseItemCompositionAdapter.OnItemClickListener {
                override fun onItemClick(v: View, documentImagePath: String) {
                    LogUtil.logE("증빙구성 이미지 OnClick.. $documentImagePath")
                    ImageDialogManager.getDialog(
                        this@PurchaseDetailActivity, documentImagePath,
                        object : ImageCloseListener {
                            override fun onClickCancelButton() {
                                LogUtil.logE("이미지 닫기 OnClick..")
                            }
                        }
                    )

                }
            })


            // 포트폴리오 마감 시간 - jhm 2022/10/25
            recruitmentEndDate = getStartFormatDate(it.data.recruitmentEndDate.toString())
            LogUtil.logE("마감 시간 : $recruitmentEndDate")

        })


        // 소유증서 OnClick - jhm 2022/10/13
        binding.itemInfoLayout2.setOnClickListener {
            LogUtil.logE("소유증서 OnClick..")

            val intent = Intent(mContext, DeedActivity::class.java)
            // 포트폴리오 제목 - jhm 2022/10/14
            intent.putExtra(
                "title",
                binding.infoTitle.text.toString()
            )
            intent.putExtra("purchaseId", purchaseId)

            intent.putExtra("size", documentList.size)
            LogUtil.logE("첫번쨰 사이즈 : ${documentList.size}")
            for (i in 0 until documentList.size) {
                intent.putExtra(
                    "subTitle$i",
                    documentList[i].title
                ) // 증빙자료 제목 - jhm 2022/10/14
                LogUtil.logE("test : " + documentList[i].title)
            }

            intent.putExtra(
                "purchasePieceVolume",
                purchasePieceVolume
            )

            // 구매 조각 수 - jhm 2022/10/14
            intent.putExtra(
                "purchaseTotalAmount",
                purchaseTotalAmount
            )// 구매 금액 - jhm 2022/10/14
            intent.putExtra("pieceVolume", pieceVolume)// 총 조각 수 - jhm 2022/10/14
            intent.putExtra(
                "recruitmentAmount",
                recruitmentAmount
            )// 총 조각 수 - jhm 2022/10/14
            intent.putExtra("purchaseAt", purchaseAt)// 구매 일자 - jhm 2022/10/14
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            startActivity(intent)
        }


        val now = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(now)
        LogUtil.logE("simpleDateFormat : $simpleDateFormat")


        val cmp = recruitmentEndDate.compareTo(simpleDateFormat)
        when {
            cmp > 0 -> {
                LogUtil.logE("%s is after %s $recruitmentEndDate $simpleDateFormat")
                btnChk = true
            }
            cmp < 0 -> {
                LogUtil.logE("%s is before %s $recruitmentEndDate $simpleDateFormat")
                btnChk = false
            }
            else -> {
                LogUtil.logE("equal")
                btnChk = false
            }
        }

        // 쿠폰으로 발급받은 조각이 아니거나 또는 구매 확정 버튼을 안눌렀을 경우 구매 취소 가능하게 처리 - jhm 2022/10/25
        if (btnChk || isConfirm == "N") {
            if (isCoupon == "Y") {
                binding.portfolioBtn.text = "포트폴리오 보러가기"
                binding.cancleBtn.visibility = View.GONE

                binding.portfolioBtn.setOnClickListener {
                    LogUtil.logE("포트폴리오 보러가기 $portfolioId")

                    val intent = Intent(mContext, PortfolioDetailActivity::class.java)
                    intent.putExtra("portfolioId", portfolioId)// 포트폴리오 Id
                    intent.putExtra(
                        "portfolioImagePath",
                        portfolioImagePath
                    )// 포트폴리오 image 경로
                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    );
                    startActivity(intent)
                }
            } else {
                LogUtil.logE("isConfirm : $isConfirm")
                LogUtil.logE("btnChk : $btnChk")


                binding.portfolioBtn.text = "구매 확정"
                // 구매취소 버튼 Visible - jhm 2022/10/25
                binding.cancleBtn.visibility = View.VISIBLE


                // 구매 확정 btn Listener
                val customDialogListener: CustomDialogListener =
                    object : CustomDialogListener {
                        override fun onCancelButtonClicked() {
                            // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                            LogUtil.logE("cancel btn")
                        }

                        override fun onOkButtonClicked() {
                            // 구매 확정  - jhm 2022/10/25
                            LogUtil.logE("구매 확정 API Call")
                            pcvm.putPurchaseConfirm(purchaseId, portfolioId, "Y")
                            pcvm.putPurchaseConfirmResponse.observe(
                                this@PurchaseDetailActivity,
                                Observer {
                                    LogUtil.logE("구매 확정 요청 Observer.. " + it.message)

                                    pvm.getPortfolioDetail(portfolioId)
                                })
                            DialogManager.openDalog(
                                mContext,
                                "구매가 확정되었어요.",
                                "좋은 소식으로 또 만나요!",
                                "확인",
                                this@PurchaseDetailActivity
                            )
                        }
                    }

                // 구매 취소 btn Listener
                val portfolioCancleBtnListener: CustomDialogListener =
                    object : CustomDialogListener {
                        override fun onCancelButtonClicked() {
                            // 닫기 - jhm 2022/07/04
                            LogUtil.logE("아니요")
                        }

                        override fun onOkButtonClicked() {
                            // 구매 취소  - jhm 2022/10/25
                            LogUtil.logE("구매취소 API Call..")

                            // 구매 취소시 필요 Model - jhm 2022/09/20
                            val purchaseRmModel = PurchaseRmModel(purchaseId)


                            response?.removePurchase(
                                "Bearer $accessToken",
                                deviceId = deviceId,
                                memberId = memberId,
                                purchaseRmModel
                            )?.enqueue(object : retrofit2.Callback<BaseDTO> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onResponse(
                                    call: Call<BaseDTO>,
                                    response: Response<BaseDTO>
                                ) {
                                    try {
                                        if (response.code() == 200) {
                                            LogUtil.logE("포트폴리오 구매 취소 성공")
                                            DialogManager.openDalog(
                                                mContext,
                                                "구매 취소 완료",
                                                "${binding.infoTitle.text} \n $purchasePieceVolume PIECE가 구매 취소되었어요.",
                                                "확인",
                                                this@PurchaseDetailActivity
                                            )
                                        } else {
                                            LogUtil.logE("포트폴리오 구매 취소 실패")
                                            finish()
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        LogUtil.logE("구매 취소 실패.." + ex.printStackTrace())
                                    }
                                }

                                override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                    LogUtil.logE("구매 취소 API 실패.." + t.message)
                                }
                            })
                        }
                    }

                binding.portfolioBtn.setOnClickListener {
                    LogUtil.logE("구매 확정 버튼 OnClick..")
                    DialogManager.openTwoBtnDialog(
                        mContext,
                        "구매를 확정할까요?",
                        "구매 확정 후에는 되돌릴 수 없습니다.",
                        customDialogListener,
                        "구매 확정"
                    )
                }

                binding.cancleBtn.setOnClickListener {
                    LogUtil.logE("구매 취소 OnClick..")
                    DialogManager.openTwoBtnNagativeDialog(
                        mContext,
                        "구매 취소",
                        "정말로 구매를 취소할까요?",
                        portfolioCancleBtnListener,
                        "구매 취소"
                    )
                }
            }
        } else {
            // 구매취소 버튼 Gone - jhm 2022/10/25
            binding.portfolioBtn.text = "포트폴리오 보러가기"
            binding.cancleBtn.visibility = View.GONE

            binding.portfolioBtn.setOnClickListener {
                LogUtil.logE("포트폴리오 보러가기 $portfolioId")

                val intent = Intent(mContext, PortfolioDetailActivity::class.java)
                intent.putExtra("portfolioId", portfolioId)// 포트폴리오 Id
                intent.putExtra("portfolioImagePath", portfolioImagePath)// 포트폴리오 image 경로
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                );
                startActivity(intent)
            }
        }
    }


    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }

    }

    // 판매단위 - jhm 2022/08/21
    fun StringDiv(amount: Int, volume: Int): Int {
        LogUtil.logE("amount : $amount")
        LogUtil.logE("volume : $volume")
        var result = (amount).div(volume)
        LogUtil.logE("result : $result")
        return result
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