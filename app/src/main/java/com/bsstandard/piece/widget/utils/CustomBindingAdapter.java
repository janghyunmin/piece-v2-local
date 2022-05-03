package com.bsstandard.piece.widget.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxrelay2.Relay;

/**
 * packageName    : com.bsstandard.piece.utils
 * fileName       : CustomBindingAdapter
 * author         : piecejhm
 * date           : 2022/04/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


public class CustomBindingAdapter {

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean visibility) {

        if(visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("onClick")
    public static void onClick(View view, final Relay<Boolean> listener) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.accept(true);
            }
        });
    }
}