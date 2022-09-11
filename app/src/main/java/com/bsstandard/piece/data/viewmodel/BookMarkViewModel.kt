/****
 * 현재 파일 사용 안함 , 추후 개선시 처리
 *
 * */

package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BookMarkDTO
import com.bsstandard.piece.data.repository.BookMarkRepository
import com.bsstandard.piece.view.bookmark.BookMarkAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : BookMarkViewModel
 * author         : piecejhm
 * date           : 2022/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/29        piecejhm       최초 생성
 */

@HiltViewModel
class BookMarkViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: BookMarkRepository = BookMarkRepository(application)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")


    private var _liveData : MutableLiveData<ArrayList<BookMarkDTO.Datum>> = MutableLiveData()
    val liveData : LiveData<ArrayList<BookMarkDTO.Datum>> get() = _liveData


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var bookMarkAdapter = BookMarkAdapter(this, context)
    private val bookmarkList: ArrayList<BookMarkDTO.Datum> = arrayListOf()


    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getBookMark(accessToken: String, deviceId: String, memberId: String) {
        viewModelScope.launch {
            repo.getBookMark(
                accessToken = accessToken,
                deviceId = deviceId,
                memberId = memberId
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ BookMarkDTO ->
                bookmarkList.clear()
                LogUtil.logE("북마크 갯수 : " + BookMarkDTO.data.size)

                for(i in 0 until BookMarkDTO.data.size) {
                   bookmarkList.add(BookMarkDTO.data[i])
                    _liveData.value = bookmarkList
                    _liveData.postValue(bookmarkList)
                }
                LogUtil.logE("list size : " + bookmarkList.size)
                bookMarkAdapter.notifyDataSetChanged()


            }, { throwable ->
                LogUtil.logE("회원 북마크 리스트 GET List Error ! " + throwable.message)
            })
        }
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = bookMarkAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getBookMarkItem(): List<BookMarkDTO.Datum> {
        return bookmarkList
    }

    fun getItemCount() {
        _liveData.value = bookmarkList
    }

}