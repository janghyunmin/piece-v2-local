package com.bsstandard.piece.widget.utils;

import java.util.Comparator;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : Ascending
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : ArrayList 정렬시 asc;
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class Ascending implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }


}