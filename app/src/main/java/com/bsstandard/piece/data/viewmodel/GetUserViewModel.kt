package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.repository.GetUserRepository
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : GetUserViewModel
 * author         : piecejhm
 * date           : 2022/09/06
 * description    :
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


    private var baseAddress : String = ""
    private var detailAddress : String = ""

    private val _liveAddress = MutableLiveData<String>()
    val liveAddress: LiveData<String> get() = _liveAddress

    private val _liveDetailAddress = MutableLiveData<String>()
    val liveDetailAddress: LiveData<String> get() = _liveDetailAddress



    @SuppressLint("CheckResult")
    fun getUserData() {
        repo.getUser()
            ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())


        repo.getUser()?.subscribe(
            {
                MemberDTO ->
                PrefsHelper.write("memberId",MemberDTO?.data?.memberId)
                PrefsHelper.write("name",MemberDTO?.data?.name)
                PrefsHelper.write("pinNumber",MemberDTO?.data?.pinNumber.toString())
                PrefsHelper.write("cellPhoneNo",MemberDTO?.data?.cellPhoneNo)
                PrefsHelper.write("cellPhoneIdNo",MemberDTO?.data?.cellPhoneIdNo)
                PrefsHelper.write("birthDay",MemberDTO?.data?.birthDay)
                PrefsHelper.write("zipCode",MemberDTO?.data?.zipCode.toString())
                PrefsHelper.write("baseAddress",MemberDTO?.data?.baseAddress.toString())
                PrefsHelper.write("detailAddress",MemberDTO?.data?.detailAddress.toString())
                PrefsHelper.write("idNo",MemberDTO?.data?.idNo.toString())
                PrefsHelper.write("ci",MemberDTO?.data?.ci)
                PrefsHelper.write("di",MemberDTO?.data?.di)
                PrefsHelper.write("gender",MemberDTO?.data?.gender)
                PrefsHelper.write("email",MemberDTO?.data?.email.toString())
                PrefsHelper.write("gender",MemberDTO?.data?.gender)
                PrefsHelper.write("isFido",MemberDTO?.data?.isFido)
                PrefsHelper.write("createdAt",MemberDTO?.data?.createdAt)
                PrefsHelper.write("joinDay",MemberDTO?.data?.joinDay)


                baseAddress = MemberDTO?.data?.baseAddress.toString()
                detailAddress = MemberDTO?.data?.detailAddress.toString()


                loadData()
                LogUtil.logE("회원 정보 조회" + MemberDTO?.data?.name)
                LogUtil.logE("member ci" + MemberDTO?.data?.ci)
                LogUtil.logE("member di" + MemberDTO?.data?.di)
                LogUtil.logE("주소 baseAddress" + MemberDTO?.data?.baseAddress)
                LogUtil.logE("주소 detailAddress?" + MemberDTO?.data?.detailAddress)

            }, {
                throwable -> LogUtil.logE("회원 정보 조회 GET Error!" + throwable.message)
            }
        )
    }

//    fun getUserDataList(): ArrayList<String> {
//        LogUtil.logE("memberArrayList : $memberArrayList")
//        return memberArrayList;
//    }

    // 주소 정보 liveData - jhm 2022/09/08
    fun loadData() = viewModelScope.launch {
        _liveAddress.value = baseAddress
        _liveAddress.postValue(baseAddress)

        _liveDetailAddress.value = detailAddress
        _liveDetailAddress.postValue(detailAddress)
    }
}