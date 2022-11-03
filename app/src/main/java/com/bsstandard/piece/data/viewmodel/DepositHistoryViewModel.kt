package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.DepositHistoryDTO
import com.bsstandard.piece.data.repository.DepositHistoryRepository
import com.bsstandard.piece.view.adapter.deposit.DepositHistoryAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : DepositHistoryViewModel
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 회원 거래 내역 목록 조회 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 */


@HiltViewModel
class DepositHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: DepositHistoryRepository = DepositHistoryRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val historyResponse: MutableLiveData<DepositHistoryDTO> = MutableLiveData()

    @SuppressLint("CheckResult")
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    private val hitoryList: ArrayList<DepositHistoryDTO.Data.Result> = arrayListOf()
    var depositHistoryAdapter = DepositHistoryAdapter(this, context)
    var n = 1

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getDepositHistory(
        accessToken: String,
        deviceId: String,
        memberId: String,
        changeReason: String,
        length: Int
    ) {
        repo.getDepositHistory(accessToken, deviceId, memberId, changeReason,length).subscribe(
            { DepositHistoryDTO ->
                LogUtil.logE("회원 거래 내역 조회 갯수 : " + DepositHistoryDTO.data.totalCount)


                hitoryList.clear()
                for (i in ArrayList(DepositHistoryDTO.data.result).indices) {
                    hitoryList.add(DepositHistoryDTO.data.result[i])

                    depositHistoryAdapter.notifyDataSetChanged()
                }
            }, { throwable ->
                LogUtil.logE("회원 거래 내역 조회 GET List Error ! " + throwable.message)
            }
        )
    }


    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = depositHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getDepositHistoryItem(): List<DepositHistoryDTO.Data.Result> {
        return hitoryList;
    }

}

object BindingDepositHistoryAdapter {
    @BindingAdapter("app:changeReason")
    @JvmStatic
    fun loadImg(imageView: ImageView, status: String) {

        val status = status
        var statusImgPath: String = ""

        LogUtil.logE("status : $status")
        when (status) {
            "MDR0101" -> statusImgPath =
                getURLForResource(R.drawable.balance_1) // 예치금 입금 - jhm 2022/09/28
            "MDR0102" -> statusImgPath =
                getURLForResource(R.drawable.balance_2) // 예치금 출금 완료 - jhm 2022/09/28
            "MDR0103" -> statusImgPath =
                getURLForResource(R.drawable.balance_3) // 분배금 입금 - jhm 2022/09/28
            "MDR0104" -> statusImgPath =
                getURLForResource(R.drawable.balance_7) // 분배 수수료 - jhm 2022/09/28
            "MDR0105" -> statusImgPath =
                getURLForResource(R.drawable.balance_5) // 분배금 입금(실명인증 필요) - jhm 2022/09/28
            "MDR0201" -> statusImgPath =
                getURLForResource(R.drawable.balance_6) // 조각 구매 - jhm 2022/09/28
            "MDR0202" -> statusImgPath =
                getURLForResource(R.drawable.balance_4) // 구매 취소 - jhm 2022/09/28
            "MDR0203" -> statusImgPath =
                getURLForResource(R.drawable.balance_5) // 조각 판매 - jhm 2022/09/28
            "MDR0204" -> statusImgPath =
                getURLForResource(R.drawable.balance_9) // 부가가치세 지불 - jhm 2022/09/28
            "MDR0205" -> statusImgPath =
                getURLForResource(R.drawable.balance_8) // 부가가치세 환불 - jhm 2022/09/28
            "MDR0301" -> statusImgPath =
                getURLForResource(R.drawable.balance_10) // 예치금 출금 신청 - jhm 2022/09/28
        }

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(imageView.context)
            .load(statusImgPath)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:changeReasonName"
    )
    @JvmStatic
    fun loadName(
        textView: TextView,
        reasonName: String
    ) {
        textView.text = reasonName
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:createdAt",
        "app:changeReasonDetail"
    )
    @JvmStatic
    fun loadDate(
        textView: TextView,
        createdAt: String,
        changeReasonDetail: String?
    ) {

        val year = createdAt.substring(0, 4)
        val month = createdAt.substring(5, 7)
        val day = createdAt.substring(8, 10)
        val hour = createdAt.substring(11, 13)
        val minute = createdAt.substring(14, 16)
        val date: LocalDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        val dayOfWeek: DayOfWeek = date.dayOfWeek
        val dayFormat = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
        val newDate = month + "월" + day + "일"

        val detail = changeReasonDetail

        if(detail == null){
            textView.text = newDate
        } else {
            textView.text = "$newDate | $detail"
        }

    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:changeAmount"
    )
    @JvmStatic
    fun loadAmount(
        textView: TextView,
        changeAmount: String
    ) {
        LogUtil.logE("changeAmount.substring(0,1) : " + changeAmount.substring(0,1))
        if(changeAmount.substring(0,1) == "-") {
            textView.setTextColor(R.color.c_131313)
        }
        else {
            textView.setTextColor(R.color.c_10cfc9)
        }

        textView.text = changeAmount
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:remainAmount"
    )
    @JvmStatic
    fun loadReAmount(
        textView: TextView,
        remainAmount: String
    ) {
        textView.text = remainAmount
    }


    // 회원 거래내역 조회시 상태값에대한 이미지를 불러오는 로직 (ex: 조각구매 , 분배금 입금 등.. ) - jhm 2022/09/28
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }


}