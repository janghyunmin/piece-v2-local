package com.bsstandard.piece.widget.utils

import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : ConvertMoney
 * author         : piecejhm
 * date           : 2022/08/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/19        piecejhm       최초 생성
 */
class ConvertMoney {
    fun getNumKorString(value: Long): String {
        var unitWords = listOf("", "억 ", "만")
        var splitUnit = 10000
        var splitCount = unitWords.size
        var resultArray =  ArrayList<Int>();
        var resultString = ""

//        val decimal = DecimalFormat("#,###")
//        LogUtil.logE(decimal.format(resultArray[0]))

        for (i in 0 until splitCount) {
            var unitResult = (value % Math.pow(
                splitUnit.toDouble(),
                (i + 1).toDouble()
            )) / Math.pow(splitUnit.toDouble(), i.toDouble())
            unitResult = Math.floor(unitResult)

            if (unitResult > 0) {
                resultArray.add(unitResult.toInt())
            }
        }

        for (index in 0 until resultArray.size) {
            resultString = resultArray[index].toString() + unitWords[index] + resultString
        }
        return resultString
    }
}
