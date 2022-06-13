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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bsstandard.piece.R;
import com.bsstandard.piece.view.intro.IntroActivity;

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

public class CommonDialog extends Dialog implements View.OnClickListener{

    Context mContext;
    TextView okBtn;
    Activity introActivity;


    public CommonDialog(@NonNull Context mContext, Activity activity){
        super(mContext);
        this.mContext = mContext;
        this.introActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_dialog);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);


        // findViewById
        okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okBtn:
                LogUtil.logE("업데이트 하러 이동");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=run.piece.dev"));
                mContext.startActivity(intent);
                dismiss();
                introActivity.finish();
                break;

        }
    }



}