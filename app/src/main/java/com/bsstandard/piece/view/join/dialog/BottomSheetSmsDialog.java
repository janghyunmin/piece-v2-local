package com.bsstandard.piece.view.join.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel;
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList;
import com.bsstandard.piece.data.dto.SmsVerificationDTO;
import com.bsstandard.piece.data.viewmodel.CallSmsAuthViewModel;
import com.bsstandard.piece.data.viewmodel.SmsViewModel;
import com.bsstandard.piece.data.viewmodel.VerifyViewModel;
import com.bsstandard.piece.databinding.SmsDialogBinding;
import com.bsstandard.piece.view.passcode.PassCodeActivity;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * packageName    : com.bsstandard.piece.view.join.dialog
 * fileName       : BottomSheetSmsDialog
 * author         : piecejhm
 * date           : 2022/06/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/23        piecejhm       최초 생성
 */
public class BottomSheetSmsDialog extends BottomSheetDialogFragment {
    // 공통변수 - jhm 2022/06/15
    public Context context;
    public Window window;
    private View view;

    // ViewBinding - jhm 2022/06/23
    SmsDialogBinding smsDialogBinding;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; // 총 3분 - jhm 2022/06/24
    final int COUNT_DONW_INTERVAL = 1000; // onTick 메소드를 호출할 간격 (1초) - jhm 2022/06/24


    // 인증번호 담는 변수 - jhm 2022/06/24
    private String verifyNum = ""; // 인증번호를 초기화해준다. - jhm 2022/06/24

    // 인증번호 필요데이터 - jhm 2022/06/24
    private SmsViewModel smsViewModel;
    private CallSmsAuthViewModel callSmsAuthViewModel;
    private VerifyViewModel verifyViewModel;
    private SmsAuthModel smsAuthModel;
    public String txSeqNo;
    public String name;
    public String birthday;
    public String sexCd;
    public String ntvFrnrCd;
    public String telComcd;
    public String telNo;
    ArrayList<ConsentList> consentList = new ArrayList<>();

    // PassCodeActivity to bundle - jhm 2022/06/30
    public Bundle passCodeData;

    public BottomSheetSmsDialog(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        smsDialogBinding = SmsDialogBinding.inflate(getLayoutInflater());
        smsDialogBinding.setLifecycleOwner(this);
        view = smsDialogBinding.getRoot();

        // 인증번호 값 check - jhm 2022/06/24
        smsViewModel = ViewModelProviders.of(this).get(SmsViewModel.class);
        smsDialogBinding.setSms(smsViewModel);



        /****************** 인증번호 재사용 ****************************************************************/
        callSmsAuthViewModel = ViewModelProviders.of(this).get(CallSmsAuthViewModel.class); // req sms - jhm 2022/06/22
        txSeqNo = getArguments().getString("txSeqNo");
        name = getArguments().getString("name");

        birthday = getArguments().getString("birthday");
        sexCd = getArguments().getString("sexCd");
        ntvFrnrCd = getArguments().getString("ntvFrnrCd");
        telComcd = getArguments().getString("telComCd");
        telNo = getArguments().getString("telNo");

        consentList = getArguments().getParcelableArrayList("consentList");

        /***********************************************************************************************/



        /****************** 인증번호 검증 ****************************************************************/
        verifyViewModel = ViewModelProviders.of(this).get(VerifyViewModel.class);

        /***********************************************************************************************/



        countDownTimer(); // 인증번호 입력 시간 countDown - jhm 2022/06/24
        verifyNumRetry(); // 인증번호 재발송 로직 - jhm 2022/06/24
        verifySetText(); // 인증번호 입력 후 확인 button 로직 - jhm 2022/06/24


        return view;
    }

    // 인증번호 입력 시간 countDown - jhm 2022/06/24
    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DONW_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                smsDialogBinding.smsTime.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
            }
        }.start();
    }


    // 인증번호 재발송 로직 - jhm 2022/06/24
    public void verifyNumRetry() {
        // 재전송 버튼 - jhm 2022/06/24
        smsDialogBinding.smsRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                countDownTimer();
                callSmsAuthViewModel.postCallSmsAuthData(smsAuthModel);
            }
        });


        // 닫기 버튼 - jhm 2022/06/24
        smsDialogBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                dismiss();
            }
        });
    }


    // 인증문자 입력 - jhm 2022/06/24
    public void verifySetText() {
        Observer<String> smsObserver = smsNumber -> verifyNum = smsNumber;
        smsViewModel.getVerifyNum().observe(this, smsObserver);
        smsViewModel.getVerifyNum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String number) {
                verifyNum = number;
                ConfirmOnclick();
                smsDialogBinding.smsError.setVisibility(View.GONE);
            }
        });
    }


    // 인증문자 입력 후 확인버튼 클릭 로직 - jhm 2022/06/24
    public void ConfirmOnclick() {
        // 입력한 인증번호가 있으면 - jhm 2022/06/24
        if(smsDialogBinding.smsNumber.getText().toString().length() > 0){
            smsDialogBinding.confirmBtn.setSelected(true);

            // 인증번호 요청시 모델 - jhm 2022/06/22
            smsAuthModel = new SmsAuthModel(
                    txSeqNo, name, birthday, sexCd, ntvFrnrCd, telComcd, telNo,
                    "Y", "Y", "Y", "Y",
                    verifyNum
            );

            passCodeData = new Bundle();
            passCodeData.putString("name",name);
            passCodeData.putString("cellPhoneNo",telNo);
            passCodeData.putString("birthDay",birthday);
            passCodeData.putString("gender",sexCd);
            passCodeData.putString("isFido","N");
            passCodeData.putParcelableArrayList("consentList",consentList);


            // 확인 버튼 클릭 - jhm 2022/06/24
            smsDialogBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.logE("onclick..");
                    // 인증번호 검증 요청을 한다. - jhm 2022/06/24
                    verifyViewModel.postCallVerifyData(smsAuthModel);

                    // 인증번호 검증 리턴 데이터를 받는다 - jhm 2022/06/24
                    Gson gson = new Gson();
                    verifyViewModel.verifyData.observe(getViewLifecycleOwner(), new Observer<SmsVerificationDTO>() {
                        @Override
                        public void onChanged(SmsVerificationDTO smsVerificationDTO) {
                            if(smsVerificationDTO.getData()!=null){
                                LogUtil.logE(gson.toJson(smsVerificationDTO));
                                LogUtil.logE("smsVerificationDTO : " + smsVerificationDTO.getData().getRsltMsg());
                                String rsMsg = smsVerificationDTO.getData().getRsltMsg();

                                passCodeData.putString("ci",smsVerificationDTO.getData().getCi());
                                passCodeData.putString("di",smsVerificationDTO.getData().getDi());


                                // 인증 완료 - jhm 2022/06/24
                                if(rsMsg.equals("본인인증 완료")){
                                    smsDialogBinding.smsError.setVisibility(View.GONE);
                                    dismiss();
                                    Intent intent = new Intent(context, PassCodeActivity.class);
                                    intent.putExtras(passCodeData);
                                    startActivity(intent);
                                }
                                // 유효하지 않은 인증번호 - jhm 2022/06/24
                                else {
                                    LogUtil.logE("유효하지 않은 인증번호");
                                    smsDialogBinding.smsError.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            });
        }
        // 입력한 인증번호가 없으면 - jhm 2022/06/24
        else {
            smsDialogBinding.confirmBtn.setSelected(false);
        }
    }


}
