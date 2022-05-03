package com.bsstandard.piece.widget.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : EndlessRecyclerViewScrollListener
 * author         : piecejhm
 * date           : 2022/04/28
 * description    : RecyclerView 무한 스크롤 공통 모듈
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/28        piecejhm       최초 생성
 */


abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    // 현재 스크롤 최소 항목 수
    private var visibleThreshold = 5

    // 로드한 데이터의 현재 오프셋 Index
    private var currentPage = 0

    // 마지막 로드 이후 데이터 세트의 총 항목수
    private var previousTotalItemCount = 0

    // 마지막 데이터가 로드되기를 기다리고 있는지 판별
    private var loading = true

    // 시작 페이지 인덱스 설정
    private val startingPageIndex = 0
    var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }


    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

       // 총 항목수가 0이고 이전 항목수가 0이 아닌 경우 체크
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        // 로딩중이고 갯수가 있는지 확인
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // 추가로 데이터 로드시 갯수 비교 후 진행
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // 새로운 검색을 수행 할때마다 아래 메소드 호출
    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // 페이지를 기반으로 실제로 더 많은 데이터를 로드하는 프로세스를 정의
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}