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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.dto.PortfolioDTO
import com.bsstandard.piece.data.repository.PortfolioRepository
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
import com.bsstandard.piece.widget.utils.DateConverter
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
 * fileName       : PortfolioViewModel
 * author         : piecejhm
 * date           : 2022/07/13
 * description    : 포트폴리오 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/13        piecejhm       최초 생성
 */

@HiltViewModel
class PortfolioViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PortfolioRepository = PortfolioRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var portfolioAdapter = PortfolioAdapter(this , context)
    private val portfolioList: ArrayList<PortfolioDTO.Data.Portfolio> = arrayListOf()
    var n = 1


    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getPortfolio(length: Int) {
        repo.getPortfolios(length = length).subscribe(
            { PortfolioDTO ->
                LogUtil.logE("포트폴리오 갯수 : " + PortfolioDTO.data.totalCount)

                portfolioList.clear()
                for (i in ArrayList(PortfolioDTO.data.portfolios).indices) {
                    portfolioList.add(PortfolioDTO.data.portfolios[i])

                    LogUtil.logE("포트폴리오 아이디 및 상태값 : " + portfolioList.get(i).portfolioId + portfolioList.get(i).recruitmentState )
                    portfolioAdapter.notifyDataSetChanged()
                }

            }, { throwable -> LogUtil.logE("포트폴리오 GET List Error!" + throwable.message) }
        )
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = portfolioAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getPortoflioItem(): List<PortfolioDTO.Data.Portfolio> {
        return portfolioList;
    }
}

object BindingAdapter {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImg(imageView: ImageView, url: String) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @BindingAdapter("app:recruitmentState")
    @JvmStatic
    fun loadState(imageView: ImageView, status: String) {
        val status = status
        var statusImgPath: String = ""

//        LogUtil.logE("포트폴리오 상태값 : $status")

//        if(status == "PRS0102") {
//           statusImgPath = getURLForResource(R.drawable.prs0102) // 판매중 - jhm 2022/07/17
//        }
        when (status) {
            "PRS0101" -> {
                statusImgPath =
                    getURLForResource(R.drawable.prs0101)
            }
            // 오픈예정 - jhm 2022/07/17
            "PRS0102" -> {
                statusImgPath =
                    getURLForResource(R.drawable.prs0102)
            }
            // 판매중 - jhm 2022/07/17
            "PRS0103" -> {
                statusImgPath =
                    getURLForResource(R.drawable.prs0103)
            }
            // 조각 완판 - jhm 2022/07/17
            "PRS0104" -> {
                LogUtil.logE("매각 대기")
            }
            "PRS0105" -> {
                LogUtil.logE("매각 진행")
            }
            "PRS0106" -> statusImgPath =
                getURLForResource(R.drawable.prs0106) // 매각 완료 - jhm 2022/07/17
            "PRS0107" -> LogUtil.logE("정산중")
            "PRS0108" -> LogUtil.logE("분배완료")
            "PRS0109" -> statusImgPath =
                getURLForResource(R.drawable.prs0109) // 일시 중지 - jhm 2022/07/17
            "PRS0110" -> statusImgPath =
                getURLForResource(R.drawable.prs0110) // 기한 만료 - jhm 2022/07/17
            "PRS0111" -> statusImgPath =
                getURLForResource(R.drawable.prs0111) // 수익 분배 - jhm 2022/07/17
        }


        Glide.with(imageView.context)
            .load(statusImgPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:recruitmentBeginDate",
        "app:recruitmentState",
        "app:remainingPieceVolume",
        "app:soldoutAt",
        "app:expectationProfitRate"
    )
    @JvmStatic
    fun loadData(
        textView: TextView,
        date: String,
        status: String,
        volume: String,
        soldout: String?,
        percent: String
    ) {
        val recruitmentState = status
        val remainingPieceVolume = volume
        val soldOutAt = soldout
        val expectationProfitRate = percent

        val string = date;


        val year = string.substring(0, 4)
        val month = string.substring(5, 7)
        val day = string.substring(8, 10)
        val hour = string.substring(11, 13)
        val minute = string.substring(14, 16)

        val date: LocalDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        val dayOfWeek: DayOfWeek = date.dayOfWeek

        val dayFormat = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
        val newDate = "$month.$day($dayFormat)$hour:$minute 오픈"

        when (recruitmentState) {
            "PRS0101" -> textView.text = newDate // 오픈예정일때 오픈날짜와 시간을 set text 해준다. - jhm 2022/07/18
            "PRS0102" -> textView.text =
                "남은 수량 " + remainingPieceVolume + "피스" // 판매중일때 남은 조각 갯수를 set text 해준다. - jhm 2022/07/18
            "PRS0103" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 마감 시간 - jhm 2022/07/18
            "PRS0104" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 매각 대기 - jhm 2022/07/18
            "PRS0105" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 매각 진행 - jhm 2022/07/18
            "PRS0106" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 매각 완료 - jhm 2022/07/18
            "PRS0107" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 정산중 - jhm 2022/07/18
            "PRS0108" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 분배 완료 - jhm 2022/07/18
            "PRS0109" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 일시 중지 - jhm 2022/07/18
            "PRS0110" -> textView.text =
                soldOutAt?.let { getSoldOut(string, it) } // 기한 만료 - jhm 2022/07/18
            "PRS0111" -> textView.text = "수익률 $expectationProfitRate% 달성" // 수익 분배 - jhm 2022/07/18
        }
    }


    // 포트폴리오 조회시 상태값에대한 이미지를 불러오는 로직 (ex: 오픈예정 , 판매중 , 조각완판 등.. ) - jhm 2022/07/18
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }

    // 판매 마감시 시간 및 일자 계산 - jhm 2022/07/18
    private fun getSoldOut(startDate: String, endDate: String): CharSequence {
        val soldOutText = DateConverter.getUploadMinuteTime(
            DateConverter.getStringToTime(startDate),
            endDate,
            "MM/dd hh:mm"
        )
        return soldOutText
    }


}
