package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.PurchaseDTO
import com.bsstandard.piece.data.repository.PurchaseRepository
import com.bsstandard.piece.view.adapter.purchase.PurchaseAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PurchaseViewModel
 * author         : piecejhm
 * date           : 2022/10/10
 * description    : 내지갑 - 소유조각 목록 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/10        piecejhm       최초 생성
 */

@HiltViewModel
class PurchaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PurchaseRepository = PurchaseRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private var _purchaseResponse : MutableLiveData<ArrayList<PurchaseDTO.Datum>> = MutableLiveData()
    val purchaseResponse: MutableLiveData<ArrayList<PurchaseDTO.Datum>> get() = _purchaseResponse

    @SuppressLint("CheckResult")
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")


    // 구매 목록 리스트 정보 - jhm 2022/10/10
    private val purchaseList: ArrayList<PurchaseDTO.Datum> = arrayListOf()
    var purchaseAdapter = PurchaseAdapter(this,context)

    private val volumeList: ArrayList<Int> = arrayListOf()
    private var _purchaseVolume: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    val purchaseVolume: MutableLiveData<ArrayList<Int>> get() = _purchaseVolume

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getPurchaseList(
        accessToken: String,
        deviceId: String,
        memberId: String
    ) {
        LogUtil.logE("accessToken : $accessToken")
        LogUtil.logE("deviceId : $deviceId")
        LogUtil.logE("memberId : $memberId")
        repo.getPurchaseList("Bearer $accessToken", deviceId, memberId).subscribe(
            {
                PurchaseDTO ->
                LogUtil.logE("회원 소유조각 리스트 갯수 : " +PurchaseDTO.data.size)

                purchaseList.clear()
                for(i in ArrayList(PurchaseDTO.data).indices) {
                    purchaseList.add(PurchaseDTO.data[i])
                    LogUtil.logE("purchaseAt : " + PurchaseDTO.data[i].purchaseAt)
                    LogUtil.logE("purchasePieceVolume : " + PurchaseDTO.data[i].purchasePieceVolume)
                    LogUtil.logE("purchase Img path1 : " + PurchaseDTO.data[i].portfolio.representThumbnailImagePath)
                    LogUtil.logE("purchase Img path2 : " + purchaseList[i].portfolio.representThumbnailImagePath)


                    LogUtil.logE("purchase isCoupon : " + PurchaseDTO.data[i].isCoupon)
                    LogUtil.logE("purchase isConfirm : " + PurchaseDTO.data[i].isConfirm)

                    _purchaseResponse.value = purchaseList
                    _purchaseResponse.postValue(purchaseList)


                    // 포트폴리오 구매시 구매 제한 카운팅 때문에 사용 - jhm 2022/11/05
                    volumeList.add(PurchaseDTO.data[i].purchasePieceVolume)
                    _purchaseVolume.value = volumeList
                    _purchaseVolume.postValue(volumeList)


                    purchaseAdapter.notifyDataSetChanged()

                }
            }, {
                throwable ->
                LogUtil.logE("회원 소유조각 조회 Error ! " + throwable.message )
            }
        )
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = purchaseAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication(),RecyclerView.HORIZONTAL, false)
    }

    fun getPurchaseItem(): List<PurchaseDTO.Datum> {
        return purchaseList;
    }
}

// 소유조각 BindingAdapter - jhm 2022/10/11
object PurchaseItemAdapter {
    @BindingAdapter("app:representThumbnailImagePath")
    @JvmStatic
    fun loadImg(imageView: ImageView, url: String) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(20))

        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:purchaseAt"
    )
    @JvmStatic
    fun loadData(
        textView: TextView,
        date: String?,
    ) {
        val localDate = date;

        val year = localDate?.substring(0, 4)
        val month = localDate?.substring(5, 7)
        val day = localDate?.substring(8, 10)

        val newDate = "$year.$month.$day"
        textView.text = newDate
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:purchasePieceVolume"
    )
    @JvmStatic
    fun loadVolume(
        textView: TextView,
        volume: String?,
    ) {

        val purchaseVolume = volume

        textView.text = purchaseVolume
    }

}