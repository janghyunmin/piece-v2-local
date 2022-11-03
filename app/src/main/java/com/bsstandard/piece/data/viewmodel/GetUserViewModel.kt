package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.data.datasource.local.room.AppDatabase
import com.bsstandard.piece.data.datasource.local.room.entity.User
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.MemberDTO
import com.bsstandard.piece.data.repository.GetUserRepository
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : GetUserViewModel
 * author         : piecejhm
 * date           : 2022/09/06
 * description    : 회원 정보 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/06        piecejhm       최초 생성
 */

@HiltViewModel
class GetUserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val EVENT_MY_INFO_ACTIVITY = "0001"
    }

    private val repo: GetUserRepository = GetUserRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val memberArrayList: ArrayList<String> = arrayListOf()

    private val _liveAddress = MutableLiveData<String>()
    val liveAddress: LiveData<String> get() = _liveAddress

    private val _liveDetailAddress = MutableLiveData<String>()
    val liveDetailAddress: LiveData<String> get() = _liveDetailAddress

    // Notification LiveData - jhm 2022/09/21
    private var _liveNotification = MutableLiveData<MemberDTO.Data.Notification>()
    val liveNotification: LiveData<MemberDTO.Data.Notification> get() = _liveNotification


    // 분배금 정산을 받기 위한 상태값 조회 데이터 - jhm 2022/10/26
    private val _isIdNo = MutableLiveData<String>()
    val isIdNo: LiveData<String> get() = _isIdNo

    // 분배금 정산 금액 - jhm 2022/10/26
    private val _totalProfitAmount = MutableLiveData<String>()
    val totalProfitAmount: LiveData<String> get() = _totalProfitAmount

    @SuppressLint("CheckResult")
    fun getUserData() {
        repo.getUser()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())


        repo.getUser()?.subscribe(
            { MemberDTO ->
                PrefsHelper.write("memberId", MemberDTO?.data?.memberId)
                PrefsHelper.write("name", MemberDTO?.data?.name)
                PrefsHelper.write("pinNumber", MemberDTO?.data?.pinNumber.toString())
                PrefsHelper.write("cellPhoneNo", MemberDTO?.data?.cellPhoneNo)
                PrefsHelper.write("cellPhoneIdNo", MemberDTO?.data?.cellPhoneIdNo)
                PrefsHelper.write("birthDay", MemberDTO?.data?.birthDay)
                PrefsHelper.write("zipCode", MemberDTO?.data?.zipCode.toString())
                PrefsHelper.write("baseAddress", MemberDTO?.data?.baseAddress.toString())
                PrefsHelper.write("detailAddress", MemberDTO?.data?.detailAddress.toString())
                PrefsHelper.write("idNo", MemberDTO?.data?.idNo.toString())
                PrefsHelper.write("ci", MemberDTO?.data?.ci)
                PrefsHelper.write("di", MemberDTO?.data?.di)
                PrefsHelper.write("gender", MemberDTO?.data?.gender)
                PrefsHelper.write("email", MemberDTO?.data?.email.toString())
                PrefsHelper.write("gender", MemberDTO?.data?.gender)
                PrefsHelper.write("isFido", MemberDTO?.data?.isFido)
                PrefsHelper.write("createdAt", MemberDTO?.data?.createdAt)
                PrefsHelper.write("joinDay", MemberDTO?.data?.joinDay)

                PrefsHelper.write("assetNotification",MemberDTO?.data?.notification?.assetNotification)
                PrefsHelper.write("portfolioNotification",MemberDTO?.data?.notification?.portfolioNotification)
                PrefsHelper.write("marketingSms",MemberDTO?.data?.notification?.marketingSms)
                PrefsHelper.write("marketingApp",MemberDTO?.data?.notification?.marketingApp)


                // room db 저장 - jhm 2022/09/18
                var newUser = User(
                    MemberDTO?.data?.name.toString(),
                    PrefsHelper.read("inputPinNumber", ""),
                    MemberDTO?.data?.cellPhoneNo.toString(),
                    MemberDTO?.data?.birthDay.toString(),
                    MemberDTO?.data?.gender.toString(),
                    MemberDTO?.data?.isFido.toString()
                )

                // 싱글톤 - jhm 2022/09/18
                val db = AppDatabase.getInstance(context)
                CoroutineScope(Dispatchers.IO).launch {
                    db!!.userDao().deleteUserByName(MemberDTO?.data?.name.toString())
                    db.userDao().insert(newUser)
                }

                _liveAddress.value = MemberDTO?.data?.baseAddress.toString()
                _liveAddress.postValue(MemberDTO?.data?.baseAddress.toString())

                _liveDetailAddress.value = MemberDTO?.data?.detailAddress.toString()
                _liveDetailAddress.postValue(MemberDTO?.data?.detailAddress.toString())



                LogUtil.logE("회원 정보 조회" + MemberDTO?.data?.name)
                LogUtil.logE("member ci" + MemberDTO?.data?.ci)
                LogUtil.logE("member di" + MemberDTO?.data?.di)
                LogUtil.logE("주소 baseAddress" + MemberDTO?.data?.baseAddress)
                LogUtil.logE("주소 detailAddress?" + MemberDTO?.data?.detailAddress)
                LogUtil.logE("이메일 : " + MemberDTO?.data?.email)

                _isIdNo.value = MemberDTO?.data?.isIdNo
                _isIdNo.postValue(MemberDTO?.data?.isIdNo)


                _totalProfitAmount.value = MemberDTO?.data?.totalProfitAmount
                _totalProfitAmount.postValue(MemberDTO?.data?.totalProfitAmount)


            }, { throwable ->
                LogUtil.logE("회원 정보 조회 GET Error!" + throwable.message)
            }
        )
    }

}