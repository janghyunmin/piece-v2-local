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
import com.bsstandard.piece.data.dto.AlarmDTO
import com.bsstandard.piece.data.repository.AlarmRepository
import com.bsstandard.piece.view.adapter.alarm.AlarmAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AlarmViewModel
 * author         : piecejhm
 * date           : 2022/10/16
 * description    : 유저 알림 설정 목록 조회 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/16        piecejhm       최초 생성
 */


class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AlarmRepository = AlarmRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val alarmResponse: MutableLiveData<AlarmDTO> = MutableLiveData()
    @SuppressLint("CheckResult")
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    private val alarmList: ArrayList<AlarmDTO.Data.Alarm> = arrayListOf()

    var alarmAdapter = AlarmAdapter(this, context)
    var n = 1

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getAlarmList(
        accessToken: String,
        deviceId: String,
        memberId: String,
        length: Int,
        notificationType: String
    ) {
        repo.getAlarm(accessToken, deviceId, memberId, length, notificationType).subscribe(
            {
                AlarmDTO ->
                LogUtil.logE("알림 목록 조회 갯수 " + AlarmDTO.data.totalCount)
                LogUtil.logE("알림 및 혜택 각 데이터 " + AlarmDTO.data.alarms)

                alarmList.clear()

                for(i in ArrayList(AlarmDTO.data.alarms).indices) {
                    alarmList.add(AlarmDTO.data.alarms[i])
                    alarmAdapter.notifyDataSetChanged()
                }
            } ,
            {
                throwable ->
                LogUtil.logE("알림 목록 조회 GET List Error ! " + throwable.message)
            }
        )
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = alarmAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getAlarmItem(): List<AlarmDTO.Data.Alarm> {
        return alarmList;
    }
}

object BindingAlarmApdater {
    @BindingAdapter("app:notificationType")
    @JvmStatic
    fun loadImage(imageView: ImageView, notificationType: String) {
        val type = notificationType
        var statusImagePath: String = ""

        when(type) {
            "NTT0101" -> statusImagePath = 
                getURLForResource(R.drawable.ntt0101) // 알림 신청 - jhm 2022/10/16
            
            "NTT0102" -> statusImagePath =
                getURLForResource(R.drawable.ntt0102) // 오픈 예정 - jhm 2022/10/16

            "NTT0103" -> statusImagePath =
                getURLForResource(R.drawable.ntt0103) // 입금 완료 - jhm 2022/10/16

            "NTT0104" -> statusImagePath =
                getURLForResource(R.drawable.ntt0104) // 출금 완료 - jhm 2022/10/16

            "NTT0105" -> statusImagePath =
                getURLForResource(R.drawable.ntt0105) // 구매 완료 - jhm 2022/10/16

            "NTT0106" -> statusImagePath =
                getURLForResource(R.drawable.ntt0106) // 구매 취소 - jhm 2022/10/16

            "NTT0107" -> statusImagePath =
                getURLForResource(R.drawable.ntt0107) // 분배 완료 - jhm 2022/10/16

            "NTT0108" -> statusImagePath =
                getURLForResource(R.drawable.ntt0108) // 오픈 알림 - jhm 2022/10/16

            "NTT0109" -> statusImagePath =
                getURLForResource(R.drawable.ntt0104) // 출금 신청 - jhm 2022/10/16
        }

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(imageView.context)
            .load(statusImagePath)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:createdAt"
    )
    @JvmStatic
    fun loadDate(
        textView: TextView,
        createdAt: String
    ) {
        textView.text = createdAt
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:title"
    )
    @JvmStatic
    fun loadTitle(
        textView: TextView,
        title: String
    ) {
        textView.text = title
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter(
        "app:message"
    )
    @JvmStatic
    fun loadMessage(
        textView: TextView,
        message: String
    ) {
        textView.text = message
    }



    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }
}