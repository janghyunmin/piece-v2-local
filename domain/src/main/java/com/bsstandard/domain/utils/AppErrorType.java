package com.bsstandard.domain.utils;

import com.bsstandard.domain.entity.DomainModel;

/**
 * packageName    : com.bsstandard.domain.entity
 * fileName       : AppErrorType
 * author         : piecejhm
 * date           : 2022/04/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


public enum AppErrorType implements DomainModel {
    SERVER_IS_NOT_AVAILABLE,
    SERVER_NORMAL_ERROR,
    UNEXPECTED_ERROR
}