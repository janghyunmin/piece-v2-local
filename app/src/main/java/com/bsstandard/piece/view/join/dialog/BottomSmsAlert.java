package com.bsstandard.piece.view.join.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bsstandard.piece.R;
import java.util.Objects;

/**
 * packageName    : com.bsstandard.piece.view.join.dialog
 * fileName       : BottomSmsAlert
 * author         : piecejhm
 * date           : 2022/06/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/23        piecejhm       최초 생성
 */
public class BottomSmsAlert extends Dialog{
    private static BottomSmsAlert bottomSmsAlert = null;
    private static Context mContext;
    private TextView content , okBtn;
    private String Contents;

    public static BottomSmsAlert getInstance(Context context){
        mContext = context.getApplicationContext();
        if(bottomSmsAlert == null){
            bottomSmsAlert = new BottomSmsAlert(context , "");
        }
        return bottomSmsAlert;
    }

    public BottomSmsAlert(@NonNull Context context, String Contents) {
        super(context);
        this.Contents = Contents;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_alert);


        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        content = findViewById(R.id.content);
        okBtn = findViewById(R.id.okBtn);

        content.setText(Contents);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

}
