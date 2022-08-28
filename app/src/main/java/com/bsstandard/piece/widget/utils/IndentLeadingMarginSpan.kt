package com.bsstandard.piece.widget.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : IndentLeadingMarginSpan
 * author         : piecejhm
 * date           : 2022/08/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/25        piecejhm       최초 생성
 */


class IndentLeadingMarginSpan(
    private val indentDelimiters: List<String> = INDENT_DELIMITERS
) : LeadingMarginSpan {

    private var indentMargin: Int = 0

    override fun getLeadingMargin(first: Boolean): Int = if (first) 0 else indentMargin

    override fun drawLeadingMargin(
        canvas: Canvas, paint: Paint, currentMarginLocation: Int, paragraphDirection: Int,
        lineTop: Int, lineBaseline: Int, lineBottom: Int, text: CharSequence, lineStart: Int,
        lineEnd: Int, isFirstLine: Boolean, layout: Layout
    ) {
        // New Line 일때만 체크
        if (!isFirstLine) {
            return
        }

        // 해당줄의 처음 2글자를 가져옴
        val lineStartText =
            runCatching { text.substring(lineStart, lineStart + 2) }.getOrNull() ?: return
        // 2글자중 마지막 값을 trim한게 delimiter 목록에 포함된다면 해당 길이만큼을 indentMargin 지정
        indentMargin =
            if (indentDelimiters.contains(lineStartText.trimEnd())) {
                paint.measureText(lineStartText).toInt()
            } else {
                0
            }
    }

    companion object {
        private val INDENT_DELIMITERS = listOf("·","•","ㆍ", "-", "┗")
    }
}