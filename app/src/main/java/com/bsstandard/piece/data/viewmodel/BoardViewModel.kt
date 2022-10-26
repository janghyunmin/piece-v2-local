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
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.dto.BoardDTO
import com.bsstandard.piece.data.repository.BoardRepository
import com.bsstandard.piece.view.adapter.notice.NoticeAdapter
import com.bsstandard.piece.view.adapter.question.QuestionAdapter
import com.bsstandard.piece.widget.utils.EndlessRecyclerViewScrollListener
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : BoardViewModel
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 공지사항 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */

@HiltViewModel
class BoardViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: BoardRepository = BoardRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    // 공지사항 Adapter - jhm 2022/09/23
    var noticeAdapter = NoticeAdapter(this, context)

    // 공지사항 ArrayList - jhm 2022/09/23
    private val noticeList: ArrayList<BoardDTO.Data.Board> = arrayListOf()

    // 자주 묻는 질문 Adapter - jhm 2022/09/23
    var questionAdapter = QuestionAdapter(this, context)

    // 자주 묻는 질문 ArrayList - jhm 2022/09/23
    private val questionList: ArrayList<BoardDTO.Data.Board> = arrayListOf()

    private var _liveData: MutableLiveData<ArrayList<BoardDTO.Data.Board>> = MutableLiveData()
    val liveData: LiveData<ArrayList<BoardDTO.Data.Board>> get() = _liveData


//    private var _questionLiveData: MutableLiveData<ArrayList<BoardDTO.Data.Board>> = MutableLiveData()
//    val questionLiveData : LiveData<ArrayList<BoardDTO.Data.Board>> get() = _questionLiveData

    // 자주 묻는 질문 LiveData - jhm 2022/09/28
    private val _questionItemList: MutableLiveData<ArrayList<BoardDTO.Data.Board>> =
        MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()
    val questionItemList: LiveData<ArrayList<BoardDTO.Data.Board>>
        get() = _questionItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition

    init {
        _currentPosition.value = 0
    }



    // 스크롤시 페이징 처리 Listener - jhm 2022/09/23
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


    // 공지사항 - jhm 2022/09/23
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getNotice(boardType: String, length: Int, page: Int) {
        repo.getBoardList("", boardType = boardType, length = length, page = page).subscribe(
            { BoardDTO ->
                LogUtil.logE("공지사항 갯수 : " + BoardDTO.data.boards.size)

                noticeList.clear()
                for (i in ArrayList(BoardDTO.data.boards).indices) {
                    noticeList.add(BoardDTO.data.boards[i])
                    LogUtil.logE("BoardDTO : " + BoardDTO.data.boards[i])
                    _liveData.value = noticeList
                    _liveData.postValue(noticeList)
                }
                noticeAdapter.notifyDataSetChanged()
            }, { throwable ->
                LogUtil.logE("공지사항 GET List Error! $throwable")
            }
        )
    }

    // 자주 묻는 질문 - jhm 2022/09/23
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getQuestion(boardCategory: String, boardType: String, length: Int, page: Int) {

            repo.getBoardList(
                boardCategory = boardCategory,
                boardType = boardType,
                length = length,
                page = page
            ).subscribe(
                { BoardDTO ->
                    LogUtil.logE("자주 묻는 질문 리스트")
                    questionList.clear()

                    viewModelScope.launch {
                        for (i in ArrayList(BoardDTO.data.boards).indices) {
                            questionList.add(BoardDTO.data.boards[i])
                            LogUtil.logE("자주 묻는 질문 List" + BoardDTO.data.boards[i].title)
                            _questionItemList.value = questionList
                            _questionItemList.postValue(questionList)
                        }
                        questionAdapter.notifyDataSetChanged()
                    }
                }, { throwable ->
                    LogUtil.logE("자주 묻는 질문 GET List Error! $throwable")
                }
            )
    }

    fun viewInit(recyclerView: RecyclerView, viewType: String) {
        when (viewType) {
            "공지사항" -> {
                recyclerView.adapter = noticeAdapter
                recyclerView.layoutManager = LinearLayoutManager(getApplication())
            }
            "자주 묻는 질문" -> {
                recyclerView.adapter = questionAdapter
                recyclerView.layoutManager = LinearLayoutManager(getApplication())

                scrollListener = object :
                    EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        LogUtil.logE("스크롤 페이징 처리중..$totalItemsCount")
                    }
                }
            }
        }

    }

    // 공지사항 item list - jhm 2022/09/23
    fun getNoticeItem(): List<BoardDTO.Data.Board> {
        return noticeList;
    }

    // 자주 묻는 질문 item list - jhm 2022/09/23
    fun getQuestionItem(): List<BoardDTO.Data.Board> {
        return questionList;
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

object QuestionBindingAdapter {
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:questionTitle")
    @JvmStatic
    fun loadText(
        textView: TextView,
        questionTitle: String?,
    ) {
        val _title = questionTitle
        textView.text = _title
    }
}