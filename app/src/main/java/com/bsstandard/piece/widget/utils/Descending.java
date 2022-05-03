package com.bsstandard.piece.widget.utils;

import java.util.Comparator;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : Descending
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : ArrayList 정렬시 desc;
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class Descending implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }
}