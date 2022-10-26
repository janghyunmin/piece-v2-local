package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.*
import com.bsstandard.piece.databinding.FragmentWalletBinding
import com.bsstandard.piece.view.adapter.purchase.PurchaseAdapter
import com.bsstandard.piece.view.bank.BankSelectActivity
import com.bsstandard.piece.view.deposit.MyDepositActivity
import com.bsstandard.piece.view.fragment.dialog.ProfitBottomSheetDialog
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.purchase_item.*
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentWallet
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 내지갑 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */


@AndroidEntryPoint
class FragmentWallet : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    // 내지갑 ViewModel - jhm 2022/09/28
    private lateinit var wvm: WalletViewModel


    // 나의 예치금 잔액 viewModel - jhm 2022/09/28
    private lateinit var vm: DepositBalanceViewModel
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    // 회원 계좌 정보 조회 ViewModel - jhm 2022/10/04
    private lateinit var mavm: AccountViewModel


    // 내지갑 소유조각 ViewModel - jhm 2022/10/11
    private lateinit var pvm: PurchaseViewModel

    // 내 정보 조회 - jhm 2022/10/26
    private lateinit var mvm: GetUserViewModel
    private var profitBottomSheetDialog: ProfitBottomSheetDialog? = null


    private var disposable: Disposable? = null

    companion object {
        fun newInstance(): FragmentWallet {
            return FragmentWallet()
        }
    }


    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false)


        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        wvm = WalletViewModel()
        binding.walletViewModel = wvm
        binding.lifecycleOwner = viewLifecycleOwner


        // 내지갑 - 나의 예치금 잔액 - jhm 2022/10/11
        vm = ViewModelProvider(this)[DepositBalanceViewModel::class.java]
        vm.getDepositBalance(accessToken = accessToken, deviceId = deviceId, memberId = memberId)
        val decimal = DecimalFormat("#,###")
        var depositText: String = ""


        // 내지갑 - 소유조각 ViewModel - jhm 2022/10/11
        pvm = ViewModelProvider(this)[PurchaseViewModel::class.java]
        pvm.getPurchaseList(accessToken, deviceId, memberId)
        binding.purchaseVm = pvm

        mvm = ViewModelProvider(this)[GetUserViewModel::class.java]
        mvm.getUserData()


        // 분배금 정산 상태값 확인 ( MIC0102 , MIC0105 ) - jhm 2022/10/26
        mvm.isIdNo.observe(viewLifecycleOwner, Observer { it ->
            LogUtil.logE("it : $it")
            if (it.equals("MIC0102") || it.equals("MIC0105")) {
                mvm.totalProfitAmount.observe(viewLifecycleOwner, Observer {
                    if(profitBottomSheetDialog == null){
                        profitBottomSheetDialog = ProfitBottomSheetDialog()

                        val data = Bundle()
                        data.putString("totalProfitAmount", it)
                        profitBottomSheetDialog!!.arguments = data
                        profitBottomSheetDialog!!.show(childFragmentManager, "profitDialog")

                    }

                })
            }
        })


