package com.bsstandard.piece.widget.utils;

import java.text.DecimalFormat;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : MoneyFormatToWon
 * author         : piecejhm
 * date           : 2022/10/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */


public class MoneyFormatToWon {

    public static String moneyFormatToWon(int originNumber) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        return String.valueOf(decimalFormat.format(originNumber));
    }
}
