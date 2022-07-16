package com.bsstandard.piece.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 *packageName    : com.bsstandard.piece.model
 * fileName       : PortfolioResponse
 * author         : piecejhm
 * date           : 2022/07/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/10        piecejhm       최초 생성
 */

@Entity(tableName = "port_folio")
data class PortfolioResponse(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "portfolioId")
    var portfolioId: String,

    @ColumnInfo(name ="expectationProfitRate")
    var expectationProfitRate : String,

    @ColumnInfo(name ="representThumbnailImagePath")
    var representThumbnailImagePath : String,

    @ColumnInfo(name ="recruitmentState")
    var recruitmentState : String,

    @ColumnInfo(name ="remainingPieceVolume")
    var remainingPieceVolume : String,

    @ColumnInfo(name ="recruitmentBeginDate")
    var recruitmentBeginDate : String,

    @ColumnInfo(name ="soldoutAt")
    var soldoutAt : String,

    @ColumnInfo(name ="createdAt")
    var createdAt : String,

    @ColumnInfo(name ="shareUrl")
    var shareUrl : String,

    )

class TypeConverterData {
    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): PortfolioResponse? {
        if (data == null) return null
        val listType: Type = object : TypeToken<PortfolioResponse?>() {}.type
        return gson.fromJson<PortfolioResponse?>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObject: PortfolioResponse?): String? {
        return gson.toJson(someObject)
    }
}