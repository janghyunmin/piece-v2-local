package com.bsstandard.piece.widget.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bsstandard.piece.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : BottomSheetDialog
 * author         : piecejhm
 * date           : 2022/05/09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/09        piecejhm       최초 생성
 */
public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    // 초기 변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener mListener;

    // 항목 findViewById
    private TextView skt_tv, kt_tv, lg_u_tv, skt_sub_tv, kt_sub_tv, lg_u_sub_tv;

    // 바텀시트 닫기 버튼
    private ImageView close_btn;


    // selected String 변수
    private String selected_ = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.popup_slideup, container, false);

        mListener = (BottomSheetListener) getContext();
        skt_tv = view.findViewById(R.id.skt_tv);
        kt_tv = view.findViewById(R.id.kt_tv);
        lg_u_tv = view.findViewById(R.id.lg_tv);
        skt_sub_tv = view.findViewById(R.id.skt_sub_tv);
        kt_sub_tv = view.findViewById(R.id.kt_sub_tv);
        lg_u_sub_tv = view.findViewById(R.id.lg_sub_tv);
        close_btn = view.findViewById(R.id.close_btn);

        skt_tv.setOnClickListener(this::onClick);
        kt_tv.setOnClickListener(this::onClick);
        lg_u_tv.setOnClickListener(this::onClick);
        skt_sub_tv.setOnClickListener(this::onClick);
        kt_sub_tv.setOnClickListener(this::onClick);
        lg_u_sub_tv.setOnClickListener(this::onClick);
        close_btn.setOnClickListener(this::onClick);


        return view;
    }

    @Override
    public void onViewStateRestored(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        switch (selected_) {
            case "SKT":
                skt_tv.setTextColor(Color.BLACK);
                break;
            case "KT":
                kt_tv.setTextColor(Color.BLACK);
                break;
            case "LG U+":
                lg_u_tv.setTextColor(Color.BLACK);
                break;
            case "SKT 알뜰폰":
                skt_sub_tv.setTextColor(Color.BLACK);
                break;
            case "KT 알뜰폰":
                kt_sub_tv.setTextColor(Color.BLACK);
                break;
            case "LG U+ 알뜰폰":
                lg_u_sub_tv.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skt_tv:
                selected_ = skt_tv.getText().toString();
                break;
            case R.id.kt_tv:
                selected_ = kt_tv.getText().toString();
                break;
            case R.id.lg_tv:
                selected_ = lg_u_tv.getText().toString();
                break;
            case R.id.skt_sub_tv:
                selected_ = skt_sub_tv.getText().toString();
                break;
            case R.id.kt_sub_tv:
                selected_ = kt_sub_tv.getText().toString();
                break;
            case R.id.lg_sub_tv:
                selected_ = lg_u_sub_tv.getText().toString();
                break;
            case R.id.close_btn:
                dismiss();
                break;
        }
        mListener.onButtonClick(selected_);
        dismiss();
    }



    // 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClick(String selected);
    }


}
