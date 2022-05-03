package com.bsstandard.data.mapper
import com.bsstandard.data.source.local.entity.AuthEntity
import com.bsstandard.domain.model.local.Auth


/**
 *packageName    : com.bsstandard.data.mapper
 * fileName       : Mapper
 * author         : piecejhm
 * date           : 2022/04/27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */



// member/is_di
fun Auth.toEntity() = AuthEntity(
    isDi = isDi,
    isWithdrawal = isWithdrawal,
    isWithdrawalOver = isWithdrawalOver

)


