package com.bsstandard.piece.view.join;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bsstandard.piece.R;
import com.bsstandard.piece.databinding.ActivityJoinBinding;
import com.bsstandard.piece.view.intro.IntroActivity;
import com.bsstandard.piece.widget.utils.BottomSheetDialog;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * packageName    : com.bsstandard.piece.ui.join
 * fileName       : JoinActivityJ
 * author         : piecejhm
 * date           : 2022/05/09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/09        piecejhm       최초 생성
 */

@AndroidEntryPoint
public class JoinActivityJ extends AppCompatActivity implements View.OnClickListener , BottomSheetDialog.BottomSheetListener {

    /** 공통 - jhm 2022/05/09 **/
    Context context;
    BottomSheetDialog bottomSheetDialog;
    private ActivityJoinBinding joinBinding;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        joinBinding = DataBindingUtil.setContentView(this, R.layout.activity_join);

        // 통신사 선택 dialog
        bottomSheetDialog = new BottomSheetDialog();


        // 남자 , 여자 default 셋팅
        joinBinding.manBtn.setSelected(true);
        joinBinding.womanBtn.setSelected(false);
        joinBinding.manBtn.setOnClickListener(this::onClick);
        joinBinding.womanBtn.setOnClickListener(this::onClick);
        joinBinding.backImg.setOnClickListener(this::onClick);
        joinBinding.phoneSelect.setOnClickListener(this::onClick);

        setStatusBar();

        // editText 클릭시 title color 변경 로직
        FocusTextLogic();
        // 모든 정보 입력, 확인버튼 클릭 후 로직
        NextStep();




   }

    public void setStatusBar(){
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logE("onResume..");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.man_btn:
                joinBinding.manBtn.setSelected(true);
                joinBinding.womanBtn.setSelected(false);
                break;

            case R.id.woman_btn:
                joinBinding.manBtn.setSelected(false);
                joinBinding.womanBtn.setSelected(true);
                break;

            // 통신사 선택
            case R.id.phone_select:
                bottomSheetDialog.show(getSupportFragmentManager(),Division.DIALOG_J_PHONE);
                break;

            // 뒤로가기 화살표 onClick
            case R.id.back_img:
                Intent intent = new Intent(context, IntroActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                break;
        }
    }

    @Override
    public void onButtonClick(String selected) {
        joinBinding.phoneSelect.setText(selected);
    }

    public void FocusTextLogic(){
        joinBinding.userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    joinBinding.titleName.setTextColor(context.getColor(R.color.black));
                }else{
                    joinBinding.titleName.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });

        joinBinding.userBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    joinBinding.titleBirth.setTextColor(context.getColor(R.color.black));
                }else{
                    joinBinding.titleBirth.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });

        joinBinding.phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    joinBinding.phoneNumTitle.setTextColor(context.getColor(R.color.black));
                }else{
                    joinBinding.phoneNumTitle.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });
    }


    public void NextStep(){

        joinBinding.phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(joinBinding.userName.getText().toString().length() > 0 &&
                        joinBinding.userBirth.getText().toString().length() > 0 &&
                        !joinBinding.phoneSelect.getText().equals(context.getText(R.string.j_phone_hint)) &&
                        joinBinding.phoneNum.getText().toString().length() > 0 )
                {
                    LogUtil.logE("입력 모두 완료 . 다음 Step 진행..");
                    joinBinding.confirmBtn.setSelected(true);
                    joinBinding.confirmBtn.setText(context.getText(R.string.confirm_btn_title));
                }else{
                    LogUtil.logE("입력값이 없거나 조건이 맞지 않음.. 다음 Step 진행 불가..");
                    joinBinding.confirmBtn.setSelected(false);
                    joinBinding.confirmBtn.setText(context.getText(R.string.confirm_btn_next_title));
                }
            }
        });

    }




}
