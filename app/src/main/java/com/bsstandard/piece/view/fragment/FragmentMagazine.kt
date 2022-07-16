package com.bsstandard.piece.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bsstandard.piece.R

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMagazine
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 매거진 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

class FragmentMagazine : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_magazine,container,false)
    }
}