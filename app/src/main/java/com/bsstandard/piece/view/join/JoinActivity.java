package com.bsstandard.piece.view.join;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.databinding.ActivityJoinBinding;
import com.bsstandard.piece.view.intro.IntroActivity;
import com.bsstandard.piece.view.join.dialog.BottomSheetDialog;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;

import java.util.regex.Pattern;

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
public class JoinActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDialog.BottomSheetListener {

    /**
     * 공통 - jhm 2022/05/09
     **/
    Context context;
    BottomSheetDialog bottomSheetDialog;
    private ActivityJoinBinding joinBinding;
    private JoinViewModel joinViewModel;
    private String Generate = "";

    public boolean isBirthChk = false;
    public boolean isNumChk = false;

    // 사용자 정보 변수 - jhm 2022/06/16
    public String mUserName;
    public String mUserBirth;
    public String mUserGender;
    public String mUserAgency;
    public String mUserPhoneNumber;
    public String telComCd = ""; // 통신사 값 filter - jhm 2022/06/22

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        joinBinding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        joinBinding.setLifecycleOwner(this);


        joinViewModel = new ViewModelProvider(this).get(JoinViewModel.class);
        joinBinding.setJv(joinViewModel);

        // 통신사 선택 dialog
        bottomSheetDialog = new BottomSheetDialog(context);

        // 화면 Render 후 첫 실행 함수 - jhm 2022/06/16
        DefaultSetting();

        // statusBar Custom
        setStatusBar();

        // editText 클릭시 title color 변경 로직
        FocusTextLogic();

