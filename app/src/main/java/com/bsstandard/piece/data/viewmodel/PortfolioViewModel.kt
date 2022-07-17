package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
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
import com.bsstandard.piece.data.dto.portfolio.PortfolioDTO
import com.bsstandard.piece.data.repository.PortfolioRepository
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
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
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/13        piecejhm       최초 생성
 */

@HiltViewModel
class PortfolioViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PortfolioRepository = PortfolioRepository(application)
    private var portfolioAdapter = PortfolioAdapter(this)
    private val portfolioList: ArrayList<PortfolioDTO.Data.Datum> = arrayListOf()
    var n = 1


    @SuppressLint("CheckResult")
    fun getPortfolio() {
        repo.getPortfolios().subscribe(
            { PortfolioDTO ->
                LogUtil.logE("dto : " + PortfolioDTO.data.data.size)

                for (i in ArrayList(PortfolioDTO.data.data).indices) {
                    portfolioList.add(PortfolioDTO.data.data[i])
                    portfolioAdapter.notifyDataSetChanged()
                }

            }, { throwable -> LogUtil.logE("Error!" + "") }
        )
    }

    fun vewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = portfolioAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getPortoflioItem(): List<PortfolioDTO.Data.Datum> {
        return portfolioList;
    }


}

object BindingAdapter {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImg(imageView: ImageView, url: String){
        LogUtil.logE("url : $url")
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
//        var requestOptions = RequestOptions()
//        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))


        val status = status
        var statusImgPath: String = ""

        when(status) {
            "PRS0101" -> statusImgPath = getURLForResource(R.drawable.prs0101) // 오픈예정 - jhm 2022/07/17
            "PRS0102" -> statusImgPath = getURLForResource(R.drawable.prs0102) // 판매중 - jhm 2022/07/17
            "PRS0103" -> statusImgPath = getURLForResource(R.drawable.prs0103) // 조각 완판 - jhm 2022/07/17
            "PRS0104" -> LogUtil.logE("매각 대기")
            "PRS0105" -> LogUtil.logE("매각 진행")
            "PRS0106" -> statusImgPath = getURLForResource(R.drawable.prs0106) // 매각 완료 - jhm 2022/07/17
            "PRS0107" -> LogUtil.logE("정산중")
            "PRS0108" -> LogUtil.logE("분배완료")
            "PRS0109" -> statusImgPath = getURLForResource(R.drawable.prs0109) // 일시 중지 - jhm 2022/07/17
            "PRS0110" -> statusImgPath = getURLForResource(R.drawable.prs0110) // 기한 만료 - jhm 2022/07/17
            "PRS0111" -> statusImgPath = getURLForResource(R.drawable.prs0111) // 수익 분배 - jhm 2022/07/17
        }


        Glide.with(imageView.context)
            .load(statusImgPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:recruitmentBeginDate","app:recruitmentState")
    @JvmStatic
    fun loadData(textView: TextView, date: String ,status: String){
        val test = status;



        LogUtil.logE("status "+test)
        val string = date;

        LogUtil.logE("string " + string)
        val year = string.substring(0,4)
        val month = string.substring(5,7)
        val day = string.substring(8,10)
        val hour = string.substring(11,13)
        val minute = string.substring(14,16)

        val date: LocalDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        val dayOfWeek: DayOfWeek = date.dayOfWeek

        val dayFormat = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
        val newDate = month + "." + day + "(" + dayFormat + ")" + hour + ":" + minute + " 오픈"
        textView.text = newDate
    }


    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }


}