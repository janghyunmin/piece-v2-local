package com.bsstandard.piece.view.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.DepositBalanceViewModel
import com.bsstandard.piece.data.viewmodel.PurchaseViewModel
import com.bsstandard.piece.databinding.ActivityPurchaseBinding
import com.bsstandard.piece.view.deposit.DepositChargeActivity
import com.bsstandard.piece.view.deposit.Listener
import com.bsstandard.piece.view.purchase.dialog.PurchaseDialog
import com.bsstandard.piece.view.withdrawal.NumberLiveViewModel
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *packageName    : com.bsstandard.piece.view.purchase
 * fileName       : PurchaseActivity
 * author         : piecejhm
 * date           : 2022/10/19
 * description    : 포트폴리오 구매 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/19        piecejhm       최초 생성
 */


// 키패드 UI 참고 , 예치금 입력, 출금 변경해놓기 - jhm 2022/10/21
@AndroidEntryPoint
class PurchaseActivity : AppCompatActivity(), Listener, Observer<String> {

    private val binding by lazy { ActivityPurchaseBinding.inflate(layoutInflater) }
    private val dvm by viewModels<DepositBalanceViewModel>()  // 출금 가능 금액 ViewModel - jhm 2022/09/29
    private val purchaseViewModel by viewModels<PurchaseViewModel>()
    private val nvm by viewModels<NumberLiveViewModel>()

    private val volumeList: ArrayList<Int> = arrayListOf()
    val mContext: Context = this@PurchaseActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    // 예치금 유무 판별 - jhm 2022/11/05

    private var depositYn: String = ""

    // 구매 수량 liveText - jhm 2022/10/21
    private var liveText: MutableLiveData<String> = MutableLiveData()

    // BottomSheetDialog 에 넘겨줄 구매 갯수 - jhm 2022/10/19
    var ownPiece: String = "0"
    var count = 0

    // last live amount - jhm 2022/10/06
    private val sb = StringBuilder()


    // 예상 수익률 * 10,000 - jhm 2022/10/20
    private var expectationProfitRate: String = ""
    private var totalPieceVolume: String = ""
    private var minPurchaseAmount: String = ""
    private var maxPurchaseAmount: String = ""
    private var portfolioTitle: String = ""
    private var remainingPieceVolume: String = "" // 구매 가능 수량 - jhm 2022/11/05


    val decimal = DecimalFormat("#,###")
    var depositBalance: String = ""


    var purchaseDialog: PurchaseDialog? = null
    var buyAmount: String? = "" // 주문 조각 수 - jhm 2022/10/21
    var allPrice: String? = "" // 총 주문 금액 - jhm 2022/10/21
    var myVolume: String? = "" // 이미 구매한 조각 수 - jhm 2022/11/05


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.apply {
            lifecycleOwner = this@PurchaseActivity
            numberPad = nvm
            depositVm = dvm
            purchaseVm = purchaseViewModel

            volumeList.clear()

            purchaseViewModel.getPurchaseList(accessToken, deviceId, memberId)
            purchaseViewModel.purchaseVolume.observe(this@PurchaseActivity, Observer {
                try {
                    var test : Int = 0
                    for(i in ArrayList(it).indices) {
                        volumeList.add(it[i])
                        myVolume = volumeList[i].toString()

                        LogUtil.logE("myVolume $myVolume")
                        test.plus(myVolume!!.toInt())
                        LogUtil.logE("test $test")

                    }
//                    if (it.isEmpty()) {
//                        LogUtil.logE("구매한 조각 수 없음")
//                        myVolume = "0"
//                    } else {
//                        myVolume = it.toString()
//                        LogUtil.logE("myVolue $myVolume")
//                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    LogUtil.logE("구매한 조각 수 없음")
                }
            })
        }

//        binding.lifecycleOwner = this@PurchaseActivity
//        binding.numberPad = nvm
//        binding.depositVm = dvm
//        binding.purchaseVm = purchaseViewModel


        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

//        dvm = ViewModelProvider(this@PurchaseActivity)[DepositBalanceViewModel::class.java]
//        nvm = ViewModelProvider(this@PurchaseActivity)[NumberLiveViewModel::class.java]
//        purchaseViewModel = ViewModelProvider(this@PurchaseActivity)[PurchaseViewModel::class.java]






        binding.backImg.setOnClickListener {
            finish()
        }

        // PortfolioDetailActivity -> 넘겨받은 데이터 - jhm 2022/10/20
        val intent = intent
        expectationProfitRate =
            intent.getStringExtra("expectationProfitRate").toString() // 수익률 - jhm 2022/10/21
        totalPieceVolume = intent.getStringExtra("totalPieceVolume").toString()
        minPurchaseAmount = intent.getStringExtra("minPurchaseAmount").toString()
        maxPurchaseAmount = intent.getStringExtra("maxPurchaseAmount").toString()
        portfolioTitle = intent.getStringExtra("portfolioTitle").toString()
        remainingPieceVolume = intent.getStringExtra("remainingPieceVolume").toString()
        purchaseDialog = PurchaseDialog(mContext)


