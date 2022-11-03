package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.BoardDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : NoticeRepository
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 게시판 목록 조회 요청 (
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */
class BoardRepository(application: Application) {
    val response: RetrofitService = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    // 공지사항 List - jhm 2022/09/23
    // 자주 묻는 질문 List - jhm 2022/09/23
    fun getBoardList(boardCategory: String, boardType: String, length: Int , page: Int): Observable<BoardDTO> =
        response.getBoard(boardCategory = boardCategory, boardType = boardType , length = length , page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    
//    fun getQuestionList(boardType: String): Observable<BoardDTO> =
//        response.getBoard(boardType = boardType)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())


    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: BoardRepository? = null

        fun getInstance(application : Application): BoardRepository? {
            if (instance == null) instance = BoardRepository(application)
            return instance
        }
    }
}