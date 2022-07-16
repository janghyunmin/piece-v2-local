package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:recruitmentBeginDate")
    @JvmStatic
    fun loadData(textView: TextView, date: String){
        val string = date;

        val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val timestamp = 1565209665.toLong() // timestamp in Long


        val timestampAsDateString = DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(timestamp))

        Log.d("parseTesting", timestampAsDateString) // prints 2019-08-07T20:27:45Z


        val date = LocalDate.parse(timestampAsDateString, secondApiFormat)

        Log.d("parseTesting", date.dayOfWeek.toString()) // prints Wednesday
        Log.d("parseTesting", date.month.toString()) // prints August

        textView.text = string
    }



}