package com.bsstandard.piece.widget.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bsstandard.piece.R;
import com.bsstandard.piece.databinding.CDialogBinding;
import com.bsstandard.piece.databinding.CDialogPasswordBinding;
import com.bsstandard.piece.view.join.JoinActivity;

import java.util.Objects;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : CommonDialog
 * author         : piecejhm
 * date           : 2022/06/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/13        piecejhm       최초 생성
 */

public class CommonDialog extends Dialog{
    Context mContext;
    Activity activity;
    String division;

    CDialogBinding dialogBinding; // 업데이트 Dialog
    CDialogPasswordBinding passcodeBinding; // 비밀번호 재설정 Dialog

    public CommonDialog(@NonNull Context mContext, Activity activity, String division){
        super(mContext);
        this.mContext = mContext;
        this.activity = activity;
        this.division = division;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtil.logE("division : " + division);
        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        switch (division){
            case "version":
                dialogBinding = DataBindingUtil.setContentView(activity,R.layout.c_dialog);
                dialogBinding.okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.logE("업데이트 하러 이동");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mContext.getResources().getString(R.string.playstore_url)));
                        mContext.startActivity(intent);
                        dismiss();
                        activity.finish();
                    }
                });
                break;

            case "password":
                passcodeBinding = DataBindingUtil.setContentView(activity,R.layout.c_dialog_password);
                passcodeBinding.cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.logE("닫기 onclick..");
                        dismiss();
                    }
                });
                passcodeBinding.rePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.logE("비밀번호 재설정");
                        Intent go_join = new Intent(mContext, JoinActivity.class);
                        mContext.startActivity(go_join);
                    }
                });
                break;
        }
    }
}