//        binding.run {
//            /** android Kotlin 코루틴 사용법 - jhm 2022/10/14 **/
//            GlobalScope.launch {
//                delay(500)
//
//            }
//        }

        activity!!.runOnUiThread {
            pvm.purchaseResponse.observe(viewLifecycleOwner, Observer {
                if (pvm.purchaseResponse.value == null) {
                    binding.noPurchaseItem.visibility = View.VISIBLE
                    binding.purchaseRv.visibility = View.GONE
                } else {

                    binding.noPurchaseItem.visibility = View.GONE
                    binding.purchaseRv.visibility = View.VISIBLE

                    pvm.viewInit(binding.purchaseRv)

                    pvm.purchaseAdapter.setOnItemClickListener(object :
                        PurchaseAdapter.OnItemClickListener {
                        override fun onItemClick(
                            v: View,
                            purchaseAt: String,
                            purchasePieceVolme: String,
                            purchaseTotalAmount: String,
                            purchaseId: String,
                            portfolioId: String,
                            portfolioImagePath: String,
                            isCoupon: String,
                            isConfirm: String
                        ) {

                            pvm.getPurchaseList(
                                accessToken,
                                deviceId,
                                memberId
                            ) // 소유조각 - jhm 2022/10/26


                            LogUtil.logE("소유조각 OnClick..")
                            LogUtil.logE("purchaseAt $purchaseAt")
                            LogUtil.logE("purchasePieceVolme $purchasePieceVolme")
                            LogUtil.logE("purchaseTotalAmount $purchaseTotalAmount")
                            LogUtil.logE("purchaseId $purchaseId")
                            LogUtil.logE("portfolioId $portfolioId")
                            LogUtil.logE("portfolioImagePath $portfolioImagePath")


                            val extras = FragmentNavigatorExtras(
                                thumnailPath to id.toString()
                            )
                            val action = FragmentWalletDirections.navToPurchaseDetailActivity(
                                purchaseAt,
                                purchasePieceVolme,
                                purchaseTotalAmount,
                                purchaseId,
                                portfolioId,
                                portfolioImagePath,
                                isCoupon,
                                isConfirm,
                            )
                            findNavController().navigate(action, extras)

                        }
                    })
                }
            })
        }


        // 나의 예치금 잔액 - jhm 2022/10/07
        vm.depoResponse.observe(viewLifecycleOwner, Observer {
            //binding.deposit.text =  decimal.format(it.data.depositBalance.toString()) + " 원"
            depositText = decimal.format(it.data.depositBalance)
            binding.deposit.text = "$depositText 원"
        })

        myDeposit() // 내지갑 상세 - jhm 2022/09/28


        mavm = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.memberAccountVm = mavm

        // 등록된 계좌 정보 조회 - jhm 2022/10/07
        mavm.getAccount(accessToken, deviceId, memberId)
        mavm.accountResponse.observe(viewLifecycleOwner, Observer {
            try {
                if (it.data == null) {
                    LogUtil.logE("등록된 계좌 없음")
                    binding.accountRegister.visibility = View.VISIBLE
                    binding.accountRegister.setOnClickListener {
                        LogUtil.logE("계좌 등록 OnClick..")
                        val intent = Intent(context, BankSelectActivity::class.java)
                        activity?.overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        );
                        context?.startActivity(intent)


                    }
                } else {
                    LogUtil.logE("등록된 계좌 있음")
                    binding.accountRegister.visibility = View.GONE

                    binding.changeAccount.setOnClickListener {
                        LogUtil.logE("계좌 변경 OnClick..")
                        val intent = Intent(context, BankSelectActivity::class.java)
                        activity?.overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        );
                        context?.startActivity(intent)


                    }


                    var statusIcon: String = ""   // 은행 아이콘 - jhm 2022/10/07
                    var bankName: String = "" // 은행 이름 - jhm 2022/10/07
                    var accountNo: String = "" // 은행 계좌번호 - jhm 2022/10/07

                    bankName = it.data.bankName
                    accountNo = it.data.accountNo

                    binding.bankName.text = bankName
                    binding.accountNum.text = accountNo


                    when (it.data.bankCode) {
                        "001" -> {
                            LogUtil.logE("한국 은행")
                        }
                        "002" -> {
                            LogUtil.logE("KDB 산업은행")
                            statusIcon = getURLForResource(R.drawable.bank02_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_084088))
                        }
                        "003" -> {
                            LogUtil.logE("기업은행")
                            statusIcon = getURLForResource(R.drawable.bank03_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_145BA9))
                        }
                        "004" -> {
                            LogUtil.logE("국민은행")
                            statusIcon = getURLForResource(R.drawable.bank04_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_776C61))
                        }
                        "005" -> {
                            LogUtil.logE("KEB 하나은행")
                            statusIcon = getURLForResource(R.drawable.bank05_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_008375))
                        }
                        "007" -> {
                            LogUtil.logE("수협은행")
                            statusIcon = getURLForResource(R.drawable.bank07_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_016EBD))
                        }
                        "008" -> {
                            LogUtil.logE("수출입 은행")
                        }
                        "011" -> {
                            LogUtil.logE("NH농협은행")
                            statusIcon = getURLForResource(R.drawable.bank11_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_1FB25A))
                        }