        // 출금 가능 금액 - jhm 2022/09/30
        dvm.getDepositBalance(accessToken, deviceId, memberId)
        dvm.depoResponse.observe(this@PurchaseActivity, Observer {
            depositBalance = decimal.format(it.data.depositBalance)

            LogUtil.logE("출금 가능 금액 :$depositBalance")

            if (it.data.depositBalance == null || it.data.depositBalance.equals("")) {
                depositYn = "N"
                // 예치금이 없을때 예치금 충전 버튼 Visible - jhm 2022/10/20
                binding.depositGo.visibility = View.VISIBLE
                binding.depositGo.setOnClickListener {
                    LogUtil.logE("예치금 충전하기 OnClick..")

                    val intent = Intent(mContext, DepositChargeActivity::class.java)
                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    );
                    startActivity(intent)
                    finish()
                }
            } else {
                depositYn = "Y"
                binding.depositGo.visibility = View.GONE
                binding.myMoney.text = "예치금 $depositBalance 원"
            }

        })


        // 피스 입력 구매 LiveData 처리 - jhm 2022/10/21
        val pre_regular = ResourcesCompat.getFont(mContext, R.font.pretendard_regular)
        val pre_bold = ResourcesCompat.getFont(mContext, R.font.pretendard_bold)


        // 남은 수량  - jhm 2022/10/21
        binding.fTitle.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
        binding.fTitle.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
        binding.fTitle1.text = "남은 수량 "
        binding.fTitle.text = remainingPieceVolume
        binding.fTitle2.text = "피스"

        nvm.textAmount.observe(this, this)
        binding.confirmBtn.isSelected = false


        val decimal = DecimalFormat("#,###")
        var depositText: String = ""
        liveText.observe(this@PurchaseActivity, Observer {
            if (it.isNotEmpty()) {
                // 피스 조각 수 * 최소 주문 가능 금액 - jhm 2022/10/21
                binding.myMoney.typeface = pre_bold
                binding.myMoney.text =
                    decimal.format(it.toInt().times(minPurchaseAmount.toInt())).toString() + "원"


                // 만약 입력한 값이 1000 이상이라면 - jhm 2022/10/21
                if (it.toInt() > 1000) {
                    // Vibrator 객체
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
                    // 안드로이드 기본 제공 진동패턴효과 생성
                    val vibrationEffect =
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                    // 진동 두번 실행
                    vibrator.vibrate(vibrationEffect)

                    binding.fTitle.setTextColor(mContext.getColor(R.color.c_ff6060)) // 색상 변경 - jhm 2022/09/20
                    binding.fTitle.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20

                    binding.fTitle1.text = "최대"
                    binding.fTitle.text =
                        decimal.format(maxPurchaseAmount.toInt().div(minPurchaseAmount.toInt()))
                            .toString()
                    binding.fTitle2.text = "피스 까지 구매할 수 있어요."

                }
                // 입력한 값이 1000 이하라면 - jhm 2022/10/21
                else if (it.toInt() <= 1000) {
                    var changeProfit = expectationProfitRate.toInt().times(ownPiece.toInt())
                    var lastProfit = changeProfit.times(100)

                    binding.fTitle.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                    binding.fTitle.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20

                    binding.fTitle1.text = ""
                    binding.fTitle.text = decimal.format(lastProfit).toString() + "원"
                    binding.fTitle2.text = "의 수익이 예상돼요."
                }



                depositText = decimal.format(it.toInt())
                binding.title.text = "$depositText 피스"



                buyAmount = it.toInt().toString() // 주문내역으로 넘겨줄 총 주문 조각 수 - jhm 2022/10/21
                allPrice = it.toInt().times(minPurchaseAmount.toInt())
                    .toString() // 총 주문 금액 - jhm 2022/10/21
            } else {

                // 예치금 - jhm 2022/10/21
                binding.myMoney.text = "예치금 $depositBalance 원"

                // 남은 수량 - jhm 2022/10/21
                binding.fTitle.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.fTitle.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.fTitle1.text = "남은 수량 "
                binding.fTitle.text = remainingPieceVolume
                binding.fTitle2.text = "피스"

                sb.setLength(0) // string builder 초기화 - jhm 2022/10/21
                ownPiece = "" // 입력값 초기화 - jhm 2022/10/21
                binding.title.text = ""
                binding.title.hint = "몇 피스를 구매할까요?"
                binding.confirmBtn.isSelected = false
            }
        })

        // 1. Vibrator 객체를 얻어온 다음
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;

        /**
         * +1 ~ 최대 버튼 OnClick
         * */
        binding.plus1.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (ownPiece.toInt() == 0 || ownPiece == "0") {
                    ownPiece = ownPiece.toInt().plus(1).toString()
                    liveText.value = ownPiece
                } else {
                    ownPiece = ownPiece.toInt().plus(1).toString()
                    liveText.value = ownPiece
                }
            }

        }
        binding.plus5.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (ownPiece.toInt() == 0 || ownPiece == "0") {
                    ownPiece = ownPiece.toInt().plus(5).toString()
                    liveText.value = ownPiece
                } else {
                    ownPiece = ownPiece.toInt().plus(5).toString()
                    liveText.value = ownPiece
                }
            }
        }
        binding.plus10.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (ownPiece.toInt() == 0 || ownPiece == "0") {
                    ownPiece = ownPiece.toInt().plus(10).toString()
                    liveText.value = ownPiece
                } else {
                    ownPiece = ownPiece.toInt().plus(10).toString()
                    liveText.value = ownPiece
                }
            }
        }
        binding.plus50.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (ownPiece.toInt() == 0 || ownPiece == "0") {
                    ownPiece = ownPiece.toInt().plus(50).toString()
                    liveText.value = ownPiece
                } else {
                    ownPiece = ownPiece.toInt().plus(50).toString()
                    liveText.value = ownPiece
                }
            }

        }

        // 최대 구매 수량 로직 변경해야함 - jhm 2022/11/05
        binding.plusMax.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (ownPiece.toInt() == 0 || ownPiece == "0") {
                    ownPiece = ownPiece.toInt().plus(1000).toString()
                    liveText.value = ownPiece
                } else {
                    ownPiece = ownPiece.toInt().plus(1000).toString()
                    liveText.value = ownPiece
                }
            }

        }


        /**
         * 1~9 키패드 OnClick
         * **/
        binding.code1.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("1").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code2.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("2").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code3.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("3").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code4.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("4").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code5.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("5").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code6.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("6").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code7.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("7").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code8.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("8").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code9.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("9").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
                }
            }
        }
        binding.code0.setOnClickListener {
            if (depositYn == "N") {
                // 안드로이드 기본 제공 진동패턴효과 생성
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                // 진동 두번 실행
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
                if (sb.toString().length < 4) {
                    ownPiece = sb.append("0").toString()
                    liveText.value = ownPiece
                    binding.confirmBtn.isSelected = true
//                    if (sb.substring(0, ownPiece.length).equals("0")) {
//                        binding.confirmBtn.isSelected = false
//                    } else {
//                        ownPiece = sb.append("0").toString()
//                        liveText.value = ownPiece
//                        binding.confirmBtn.isSelected = true
//                    }
                }
            }
        }

        // 1자리씩 삭제 - jhm 2022/10/21
        binding.clear.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
            removeNumber()
        }

        // 초기화 버튼 - jhm 2022/10/21
        binding.clearText.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 100));
            // 예치금 - jhm 2022/10/21
            binding.myMoney.text = "예치금 $depositBalance 원"

            // 남은 수량 - jhm 2022/10/21
            // 남은 수량  - jhm 2022/10/21
            binding.fTitle.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
            binding.fTitle.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
            binding.fTitle1.text = "남은 수량 "
            binding.fTitle.text = remainingPieceVolume
            binding.fTitle2.text = "피스"

            sb.setLength(0) // string builder 초기화 - jhm 2022/10/21
            ownPiece = "0" // 입력값 초기화 - jhm 2022/10/21
            binding.title.text = ""
            binding.title.hint = "몇 피스를 구매할까요?"
            binding.confirmBtn.isSelected = false
        }


        // 확인 버튼 - jhm 2022/10/04
        binding.confirmBtn.setOnClickListener {
            if (binding.confirmBtn.isSelected) {
                LogUtil.logE("피스 구매 확인 버튼 OnClick..")

                PrefsHelper.write("portfolioTitle", portfolioTitle)
                PrefsHelper.write("purchaseVolume", buyAmount)

                LogUtil.logE("제목 : $portfolioTitle")

                val bundle = Bundle()
                bundle.putString("portfolioTitle", portfolioTitle) // 포트폴리오 제목 - jhm 2022/10/21
                bundle.putString(
                    "minPurchaseAmount",
                    minPurchaseAmount
                ) // 조각당 금액 단위 - jhm 2022/10/21
                bundle.putString("buyAmount", buyAmount) // 주문 조각 수 - jhm 2022/10/21
                bundle.putString("allPrice", allPrice) // 총 주문 금액 - jhm 2022/10/21


                purchaseDialog!!.arguments = bundle
                purchaseDialog!!.show(supportFragmentManager, "포트폴리오 주문내역")

            }
        }
    }

    // 마지막 입력값 제거 - jhm 2022/10/21
    fun removeLastNchars(str: String, n: Int): String {
        LogUtil.logE("str length : " + str.length)
        return str.substring(0, str.length - n)
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(t: String?) {
        LogUtil.logE("t $t")
    }


    // 뒤에서 부터 1자리씩 지우기 로직 - jhm 2022/10/21
    @SuppressLint("SetTextI18n")
    private fun removeNumber() {
        if (ownPiece.isNotEmpty()) {
            if (ownPiece.substring(0, 0) == "0") {
                LogUtil.logE("앞에 0먼저 오면 반응 x")
            } else {
                ownPiece = sb.substring(0, ownPiece.length - 1).toString()
                liveText.value = ownPiece
                LogUtil.logE("ownPiece $ownPiece")
            }
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

    override fun changeText(str: String?) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

fun replace(str: String?, target: String?, replacement: String?): String {
    return Pattern.compile(target, Pattern.LITERAL).matcher(str)
        .replaceAll(Matcher.quoteReplacement(replacement))
}
