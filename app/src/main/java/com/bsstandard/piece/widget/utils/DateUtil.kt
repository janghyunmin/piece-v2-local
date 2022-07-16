package com.bsstandard.piece.widget.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DateUtil
 * author         : piecejhm
 * date           : 2022/07/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/10        piecejhm       최초 생성
 */

class DateUtil{

    companion object{
        fun changeDateFormat(strDate: String?): String {
            if(strDate.isNullOrEmpty()){
                return ""
            }
            return try{
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                requiredSdf.format(sourceSdf.parse(strDate))
            }catch (ex: Exception){
                ""
            }
        }
    }
}