//                        "012" -> {
//                            LogUtil.logE("지역 농축협")
//                        }
                        "020" -> {
                            LogUtil.logE("우리은행")
                            statusIcon = getURLForResource(R.drawable.bank20_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_1898D6))
                        }

                        // 외환은행은 KEB 하나은행으로 통합 - jhm 2022/10/07
//                    "021" -> {
//                        LogUtil.logE("외환은행")
//                        statusIcon = getURLForResource(R.drawable.bank21)
//                    }
                        "023" -> {
                            LogUtil.logE("SC제일은행")
                            statusIcon = getURLForResource(R.drawable.bank23_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_0473EA))
                        }
                        "026" -> {
                            LogUtil.logE("신한은행")
                            statusIcon = getURLForResource(R.drawable.bank26_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_0079C1))
                        }
                        "027" -> {
                            LogUtil.logE("한국씨티은행")
                            statusIcon = getURLForResource(R.drawable.bank27_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_003B70))
                        }
                        "031" -> {
                            LogUtil.logE("대구은행")
                            statusIcon = getURLForResource(R.drawable.bank31_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_007DC5))
                        }
                        "032" -> {
                            LogUtil.logE("부산은행")
                            statusIcon = getURLForResource(R.drawable.bank32_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_D81920))
                        }
                        "034" -> {
                            LogUtil.logE("광주은행")
                            statusIcon = getURLForResource(R.drawable.bank34_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_1A3282))
                        }
                        "035" -> {
                            LogUtil.logE("제주은행")
                            statusIcon = getURLForResource(R.drawable.bank35_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_0079C1))
                        }
                        "037" -> {
                            LogUtil.logE("전북은행")
                            statusIcon = getURLForResource(R.drawable.bank37_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_1A3282))
                        }
                        "039" -> {
                            LogUtil.logE("경남은행")
                            statusIcon = getURLForResource(R.drawable.bank39_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_D81920))
                        }
                        "045" -> {
                            LogUtil.logE("새마을 금고")
                            statusIcon = getURLForResource(R.drawable.bank45_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_31B6E8))
                        }
                        "047" -> {
                            LogUtil.logE("신협")
                            statusIcon = getURLForResource(R.drawable.bank47_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_295CB3))
                        }
                        "064" -> {
                            LogUtil.logE("산림조합중앙회")
                            statusIcon = getURLForResource(R.drawable.bank64_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_42B191))
                        }
                        "071" -> {
                            LogUtil.logE("우체국")
                            statusIcon = getURLForResource(R.drawable.bank71_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_EE2722))
                        }
                        "089" -> {
                            LogUtil.logE("케이뱅크")
                            statusIcon = getURLForResource(R.drawable.bank89_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_DD5471))
                        }
                        "090" -> {
                            LogUtil.logE("카카오 뱅크")
                            statusIcon = getURLForResource(R.drawable.bank90_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_FFE300))
                        }
                        "092" -> {
                            LogUtil.logE("토스뱅크")
                            statusIcon = getURLForResource(R.drawable.bank92_rec)
                            binding.accountLayout.backgroundTintList =
                                ColorStateList.valueOf(binding.root.context.getColor(R.color.c_0064FF))
                        }
                    }

                    Glide.with(binding.root.context)
                        .load(statusIcon)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.bankIcon)
                }
            } catch (e: Exception) {
                LogUtil.logE("회원 계좌 정보 조회 Error ! ${e.printStackTrace()}")
            }
        })



        return binding.root
    }


    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtil.logE("onAttach")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.logE("onResume")

        vm.getDepositBalance(
            accessToken = accessToken,
            deviceId = deviceId,
            memberId = memberId
        ) // 예치금 잔액 - jhm 2022/10/26
        mavm.getAccount(accessToken, deviceId, memberId) // 등록된 계좌 - jhm 2022/10/26
        pvm.getPurchaseList(accessToken, deviceId, memberId) // 소유조각 - jhm 2022/10/26
    }


    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 나의 예치금 잔액 OnClick.. - jhm 2022/09/28
    private fun myDeposit() {
        wvm.startDeposit.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("나의 예치금 잔액 OnClick..")
            val intent = Intent(context, MyDepositActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
        }
    }

    // 은행 코드에 따른 은행 아이콘 이미지 load - jhm 2022/10/04
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
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