        // ViewModel 연동처리 로직 - jhm 2022/06/16
        ObserverLogic();


    }

    public void DefaultSetting() {
        // 남자 , 여자 default 셋팅
        joinBinding.manBtn.setSelected(true);
        joinBinding.womanBtn.setSelected(false);

        joinBinding.manBtn.setOnClickListener(this::onClick);
        joinBinding.womanBtn.setOnClickListener(this::onClick);
        joinBinding.backImg.setOnClickListener(this::onClick);
        joinBinding.phoneSelect.setOnClickListener(this::onClick);
    }

    public void setStatusBar() {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void FocusTextLogic() {
        joinBinding.userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    joinBinding.titleName.setTextColor(context.getColor(R.color.black));
                } else {
                    joinBinding.titleName.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });

        joinBinding.userBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    joinBinding.titleBirth.setTextColor(context.getColor(R.color.black));
                } else {
                    joinBinding.titleBirth.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });


        joinBinding.phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    joinBinding.phoneNumTitle.setTextColor(context.getColor(R.color.black));
                } else {
                    joinBinding.phoneNumTitle.setTextColor(context.getColor(R.color.c_b8bcc8));
                }
            }
        });
    }


    public void ObserverLogic() {
        Observer<String> nameObserver = userName -> mUserName = userName;
        Observer<String> birthObserver = userBirth -> mUserBirth = userBirth;
        Observer<String> genderObserver = userGender -> mUserGender = userGender;
        Observer<String> agencyObserver = userAgency -> mUserAgency = userAgency;
        Observer<String> phoneNumberObserver = userPhoneNumber -> mUserPhoneNumber = userPhoneNumber;

        joinViewModel.getUserName().observe(this, nameObserver);
        joinViewModel.getUserBirth().observe(this, birthObserver);
        joinViewModel.getUserGender().observe(this, genderObserver);
        joinViewModel.getUserAgency().observe(this, agencyObserver);
        joinViewModel.getUserPhoneNumber().observe(this, phoneNumberObserver);


        joinViewModel.getUserName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String name) {
                mUserName = name;
                ConfirmOnclick();
            }
        });
        joinViewModel.getUserBirth().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String birth) {
                mUserBirth = birth;
                joinBinding.birthError.setVisibility(View.GONE);
                ConfirmOnclick();
            }
        });
        joinViewModel.getUserGender().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String gender) {
                mUserGender = gender;
                ConfirmOnclick();
            }
        });
        joinViewModel.getUserAgency().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String agency) {
                mUserAgency = agency;
                ConfirmOnclick();
            }
        });
        joinViewModel.getUserPhoneNumber().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phoneNumber) {
                mUserPhoneNumber = phoneNumber;
                joinBinding.phoneNumError.setVisibility(View.GONE);
                ConfirmOnclick();
            }
        });
    }


    // 모든 정보 입력, 확인버튼 클릭 후 로직
    public void ConfirmOnclick() {
        if (joinBinding.userName.getText().toString().length() > 0 &&
                joinBinding.userBirth.getText().toString().length() > 0 &&
                !joinBinding.phoneSelect.getText().equals(context.getText(R.string.j_phone_hint)) &&
                joinBinding.phoneNum.getText().toString().length() > 0) {
            LogUtil.logE("입력 모두 완료 . 유효성검사 진행..");
            joinBinding.confirmBtn.setSelected(true);
            joinBinding.confirmBtn.setText(context.getText(R.string.confirm_btn_title));
            
            // 유효성 검사 체크 로직
            birthValid();
            phoneNumValid();

            joinBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 유효성 검사 체크 로직
                    birthValid();
                    phoneNumValid();

                    if (isBirthChk && isNumChk) {
                        if(joinBinding.manBtn.isSelected()){
                            mUserGender = "M";
                            LogUtil.logE("gender : " + mUserGender);
                        }else {
                            mUserGender = "F";
                            LogUtil.logE("gender : " + mUserGender);
                        }
                        GetConsent(); // 약관 목록 조회 - jhm 2022/06/19
                    }
                }
            });

        } else {
            LogUtil.logE("입력값이 없거나 조건이 맞지 않음.. 다음 Step 진행 불가..");
            joinBinding.confirmBtn.setSelected(false);
            joinBinding.confirmBtn.setText(context.getText(R.string.confirm_btn_next_title));
        }
    }

    // 생년월일 유효성 검사 - jhm 2022/06/17
    public boolean isBirthValid(String str) {
        return Pattern.matches("^((19|20)\\d\\d)?([-/.])?(0[1-9]|1[012])([-/.])?(0[1-9]|[12]|3[01])$", str);
    }
    // 휴대폰 번호 유효성 검사 - jhm 2022/06/17
    public boolean isNumberValid(String str) {
        return Pattern.matches("^\\d{3}\\d{4}\\d{4}$", str);
    }
    public void birthValid() {
        if (!isBirthValid(joinBinding.userBirth.getText().toString())) {
            LogUtil.logE("유효하지 않은 생년월일");
            joinBinding.birthError.setVisibility(View.VISIBLE);
            joinBinding.userBirth.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.c_FF7878)));
            isBirthChk = false;
        } else {
            joinBinding.birthError.setVisibility(View.GONE);
            joinBinding.userBirth.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.c_EAECF0)));
            isBirthChk = true;
        }
    }
    public void phoneNumValid() {
        if (!isNumberValid(joinBinding.phoneNum.getText().toString())) {
            LogUtil.logE("유효하지 않은 휴대폰 번호");
            joinBinding.phoneNumError.setVisibility(View.VISIBLE);
            joinBinding.phoneNum.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.c_FF7878)));
            isNumChk = false;
        } else {
            joinBinding.phoneNumError.setVisibility(View.GONE);
            joinBinding.phoneNum.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.c_EAECF0)));
            isNumChk = true;
        }
    }

    // 약관 목록 dialog 호출 - jhm 2022/06/22
    public void GetConsent() {

        Bundle data = new Bundle();
        data.putString("viewType", Division.DIALOG_J_TERMS);
        data.putString("txSeqNo","");
        data.putString("name",joinBinding.userName.getText().toString());
        data.putString("birthday",joinBinding.userBirth.getText().toString());
        data.putString("sexCd",mUserGender);
        data.putString("ntvFrnrCd","L");
        data.putString("telComCd",telComCd);
        data.putString("telNo",joinBinding.phoneNum.getText().toString());
        bottomSheetDialog.setArguments(data);
        bottomSheetDialog.show(getSupportFragmentManager(), Division.DIALOG_J_TERMS);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                Bundle viewType = new Bundle();
                viewType.putString("viewType", Division.DIALOG_J_PHONE);
                bottomSheetDialog.setArguments(viewType);
                bottomSheetDialog.show(getSupportFragmentManager(), Division.DIALOG_J_PHONE);
                break;

            // 뒤로가기 화살표 onClick
            case R.id.back_img:
                Intent intent = new Intent(context, IntroActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }

    @Override
    public void onButtonClick(String selected) {
        joinBinding.phoneSelect.setText(selected);

        switch (selected){
            case "SKT": telComCd="01"; break;
            case "KT": telComCd="02"; break;
            case "LG U+": telComCd="03"; break;
            case "SKT 알뜰폰": telComCd="04"; break;
            case "KT 알뜰폰": telComCd="05"; break;
            case "LG U+ 알뜰폰": telComCd="06"; break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.logE("JoinActivity backPress..");
        PrefsHelper.write("pinNumChk","true");
    }

    @Override
    public void finish() {
        super.finish();
        LogUtil.logE("JoinActivity finish..");
        PrefsHelper.write("pinNumChk","true");
    }
}
