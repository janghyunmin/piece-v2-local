package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.dto.BoardDTO
import com.bsstandard.piece.data.repository.NoticeRepository
import com.bsstandard.piece.view.adapter.notice.NoticeAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : NoticeViewModel
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 공지사항 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */

@HiltViewModel
class NoticeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: NoticeRepository = NoticeRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var noticeAdapter = NoticeAdapter(this,context)
    private val noticeList: ArrayList<BoardDTO.Data.Board> = arrayListOf()

    private var _liveData : MutableLiveData<ArrayList<BoardDTO.Data.Board>> = MutableLiveData()
    val liveData : LiveData<ArrayList<BoardDTO.Data.Board>> get() = _liveData


    @SuppressLint("CheckResult","NotifyDataSetChanged")
    fun getNotice() {
        repo.getNoticeList().subscribe(
            {
                BoardDTO ->
                LogUtil.logE("공지사항 갯수 : " + BoardDTO.data.boards.size)

                noticeList.clear()
                for(i in ArrayList(BoardDTO.data.boards).indices) {
                    noticeList.add(BoardDTO.data.boards[i])
                    LogUtil.logE("BoardDTO : " + BoardDTO.data.boards[i])
                    _liveData.value = noticeList
                    _liveData.postValue(noticeList)
                }
                noticeAdapter.notifyDataSetChanged()
            } , {
                throwable -> LogUtil.logE("공지사항 GET List Error!$throwable")
            }
        )
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = noticeAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getNoticeItem() : List<BoardDTO.Data.Board> {
        return noticeList;
    }

    fun getItemCount() {
        _liveData.value = noticeList
    }
}

object NotiBindingAdapter {
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:createdAt")
    @JvmStatic
    fun loadDate(
        textView: TextView,
        noticeDate: String?,
    ) {
        val _date = noticeDate
        textView.text = _date
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:title")
    @JvmStatic
    fun loadTitle(
        textView: TextView,
        title: String?,
    ) {
        val _title = title
        textView.text = _title
    }
}