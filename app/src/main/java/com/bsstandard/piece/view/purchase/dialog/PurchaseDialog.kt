package com.bsstandard.piece.view.purchase.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.PurchaseConfirmBinding
import com.bsstandard.piece.view.passcode.PassCodeActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.purchase.bialog
 * fileName       : PurchaseDialog
 * author         : piecejhm
 * date           : 2022/10/21
 * description    : 주문하기전 주문 확인 내역 BottomSheetDialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/21        piecejhm       최초 생성
 */


class PurchaseDialog(context: Context) : BottomSheetDialogFragment() {
    private val mContext: Context = context
    lateinit var binding: PurchaseConfirmBinding;

    private var portfolioTitle:String = ""
    private var minPurchaseAmount: String = ""
    private var buyAmount: String = ""
    private var allPrice: String = ""



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)


        binding = PurchaseConfirmBinding.inflate(inflater, container , false)
        binding.lifecycleOwner = this


        portfolioTitle = arguments?.getString("portfolioTitle").toString()
        minPurchaseAmount = arguments?.getString("minPurchaseAmount").toString()
        buyAmount = arguments?.getString("buyAmount").toString()
        allPrice = arguments?.getString("allPrice").toString()

        val decimal = DecimalFormat("#,###")



        LogUtil.logE("portfolioTitle : $portfolioTitle")
        LogUtil.logE("minPurchaseAmount : $minPurchaseAmount")
        LogUtil.logE("buyAmount : $buyAmount")
        LogUtil.logE("allPrice : $allPrice")

        if(minPurchaseAmount == "10000") {
            minPurchaseAmount = "1"
        }
        else if (minPurchaseAmount == "20000") {
            minPurchaseAmount = "2"
        }
        else if (minPurchaseAmount == "30000") {
            minPurchaseAmount = "3"
        }
        binding.portfolioTitle.text = portfolioTitle
        binding.buyCount.text = "조각당 $minPurchaseAmount 만원"
        binding.count.text = decimal.format(buyAmount.toInt()).toString() + " 피스"
        binding.amount.text = decimal.format(allPrice.toInt()).toString() + " 원"



        // 닫기 - jhm 2022/10/21
        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.confirmBtn.setOnClickListener {
            LogUtil.logE("피스 구매하기 OnClick..")
            val intent = Intent(context, PassCodeActivity::class.java)
            intent.putExtra("Step","5")
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
            dismiss()
            activity?.finish()
        }

        return binding.root
    }
}